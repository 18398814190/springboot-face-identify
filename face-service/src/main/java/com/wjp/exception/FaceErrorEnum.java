package com.wjp.exception;

import lombok.Data;
import lombok.Getter;

@Getter
public enum FaceErrorEnum {

    FACE_NUM_EXCEPTION(100, "face_num can not big than 1");

    private Integer code;

    private String message;

    FaceErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}
