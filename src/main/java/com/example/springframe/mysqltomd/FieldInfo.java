package com.example.springframe.mysqltomd;

import lombok.Data;

@Data
public class FieldInfo {

    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 是否为空
     */
    private String isEmpty;
    /**
     * 字段类型
     */
    private String fieldType;
    /**
     * 字段说明
     */
    private String fieldExplain;
    /**
     * 字段详情
     */
    private String fieldDetails;

}
