package com.wjp.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 异常处理类
 */
@Data
@NoArgsConstructor
public class FaceException extends RuntimeException {
    private FaceErrorEnum errData;

    public FaceException(String errMessage){
        super(errMessage);
    }

    public FaceException(String errMessage, FaceErrorEnum data){
        super(errMessage);
        this.errData = data;
    }

    public FaceException(FaceErrorEnum data){
        super();
        this.errData = data;
    }
}