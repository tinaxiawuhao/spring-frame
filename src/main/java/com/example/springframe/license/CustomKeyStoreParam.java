package com.example.springframe.license;

import de.schlichtherle.license.AbstractKeyStoreParam;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 * 自定义KeyStoreParam，用于将公私钥存储文件存放到其他磁盘位置而不是项目中。现场使用的时候公钥大部分都不会放在项目中的
 */
public class CustomKeyStoreParam extends AbstractKeyStoreParam {

    /**
     * 公钥/私钥在磁盘上的存储路径
     */
    private final String storePath;
    private final String alias;
    private final String storePwd;
    private final String keyPwd;

    public CustomKeyStoreParam(Class clazz, String resource, String alias, String storePwd, String keyPwd) {
        super(clazz, resource);
        this.storePath = resource;
        this.alias = alias;
        this.storePwd = storePwd;
        this.keyPwd = keyPwd;
    }


    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getStorePwd() {
        return storePwd;
    }

    @Override
    public String getKeyPwd() {
        return keyPwd;
    }

    /**
     * AbstractKeyStoreParam里面的getStream()方法默认文件是存储的项目中。
     * 用于将公私钥存储文件存放到其他磁盘位置而不是项目中
     */
    @Override
    public InputStream getStream() throws IOException {
//        return new FileInputStream(new File(storePath));
        File file = ResourceUtils.getFile(storePath);
        if (file.exists()) {
            return new FileInputStream(file);
        } else {
            throw new FileNotFoundException(storePath);
        }
    }
}