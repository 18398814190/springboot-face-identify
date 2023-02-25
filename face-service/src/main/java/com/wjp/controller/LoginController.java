package com.wjp.controller;

import bean.dto.LoginDTO;
import bean.dto.UserInfoDTO;
import bean.vo.LoginVo;
import bean.vo.ResultVo;
import com.wjp.autoconfig.template.SmsTemplate;
import com.wjp.constant.FaceConstants;
import com.wjp.exception.FaceErrorEnum;
import com.wjp.exception.FaceException;
import com.wjp.service.UserInfoService;
import com.wjp.service.UserService;
import com.wjp.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Resource
    private SmsTemplate smsTemplate;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private UserService userService;

    @Resource
    private UserInfoService userInfoService;

    @Value("${tempFile}")
    private String tempFileDir;

    @GetMapping("/send/code")
    public ResultVo<Object> sendCode(String phoneNmuber) {
        String key = FaceConstants.CODE_KEY + phoneNmuber;
        String code = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotEmpty(code)) {
            return ResultVo.error("验证码未失效，请使用已发送的验证码");
        }
        String randomCode = String.valueOf(CommonUtils.generateValidateCode(4));
        String sendPhone = FaceConstants.PHONE_PREIX + phoneNmuber;
        if (smsTemplate.sendSms(randomCode, sendPhone)) {
            redisTemplate.opsForValue().set(key, randomCode, 5, TimeUnit.MINUTES);
            return ResultVo.success("短信发送成功");
        }
        return ResultVo.error("验证码发送失败");
    }

    /**
     * 验证码检验
     *
     * @param loginDTO
     * @return
     */
    @PostMapping("/loginVerification")
    public ResultVo<LoginVo> loginVerification(@RequestBody @Valid LoginDTO loginDTO) {
        //  1.获取前端手机号和验证码
        String phone = loginDTO.getPhone();
        String code = loginDTO.getVerificationCode();
        //  2.调用Service层
        LoginVo resultMap = userService.login(phone, code);
        //  3.返回结果
        return ResultVo.success(resultMap);
    }

    /**
     * 首次登录---完善资料
     * ResponseEntity:响应实体对象
     */
    @PostMapping("/loginReginfo")
    public ResultVo loginReginfo(@RequestBody UserInfoDTO userInfoDTO) {
        userInfoService.loginReginfo(userInfoDTO);
        return ResultVo.success(null);
    }

    /**
     * 用户登录-上传头像
     *
     * @param headPhoto
     * @return
     */
    @PostMapping("/loginReginfo/head")
    public ResultVo uphead(@RequestParam("file") MultipartFile headPhoto) {
        try {
            return ResultVo.success(userInfoService.uphead(headPhoto));
        } catch (IOException e) {
            return ResultVo.error(e.getMessage());
        }
    }

    @GetMapping("/image/download")
    public ResultVo downloadImage(String imageUrl, HttpServletResponse response) {
        File file = new File(tempFileDir + imageUrl);
        BufferedInputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
            return ResultVo.success(null);
        } catch (Exception e) {
            throw new FaceException(FaceErrorEnum.FILE_DOWNLOAD_EXCEPTION);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                log.error("IO close error!");
            }

        }

    }
}
