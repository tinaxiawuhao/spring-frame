package com.example.springframe.utils.util;

import com.alibaba.fastjson.JSON;
import com.example.springframe.utils.enums.Charsets;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Objects;

/**
 * Base64 编码解码工具类
 */
public final class Base64 {

    /**
     * 将指定编码的字符串转换为 Base64 编码字符串
     *
     * @param content String
     * @return String 返回Base64编码字符串
     */
    public static String encode(final String content) {
        return encode(content, null);
    }

    /**
     * 将指定编码的字符串转换为 Base64 编码字符串
     *
     * @param content String
     * @param charset Charset
     * @return String 返回Base64编码字符串
     */
    public static String encode(final String content, Charset charset) {
        if (Util.isEmpty(content)) {
            return null;
        }
        if (Util.isEmpty(charset)) {
            charset = Charsets.UTF_8.charset;
        }
        return org.apache.commons.codec.binary.Base64.encodeBase64String(content.getBytes(charset));
    }

    /**
     * 将 Base64 编码字符串转换为指定编码的字符串
     *
     * @param content String
     * @return String 返回指定编码字符串
     */
    public static String decode(final String content) {
        return decode(content, null);
    }

    /**
     * 将 Base64 编码字符串转换为指定编码的字符串
     *
     * @param content String
     * @param charset Charset
     * @return String 返回指定编码字符串
     */
    public static String decode(final String content, Charset charset) {
        if (Util.isEmpty(content)) {
            return null;
        }
        if (Util.isEmpty(charset)) {
            charset = Charsets.UTF_8.charset;
        }
        return new String(org.apache.commons.codec.binary.Base64.decodeBase64(content), charset);
    }

    /**
     * 将指定编码的字符串转换为 Base64 编码字符串
     *
     * @param content String
     * @return String 返回Base64编码字符串
     */
    public static String encoder(final byte[] content) {
        if (Objects.isNull(content)) {
            return null;
        }
        return org.apache.commons.codec.binary.Base64.encodeBase64String(content);
    }

    /**
     * 将 Base64 编码字符串转换为 byte[]
     *
     * @param content String
     * @return String 返回指定编码字符串
     */
    public static byte[] decoder(String content) {
        if (Util.isEmpty(content)) {
            return null;
        }
        return org.apache.commons.codec.binary.Base64.decodeBase64(content);
    }

    public static void main(String[] args) {
        System.out.println(Base64.encode(JSON.toJSONString(new HashMap<String, String>() {{
            put("", "");
        }})));
        System.out.println(Base64.encode("{\"name\":\"JX\"} "));
        System.out.println(Base64.encode("测试"));
        System.out.println(Base64.encode("5rWL6K+V"));
        System.out.println(Base64.encode(",./<>?<[]-=)_())("));
        System.out.println(Base64.encode("LC4vPD4-PFtdLT0pXygpKSg_"));
        System.out.println(Base64.encode("{1:\"1\"}"));
        System.out.println(Base64.encode("ezE6IjEifQ__"));
        System.out.println(Base64.encode("{role:管理员,description:测试描述}"));
        System.out.println(Base64.decode("eyJpbml0IjpmYWxzZSwicGFzc3dvcmQiOiIxMTExMTEiLCJ1c2VybmFtZSI6IjEwMDAwMDAyMTAx\nIn0="));
    }
}
