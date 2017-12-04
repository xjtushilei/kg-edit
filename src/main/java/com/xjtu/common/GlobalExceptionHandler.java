package com.xjtu.common;


import com.xjtu.common.domain.Error;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author shilei
 * @Date 2017/12/4.
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Object exceptionHandler(HttpServletRequest request,
                                   Exception exception) throws Exception {

        return new Error("发生了异常:" + exception.getLocalizedMessage());
    }
}