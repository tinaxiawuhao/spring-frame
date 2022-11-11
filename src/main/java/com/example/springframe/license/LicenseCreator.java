package com.example.springframe.license;

import de.schlichtherle.license.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.text.MessageFormat;
import java.util.prefs.Preferences;

/**
 * License生成类 -- 用于license生成
 */
public class LicenseCreator {

    private final static X500Principal DEFAULT_HOLDER_AND_ISSUER = new X500Principal("CN=Haier, OU=Haier, O=Space, L=SH, ST=SH, C=CN");

    private static final Logger logger = LogManager.getLogger(LicenseCreator.class);

    private final License license;

    public LicenseCreator(License license) {
        this.license = license;
    }

    /**
     * 生成License证书
     */
    public boolean generateLicense() {
        try {
            LicenseManager licenseManager = new CustomLicenseManager(initLicenseParam());
            LicenseContent licenseContent = initLicenseContent();
            licenseManager.store(licenseContent, new File(license.getLicensePath()));
            return true;
        } catch (Exception e) {
            logger.error(MessageFormat.format("证书生成失败：{0}", license), e);
            return false;
        }
    }

    /**
     * 初始化证书生成参数
     */
    private LicenseParam initLicenseParam() {
        Preferences preferences = Preferences.userNodeForPackage(LicenseCreator.class);

        //设置对证书内容加密的秘钥
        CipherParam cipherParam = new DefaultCipherParam(license.getStorePass());

        KeyStoreParam privateStoreParam = new CustomKeyStoreParam(LicenseCreator.class,
                license.getPrivateKeysStorePath(),
                license.getPrivateAlias(),
                license.getStorePass(),
                license.getKeyPass());

        return new DefaultLicenseParam(license.getSubject(),
                preferences,
                privateStoreParam,
                cipherParam);
    }

    /**
     * 设置证书生成正文信息
     */
    private LicenseContent initLicenseContent() {
        LicenseContent licenseContent = new LicenseContent();
        licenseContent.setHolder(DEFAULT_HOLDER_AND_ISSUER);
        licenseContent.setIssuer(DEFAULT_HOLDER_AND_ISSUER);

        licenseContent.setSubject(license.getSubject());
        licenseContent.setIssued(license.getIssuedTime());
        licenseContent.setNotBefore(license.getIssuedTime());
        licenseContent.setNotAfter(license.getExpiryTime());
        licenseContent.setConsumerType(license.getConsumerType());
        licenseContent.setConsumerAmount(license.getConsumerAmount());
        licenseContent.setInfo(license.getDescription());

        //扩展校验，这里可以自定义一些额外的校验信息(也可以用json字符串保存)
        if (license.getLicenseExtraModel() != null) {
            licenseContent.setExtra(license.getLicenseExtraModel());
        }

        return licenseContent;
    }

}