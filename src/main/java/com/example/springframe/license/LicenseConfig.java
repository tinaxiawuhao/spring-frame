package com.example.springframe.license;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "license")
public class LicenseConfig {

    /**
     * 证书subject
     */
    private String subject;

    /**
     * 公钥别称
     */
    private String publicAlias;

    /**
     * 访问公钥库的密码
     */
    private String storePass;

    /**
     * 证书生成路径
     */
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    private String publicKeysStorePath;

    @Bean(initMethod = "installLicense", destroyMethod = "unInstallLicense")
    public LicenseVerify licenseVerify() {
        return new LicenseVerify(subject, publicAlias, storePass, licensePath, publicKeysStorePath);
    }

}