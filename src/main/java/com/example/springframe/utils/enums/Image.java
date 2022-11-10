package com.example.springframe.utils.enums;

import java.util.Objects;

/**
 * 图片格式定义
 */
public enum Image {
    // JPEG
    JPEG, PNG;

    public String base64(final String base64) {
        Objects.requireNonNull(base64, "参数【base64】是必须的");
        return String.format("data:image/%s;base64,", this.name().toLowerCase()).concat(base64);
    }
}