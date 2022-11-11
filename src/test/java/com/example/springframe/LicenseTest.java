package com.example.springframe;

import com.example.springframe.license.License;
import com.example.springframe.license.LicenseCreator;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

public class LicenseTest {
    @Test
    void generateLicense() {
        // 生成license需要的一些参数
        License param = new License();
        // 证书授权主体
        param.setSubject("licenseTest");
        // 私钥别名
        param.setPrivateAlias("privateKey");
        // 私钥密码（需要妥善保管，不能让使用者知道）
        param.setKeyPass("cosmo1");
        // 访问私钥库的密码
        param.setStorePass("cosmo1");
        // 证书存储地址
        param.setLicensePath("C:\\haier\\dt-commercialization\\dt-os-plat\\src\\main\\resources\\license\\license.lic");
        // 私钥库所在地址
        param.setPrivateKeysStorePath("C:\\study\\spring-frame\\desc\\keytool\\privateKeys.store");
        // 证书生效时间
        Calendar issueCalendar = Calendar.getInstance();
        param.setIssuedTime(issueCalendar.getTime());
        // 证书失效时间
        Calendar expiryCalendar = Calendar.getInstance();
        // 设置当前时间
        expiryCalendar.setTime(new Date());
        // 往后延长一年 = 授权一年时间
        expiryCalendar.add(Calendar.YEAR, 1);
        param.setExpiryTime(expiryCalendar.getTime());
        // 用户类型
        param.setConsumerType("user");
        // 用户数量
        param.setConsumerAmount(1);
        // 描述
        param.setDescription("测试");
        LicenseCreator licenseCreator = new LicenseCreator(param);
        // 生成license
        licenseCreator.generateLicense();
    }
}
