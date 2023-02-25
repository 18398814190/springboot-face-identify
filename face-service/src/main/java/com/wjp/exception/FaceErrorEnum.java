package com.wjp.exception;

import lombok.Data;
import lombok.Getter;

@Getter
public enum FaceErrorEnum {

    FACE_NUM_EXCEPTION(100, "人脸检测异常"),

    CODE_MISS_EXCEPTION(101, "验证码失效"),

    CODE_CHECK_EXCEPTION(102, "验证码输入错误"),

    SESSION_INVALID(103, "登录失效，请重新登录"),

    FILE_DOWNLOAD_EXCEPTION(104, "文件传输异常");

    private Integer code;

    private String message;

    FaceErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}
