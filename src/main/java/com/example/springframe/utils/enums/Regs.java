package com.example.springframe.utils.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式预编译
 */
public enum Regs {
    // \d+ 匹配纯数字
    d("\\d+ 匹配纯数字", Pattern.compile("^\\d+$")),
    d_FIND("(\\d+) 匹配并获取纯数字", Pattern.compile("^(\\d+)$")),
    A_Z("[A-Z]+ 匹配[A-Z]", Pattern.compile("[A-Z]+")),
    A_Z_FIND("([A-Z]+) 匹配并获取[A-Z]", Pattern.compile("([A-Z]+)")),
    EXCEL_ADDRESS("Excel 坐标匹配并获取", Pattern.compile("^([A-Z]+)([0-9]+)$")),
    NUMBER("数字匹配", Pattern.compile("^[+-]?\\d+(\\.\\d+)?$")),
    ;
    public final String comment;
    public final Pattern pattern;

    public Matcher matcher(final String value) {
        return pattern.matcher(value);
    }

    public boolean test(final String value) {
        return pattern.matcher(value).matches();
    }

    Regs(final String comment, final Pattern pattern) {
        this.comment = comment;
        this.pattern = pattern;
    }
}
