package com.example.springframe.mysqltomd;

import lombok.Data;

/**
 * 数据库表信息
 */
@Data
public class TableInfo {

    /**
     * 表名
     */
    private String tableName;
    /**
     * 描述
     */
    private String tableComment;
}
