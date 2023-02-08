package com.example.springframe.mysqltomd;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class InitRunner {

    @Autowired
    private TableService tableService;

    @Resource
    private MysqltomdConfig mysqltomdConfig;

    public void build() throws IOException {
        List<String> list = new ArrayList<>();
        list.add("fieldName");
        list.add("fieldExplain");
        list.add("fieldType");
        list.add("defaultValue");
        list.add("isEmpty");
        if (mysqltomdConfig.getExcludeField() != null && !"".equals(mysqltomdConfig.getExcludeField())){
            ArrayList<String> strings = new ArrayList<>(Arrays.asList(mysqltomdConfig.getExcludeField().split(",")));
            list.removeIf(strings::contains);
        }

        List<TableInfo> tableList = tableService.getTableList();
        StringBuilder stringBuilder = new StringBuilder();
        for (TableInfo tableInfo : tableList) {
            stringBuilder.append("### ");
            stringBuilder.append(tableInfo.getTableComment());
            stringBuilder.append('(');
            stringBuilder.append(tableInfo.getTableName());
            stringBuilder.append(')');
            stringBuilder.append('\n');
            stringBuilder.append('|');
            if (list.contains("fieldName")){
                stringBuilder.append(" 字段名 |");
            }
            if (list.contains("fieldExplain")){
                stringBuilder.append(" 字段说明 |");
            }
            if (list.contains("fieldType")){
                stringBuilder.append(" 字段类型 |");
            }
            if (list.contains("defaultValue")){
                stringBuilder.append(" 默认值 |");
            }
            if (list.contains("isEmpty")){
                stringBuilder.append(" 是否为空 |");
            }
            stringBuilder.append('\n');
            stringBuilder.append('|');
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append("------ |");
            }
            stringBuilder.append('\n');
            List<FieldInfo> fieldInfoList = tableService.getFieldInfoList(tableInfo.getTableName());
            for (FieldInfo fieldInfo : fieldInfoList) {
                JSONObject jsonObject = (JSONObject) JSON.toJSON(fieldInfo);
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0){
                        stringBuilder.append('|');
                    }
                    stringBuilder.append(jsonObject.get(list.get(i)));
                    stringBuilder.append(" |");
                }
                stringBuilder.append('\n');
            }
            stringBuilder.append('\n');
        }

        if (StringUtils.isBlank(mysqltomdConfig.getFilePath())){
            mysqltomdConfig.setFilePath("./desc/数据库文档.md");
        }
        System.out.println(mysqltomdConfig.getFilePath());
        File writeName = new File(mysqltomdConfig.getFilePath());
        boolean newFile = writeName.createNewFile();
        FileWriter writer = new FileWriter(writeName);
        BufferedWriter out = new BufferedWriter(writer);
        out.write(stringBuilder.toString());
        out.flush();
        out.close();
    }
}