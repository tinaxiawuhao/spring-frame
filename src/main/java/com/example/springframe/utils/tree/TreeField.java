package com.example.springframe.utils.tree;

/**
 * <b> TreeField </b>
 * <p>
 * 功能描述:树节点核心字段枚举类，用于生成树结构的实体字段，将被注解的字段映射成指定节点属性
 * </p>
 */
public enum TreeField {

    //节点主键
    ID,
    //父节点主键
    PARENT_ID,
    //节点名称
    NAME,
    //排序
    SORT
}
