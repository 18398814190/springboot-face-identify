package com.wjp.exception;

import lombok.Data;
import lombok.Getter;

@Getter
public enum FaceErrorEnum {

    FACE_NUM_EXCEPTION(100, "人脸检测异常"),

    CODE_MISS_EXCEPTION(101, "验证码失效"),

    CODE_CHECK_EXCEPTION(102, "验证码输入错误"),

    SESSION_INVALID(103, "登录失效，请重新登录"),

    FILE_DOWNLOAD_EXCEPTION(104, "文件传输异常"),

    ROLE_EXCEPTION(105, "没有权限作此操作"),

    DELETE_COMPANY_EXCEPTION(106, "该公司存在员工，不可删除，请先删除公司下的员工");

    private Integer code;

    private String message;

    FaceErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}
