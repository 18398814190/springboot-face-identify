package com.wjp.service.impl;

import bean.vo.LoginVO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wjp.constant.FaceConstants;
import com.wjp.exception.FaceErrorEnum;
import com.wjp.exception.FaceException;
import com.wjp.mapper.BaseUserMapper;
import com.wjp.mapper.UserInfoMapper;
import com.wjp.pojo.BaseUser;
import com.wjp.pojo.UserInfo;
import com.wjp.service.UserService;
import com.wjp.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private BaseUserMapper baseUserMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public LoginVO login(String phone, String code) {
//        1.根据传来的phone查询redis，是否有验证码
        String key = FaceConstants.CODE_KEY + phone;
        String redisCode = redisTemplate.opsForValue().get(key);
//        2.如果没有值，表示验证码过期，抛出异常
        if (StringUtils.isEmpty(redisCode)) {
            throw new FaceException(FaceErrorEnum.CODE_MISS_EXCEPTION);
        }
//        3.有值，将前端传来的验证码coed与redis中的验证码比较，不一样则表示验证码输入错误
        if (!redisCode.equals(code)) {
            throw new FaceException(FaceErrorEnum.CODE_CHECK_EXCEPTION);
        }
//        4.之后根据手机号查询数据库
        LambdaQueryWrapper<BaseUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(BaseUser::getPhoneNumber, phone);
        BaseUser user = baseUserMapper.selectOne(lqw);
//        5.如果用户不存在，自动注册，新增用户
        boolean isNew = false;
        if (user == null) {
            user = new BaseUser();
            user.setPhoneNumber(phone);
            baseUserMapper.insert(user);
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(user.getId());
            userInfo.setPhoneNumber(phone);
            userInfoMapper.insert(userInfo);
            isNew = true;
        }
//        6.之后从redis中删除验证码，防止用户重复提交
        redisTemplate.delete(key);
//        7.利用工具类JwtUtils将手机号和ID变成token
        String token = JwtUtils.createToken(user.getId(), phone);
//        8.将token作为key，user对象作为value存入Redis，设置1天有效
        String tokenKey = FaceConstants.TOKEN_KEY + token;

        String userStr = JSON.toJSONString(user); //将对象 转为 字符串
        // 7天登录失效
        redisTemplate.opsForValue().set(tokenKey, userStr, 7, TimeUnit.DAYS);
//        9.返回登录成功的数据（1.token 2.isNew）
        LoginVO map = new LoginVO();
        map.setToken(token);
        map.setUserFlag(isNew);

        return map;
    }
}
