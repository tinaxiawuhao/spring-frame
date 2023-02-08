package com.example.springframe.mysqltomd;

import com.example.springframe.license.LicenseVerify;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mysqltomd")
public class MysqltomdConfig {

    /**
     * 件下载路径
     */
    private String filePath;

    /**
     * 排除表名称
     */
    private String excludeTable;

    /**
     * 排除字段名称
     */
    private String excludeField;

    /**
     * 输出表名称
     */
    private String appointTable;

}