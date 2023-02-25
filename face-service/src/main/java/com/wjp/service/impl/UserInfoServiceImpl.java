package com.wjp.service.impl;

import bean.dto.UserInfoDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wjp.exception.FaceErrorEnum;
import com.wjp.exception.FaceException;
import com.wjp.mapper.UserInfoMapper;
import com.wjp.pojo.BaseUser;
import com.wjp.pojo.UserInfo;
import com.wjp.service.UserInfoService;
import com.wjp.util.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;


    @Value("${tempFile}")
    private String tempFileDir;

    @Override
    public void loginReginfo(UserInfoDTO userInfoDTO) {
        BaseUser user = ThreadLocalUtils.getUser();
        // 如果已经登录，保存用户信息
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoDTO, userInfo);
        userInfo.setUserId(user.getId());
        // 设置年龄
        userInfo.setUserAge(getAge(userInfoDTO.getUserBirthday()));
        LambdaQueryWrapper<UserInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserInfo::getUserId, user.getId());
        UserInfo selectOne = userInfoMapper.selectOne(lqw);
        if (selectOne != null) {
            userInfoMapper.update(userInfo, lqw);
        } else {
            userInfoMapper.insert(userInfo);
        }

    }

    /**
     * 通过年-月-日获取年龄
     *
     * @param yearMonthDay
     * @return
     */
    private int getAge(String yearMonthDay) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthDay = sdf.parse(yearMonthDay);
            Years years = Years.yearsBetween(new DateTime(birthDay), DateTime.now());
            return years.getYears();
        } catch (ParseException e) {
            log.error("获取用户年龄失败!", e);
            return 0;
        }
    }

    @Override
    public String uphead(MultipartFile headPhoto) throws IOException {
        File faceFile = MultipartFileToFile(headPhoto);
        String path = faceFile.getName();

        if (StringUtils.isEmpty(path)) {
            throw new FaceException(FaceErrorEnum.FACE_NUM_EXCEPTION);
        }

        return path;
    }

    public File MultipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码

        try {
            File fileDir = new File(tempFileDir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File file = File.createTempFile(fileName, prefix, fileDir);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            log.error("error : {}", e.getMessage());
        }
        return null;
    }
}
