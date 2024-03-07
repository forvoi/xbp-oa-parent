package com.xbp.common.result;

import lombok.Data;

/**
 * @author 14343
 * @version 1.0
 * @description: TODO
 * @date 2024/3/1 15:42
 */
@Data
public class Result<T>{

    //返回码
    private Integer code;

    //返回消息
    private String message;

    //返回数据
    private T data;

    private Result(){}


    //封装返回的数据
    private static <T> Result<T>  bulid(T body,ResultCodeEnum resultCodeEnum){
        Result<T> result = new Result<>();

        //封装数据
        if (body!=null){
            result.setData(body);
        }
        //状态码
        result.code=resultCodeEnum.getCode();
        //返回信息
        result.message=resultCodeEnum.getMessage();

        return result;
    }
    //返回成功
    public static <T> Result<T> ok(){
        return bulid(null,ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> ok(T data){
        return bulid(data,ResultCodeEnum.SUCCESS);
    }

    //返回失败
    public static <T> Result<T> fail(){
        return bulid(null,ResultCodeEnum.FAIL);
    }

    public static <T> Result<T> fail(T data){
        return bulid(data,ResultCodeEnum.FAIL);
    }

    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }

}
