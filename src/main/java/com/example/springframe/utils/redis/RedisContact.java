package com.example.springframe.utils.redis;

public class RedisContact {

    /**
     * 短信发送次数限制 15分钟之后再发
     *
     */
    public static String TX_COUNT_MESSAGE_EXPIRE = "TX_COUNT_MESSAGE_EXPIRE@";
    /**
     * 短信发送次数限制 找回密码 15分钟之后再发
     *
     */
    public static String TX_COUNT_FORGET_MESSAGE_EXPIRE = "TX_COUNT_FORGET_MESSAGE_EXPIRE@";

}
