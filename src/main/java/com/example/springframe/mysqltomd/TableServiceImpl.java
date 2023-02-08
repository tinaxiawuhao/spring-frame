package com.example.springframe.mysqltomd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.springframe.mapper.IMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TableServiceImpl implements TableService {

    @Autowired
    private IMapper iMapper;
    @Resource
    private DataSourceProperties dataSourceProperties;
    @Resource
    private MysqltomdConfig mysqltomdConfig;

    @Override
    public List<TableInfo> getTableList() {
        String[] split = dataSourceProperties.getUrl().split("\\?");
        String s = split[0];
        String[] split1 = s.split("/");
        String sql = "SELECT\n" +
               "\tTABLE_NAME AS tableName,\n" +
               "\tTABLE_COMMENT AS tableComment \n" +
               "FROM\n" +
               "\tinformation_schema.TABLES \n" +
               "WHERE\n" +
               "\ttable_schema='" + split1[split1.length-1] + "'";
        List<JSONObject> jsonObjects = iMapper.selectList(sql);
        List<TableInfo> list = new ArrayList<>(jsonObjects.size());
        for (JSONObject jsonObject : jsonObjects) {
            list.add(JSON.toJavaObject(jsonObject, TableInfo.class));
        }
        if (mysqltomdConfig.getExcludeTable() != null && !"".equals(mysqltomdConfig.getExcludeTable())){
            ArrayList<String> strings = new ArrayList<>(Arrays.asList(mysqltomdConfig.getExcludeTable().split(",")));
            list.removeIf(temp -> strings.contains(temp.getTableName()));
        }

        if (mysqltomdConfig.getAppointTable() != null && !"".equals(mysqltomdConfig.getAppointTable())){
            ArrayList<String> strings = new ArrayList<>(Arrays.asList(mysqltomdConfig.getAppointTable().split(",")));
            list.removeIf(temp -> !strings.contains(temp.getTableName()));
        }
        return list;
    }

    @Override
    public List<FieldInfo> getFieldInfoList(String tableName) {
        String[] split = dataSourceProperties.getUrl().split("\\?");
        String s = split[0];
        String[] split1 = s.split("/");
        String sql = "SELECT\n" +
                "\t\tCOLUMN_NAME as fieldName,\n" +
                "\t\tCOLUMN_DEFAULT as  defaultValue,\n" +
                "\t\t ( CASE IS_NULLABLE WHEN 'YES' THEN '是' ELSE '不为空' END ) AS isEmpty,\n" +
                "\t\tDATA_TYPE as fieldType,\n" +
                "\t\tCOLUMN_COMMENT as fieldExplain,\n" +
                "\t\tCOLUMN_TYPE as fieldDetails\n" +
                "FROM\n" +
                "\tinformation_schema.COLUMNS \n" +
                "WHERE\n" +
                "\ttable_schema = '" + split1[split1.length-1] + "'\n" +
                "\tAND table_name = '" + tableName+ "'";
        List<JSONObject> jsonObjects = iMapper.selectList(sql);
        List<FieldInfo> list = new ArrayList<>(jsonObjects.size());
        for (JSONObject jsonObject : jsonObjects) {
            if ("id".equals(jsonObject.getString("fieldName"))){
                jsonObject.put("fieldExplain", "id");
            }
            if ("create_time".equals(jsonObject.getString("fieldName"))){
                jsonObject.put("fieldExplain", "创建时间");
            }
            if ("update_time".equals(jsonObject.getString("fieldName"))){
                jsonObject.put("fieldExplain", "更新时间");
            }
            list.add(JSON.toJavaObject(jsonObject, FieldInfo.class));
        }
        return list;
    }
}
