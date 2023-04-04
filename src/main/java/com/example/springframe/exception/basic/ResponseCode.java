package com.example.springframe.exception.basic;

import lombok.Getter;

@Getter
public enum ResponseCode implements IResponseCode {
    SUCCESS("成功", 200),
    NEED_CHANGE_PAAS("需要修改初始密码", 220),
    FAIL("失败", 500),
    EXCEPTION("异常", 505),
    TOKEN_INVALID("token失效", 501),
    ILLEGAL_TOKEN("非法token", 502),
    NOTFUND_TOKEN("请求头缺少token", 503),
    FORBIDDEN_ACCESS("无访问权限", 403),
    ACCOUNT_NOTFOUND( "账号不存在",404),
    ACCOUNT_EXIST( "账号已存在",410),
    LOGIN_INVALID("账号或密码错误",405),
    ACCOUNT_EXPIRED("账号过期",406),
    PASSWORD_EXPIRED( "密码过期",407),
    LICENSE_OUTTIME("license许可已经过期",408),
    ACCOUNT_LOCKED( "无法登录，请与管理员联系",409),
    CODE_NOT_SEND( "短信验证码未发送",411),
    CODE_ERROR( "短信验证码错误",412),
    CONNECTION_TIMEOUT("服务连接超时，请重试",51001),
    GETDATA_FAIL("接口调用失败",510002),
    REQUEST_PARAM_ERROR("接口请求参数异常",510003),
    ;
    int code;

    String msg;

    ResponseCode(int code) {
        this.code = code;
    }

    ResponseCode(String msg,int code) {
        this(code);
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
