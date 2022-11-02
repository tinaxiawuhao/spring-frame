package com.example.springframe.enumeration;

public enum State {
    AVAILABLE(0,"可用"),
    DISABLED(1,"禁用");

    public Integer code;
    public String desc;

    private State(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
