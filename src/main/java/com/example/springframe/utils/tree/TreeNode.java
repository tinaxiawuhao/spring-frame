package com.example.springframe.utils.tree;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * <b> TreeNode </b>
 * <p>
 * 功能描述:树形结构 节点
 * </p>
 */
@Data
public class TreeNode {
    /**
     * 节点主键
     */
    private Serializable id;

    /**
     * 节点内容 v2
     */
    private String name;

    /**
     * 父节点主键
     */
    private Serializable parentId;

    /**
     * 子节点
     */
    private TreeChildren children = new TreeChildren();

    /**
     * 扩展信息
     */
    private TreeExtend extend;
    /**
     * 排序
     */
    public Integer sort;

    public void setHasLeaf(boolean hasLeaf) {
        extend.hasLeaf = hasLeaf;
    }

    public boolean isLeaf() {
        return extend.getIsLeaf() || children.getSize() <= 0;
    }

    public void setLeaf(boolean isLeaf) {
        extend.isLeaf = isLeaf;
    }

    public boolean hasLeaf() {
        return !isLeaf();
    }

    public boolean effectNode() {
        return extend.hasLeaf || extend.isLeaf;
    }

    /**
     * 兄弟节点横向排序
     */
    public void sortChildren() {
        if (children != null && children.getSize() != 0) {
            children.sortChildren();
        }
    }

    public void sortChildren(HashMap<Class, TreeNodeClassInfo> treeClassCache) {
        if (children != null && children.getSize() != 0) {
            children.sortChildren(treeClassCache);
        }
    }

    /**
     * 添加子节点
     *
     * @param node
     */
    public void addChild(TreeNode node) {
        this.children.addChild(node);
        //根据子节点设置当前节点是否有叶子
        this.setHasLeaf(effectNode());
        //存在子节点说明是非叶子节点
        this.setLeaf(false);
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    public JSONObject toJSON() {
        return toJSON(null);
    }


    JSONObject toJSON(HashMap<Class, TreeNodeClassInfo> classInfo) {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("sort", sort);
        json.put("parentId", parentId);
        json.putAll(classInfo == null ? extend.toJSON() : extend.toJSON(classInfo));
        List<JSONObject> childrenList = classInfo == null ? children.toJSON() : children.toJSON(classInfo);
        if (!CollectionUtil.isEmpty(childrenList)) {
            json.put("children", childrenList);
        }
        json.put("isLeaf", isLeaf());
        json.put("hasLeaf", hasLeaf());
        return json;
    }
}

