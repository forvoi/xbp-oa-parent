package com.xbp.common.config.exception;

import com.xbp.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 14343
 * @version 1.0
 * @description: TODO
 * @date 2024/3/1 17:42
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    //全局异常处理，执行的方法
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail().message("执行了全局异常处理。。。");
    }

    //特殊异常处理
    @ResponseBody
    @ExceptionHandler(ArithmeticException.class)
    public Result error(ArithmeticException e){
        e.printStackTrace();
        return Result.fail().message("执行了特殊异常处理");
    }

    //自定义异常处理
    @ResponseBody
    @ExceptionHandler(XbpException.class)
    public Result error(XbpException e){
        e.printStackTrace();
        return Result.fail().code(e.getCode()).message(e.getMessage());
    }
}
