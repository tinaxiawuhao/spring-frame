package com.example.springframe.enumeration;

public enum Platform {
    WEBSOCKET(2,"websocket"),
    INTERFACE(3,"interface");

    public Integer code;
    public String desc;

    private Platform(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
