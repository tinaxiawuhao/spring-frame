package com.example.springframe.aop.log;

/**
 * @author
 * @date 2021-10-13 11:20
 */
public enum SysLogTypeEnum {

    /**
     * 日志类型
     */
    OPERATION(1, "操作类型"),
    EXCEPTION(2, "异常类型"),
    ;

    /**
     * code
     */
    private int code;

    /**
     * 详情
     */
    private String desc;

    SysLogTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
