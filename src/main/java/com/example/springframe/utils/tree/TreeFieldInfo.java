package com.example.springframe.utils.tree;

import lombok.Data;

import java.lang.reflect.Field;

/**
 * <b> TreeFieldInfo </b>
 * <p>
 * 功能描述:TreeFieldInfo
 * </p>
 */
@Data
class TreeFieldInfo {
    /**
     * 字段对象
     */
    private Field field;
    /**
     * 排序信息
     */
    private TreeSort treeSort;
    /**
     * 映射树属性
     */
    private TreeProperty treeProperty;

    /**
     * 树属性前缀
     */
    private TreeFieldPrefix treeFieldPrefix;
}
