package com.xbp.common.config.exception;

import com.xbp.common.result.ResultCodeEnum;
import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @author 14343
 * @version 1.0
 * @description: TODO
 * @date 2024/3/1 18:06
 */
@Data
public class XbpException extends RuntimeException{
    private Integer code;
    private String message;

    public XbpException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 接收枚举类型对象
     *
     * @param resultCodeEnum
     */
    public XbpException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }


}
