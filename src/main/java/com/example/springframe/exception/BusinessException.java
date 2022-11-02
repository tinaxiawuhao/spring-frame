package com.example.springframe.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException{

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 该方式无法支撑多语化，推荐使用 BaseException(IResponseCode responseCode)
     *
     * @param code    异常码
     * @param message 异常信息
     */
    private BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

}
