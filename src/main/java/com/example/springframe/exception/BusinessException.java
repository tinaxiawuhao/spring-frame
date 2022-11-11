package com.example.springframe.exception;

import com.example.springframe.exception.basic.IResponseCode;
import com.example.springframe.exception.basic.ResponseCode;
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
     * 信息参数
     */
    private Object[] args;
    /**
     * 英文错误信息
     */
    private IResponseCode responseCode;

    public BusinessException() {
        this.code = ResponseCode.FAIL.getCode();
    }

    public BusinessException(String message) {
        this(ResponseCode.FAIL.getCode(), message);
        setResponseCode(ResponseCode.FAIL);
    }

    public BusinessException(IResponseCode responseCode) {
        this(responseCode.getCode(), responseCode.getMsg());
        setResponseCode(responseCode);
    }

    public BusinessException(IResponseCode responseCode, Object... args) {
        this(responseCode.getCode(), responseCode.getMsg());
        setArgs(args);
        setResponseCode(responseCode);
    }

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
