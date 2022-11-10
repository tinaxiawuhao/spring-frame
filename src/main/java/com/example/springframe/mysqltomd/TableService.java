package com.example.springframe.mysqltomd;

import java.util.List;

public interface TableService {

    /**
     * 获取表信息
     * @return List<TableInfo>
     */
    List<TableInfo> getTableList();

    /**
     * 获取表字段信息
     * @return List<FieldInfo>
     */
    List<FieldInfo> getFieldInfoList(String tableName);
}
