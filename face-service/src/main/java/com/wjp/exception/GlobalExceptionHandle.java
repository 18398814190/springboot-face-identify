package com.wjp.exception;

import bean.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {

    /**
     * 处理自定义的业务异常
     * @param exception
     * @return
     */
    @ExceptionHandler(FaceException.class)
    public ResultVO<String> handleFaceException(FaceException exception){
        if(ObjectUtils.isNotEmpty(exception.getErrData())){
            return ResultVO.error(exception.getErrData().getMessage());
        }
        log.error("发生异常: 异常信息 ：{}",exception.getMessage());
        return ResultVO.error("未知异常");
    }

    @ExceptionHandler(Exception.class)
    public ResultVO<String> handleException(Exception ex){
        log.error("发生异常: 异常信息 ：{}",ex.getMessage());
        return ResultVO.error(ex.getMessage());
    }
}
