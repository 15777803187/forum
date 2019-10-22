package com.tensquare.base.controller;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 该微服务统一的异常处理类
 */
@ControllerAdvice//注解增强
public class BaseExceptionHandler {

    @ExceptionHandler(value = Exception.class)//该微服务所有抛出的Exception都会被下面的方法捕捉到，并将错误对象传入e
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR,e.getMessage());
    }
}
