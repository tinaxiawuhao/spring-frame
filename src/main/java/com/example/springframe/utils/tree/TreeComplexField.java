package com.example.springframe.utils.tree;

import cn.hutool.json.JSONObject;

import java.io.Serializable;

/**
 * <b> TreeComplexField </b>
 * <p>
 * 功能描述:树节点复杂属性
 * </p>
 */
public interface TreeComplexField extends Serializable {
    /**
     * 转JSON
     *
     * @return
     */
    JSONObject toJSON();
}
