package com.example.springframe.utils.tree;

import lombok.Data;

import java.util.List;

/**
 * <b> TreeNodeClassInfo </b>
 * <p>
 * 功能描述:树节点字节码信息
 * </p>
 */
@Data
class TreeNodeClassInfo {
    /**
     * 类名
     */
    private Class type;

    /**
     * 类属性
     */
    private List<TreeFieldInfo> fieldInfoList;


}
