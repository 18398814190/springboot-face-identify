package com.wjp.intercepter;

import com.alibaba.fastjson.JSON;
import com.wjp.constant.FaceConstants;
import com.wjp.exception.FaceErrorEnum;
import com.wjp.exception.FaceException;
import com.wjp.pojo.BaseUser;
import com.wjp.util.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class LoginIntercepter implements HandlerInterceptor {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 前置处理 进入controller之前处理
     * 1.登录请求 要放行(配置类配置即可)
     * 2.非登录请求 判断用户是否登录
     * 2.1 如果用户没有登录 直接返回401（用户没有访问权限，需要进行身份认证）
     * 2.2 如果用户登录了，放行 并将用户信息存入ThreadLocal
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.打印请求路径信息

        log.info("用户IP : {}, 请求了路径 ：{}", request.getRemoteAddr(), request.getRequestURI());
        if (handler instanceof HandlerMethod) {
            // 找出调用的哪个controller中的方法
            HandlerMethod method = (HandlerMethod) handler;
            log.info("执行了: " + method.getShortLogMessage());
        }
        //  1.首先判断是否登录，没有登录就抛出异常
        String token = request.getHeader("token");
        if (StringUtils.isNotEmpty(token)) {
            String tokenKey = FaceConstants.TOKEN_KEY + token;
            String userStr = redisTemplate.opsForValue().get(tokenKey);
            if (StringUtils.isEmpty(userStr)) {
                throw new FaceException(FaceErrorEnum.SESSION_INVALID);
            }
            BaseUser user = JSON.parseObject(userStr, BaseUser.class);
            ThreadLocalUtils.setUser(user);
            //续签
            redisTemplate.expire(tokenKey, 7, TimeUnit.DAYS);
            return true;
        }
        log.info("登录异常，请携带token！");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        //在线程执行完毕后删除ThreadLoacl中的数据，节省内存
        ThreadLocalUtils.remove();
    }

}
