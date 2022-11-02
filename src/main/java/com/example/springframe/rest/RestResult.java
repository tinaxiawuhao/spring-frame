package com.example.springframe.rest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@ApiModel(value = "统一响应实体")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestResult<T> {
    @ApiModelProperty(value = "英文状态描述")
    private String status;

    @ApiModelProperty(value = "状态码")
    private int code;

    @ApiModelProperty(value = "响应消息")
    private String msg;

    @ApiModelProperty(value = "接口响应内容")
    private T data;


    public static <T> RestResult<T> ok() {
        return restResult(null, ResEnum.SUCCESS.getCode(), ResEnum.SUCCESS.getValue(), ResEnum.SUCCESS.toString());
    }

    public static <T> RestResult<T> ok(T data) {
        return restResult(data, ResEnum.SUCCESS.getCode(), ResEnum.SUCCESS.getValue(), ResEnum.SUCCESS.toString());
    }

    public static <T> RestResult<T> ok(T data, String msg) {
        return restResult(null, ResEnum.SUCCESS.getCode(), msg, ResEnum.SUCCESS.toString());
    }

    public static <T> RestResult<T> failed() {
        return restResult(null, ResEnum.FAIL.getCode(), ResEnum.FAIL.getValue(), ResEnum.FAIL.toString());
    }

    public static <T> RestResult<T> failed(String msg) {
        return restResult(null, ResEnum.FAIL.getCode(), msg, ResEnum.FAIL.toString());
    }

    public static <T> RestResult<T> failed(T data) {
        return restResult(data, ResEnum.FAIL.getCode(), ResEnum.FAIL.getValue(), ResEnum.FAIL.toString());
    }

    public static <T> RestResult<T> failed(T data, String msg) {
        return restResult(data, ResEnum.FAIL.getCode(), msg, ResEnum.FAIL.toString());
    }

    public static <T> RestResult<T> failed(Integer code, String msg) {
        return restResult(null, code, msg, ResEnum.FAIL.toString());
    }

    public static <T> RestResult<T> failed(ResEnum resEnum) {
        return restResult(null, resEnum.code, resEnum.value, resEnum.toString());
    }

    private static <T> RestResult<T> restResult(T data, int code, String msg, String status) {
        RestResult<T> apiResult = new RestResult<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        apiResult.setStatus(status);
        return apiResult;
    }
    public enum ResEnum {
        SUCCESS("成功", 200),
        NEED_CHANGE_PAAS("需要修改初始密码", 220),
        FAIL("失败", 500),
        EXCEPTION("异常", 505),
        TOKEN_INVALID("token失效", 501),
        ILLEGAL_TOKEN("非法token", 502),
        NOTFUND_TOKEN("未找到token", 503),
        FORBIDDEN_ACCESS("无访问权限", 403),
        ACCOUNT_NOTFOUND( "账号不存在",404),
        LOGIN_INVALID("账号或密码错误",405),
        ACCOUNT_EXPIRED("账号过期",406),
        PASSWORD_EXPIRED( "密码过期",407),
        LICENSE_OUTTIME("license许可已经过期",408),
        ACCOUNT_LOCKED( "无法登录，请与管理员联系",409),
        ;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        private final String value;

        private final int code;

        ResEnum(String value, int code) {
            this.value = value;
            this.code = code;
        }

    }

}
