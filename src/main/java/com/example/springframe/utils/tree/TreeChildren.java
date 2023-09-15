package com.example.springframe.utils.tree;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <b> TreeChildren </b>
 * <p>
 * 功能描述:树形结构 子节点
 * </p>
 */
@Data
class TreeChildren implements Comparator<TreeNode>, Serializable {
    private List<TreeNode> list = new ArrayList();

    /**
     * 获取节点数量
     *
     * @return
     */
    public int getSize() {
        return list.size();
    }

    /**
     * 添加子节点
     *
     * @param node
     */
    public void addChild(TreeNode node) {
        list.add(node);
    }

    /**
     * 节点排序
     */
    public void sortChildren() {
        // 对本层节点进行排序
        // 可根据不同的排序属性，传入不同的比较器，这里传入ID比较器
        SortUtils.sort(list);
        // 对每个节点的下一层节点进行排序
        list.forEach(
                TreeNode::sortChildren
        );
    }

    public void sortChildren(HashMap<Class, TreeNodeClassInfo> treeClassCache) {
        // 对本层节点进行排序
        // 可根据不同的排序属性，传入不同的比较器，这里传入ID比较器
        SortUtils.sortTree(list, treeClassCache);
        // 对每个节点的下一层节点进行排序
        list.forEach(
                treeNode -> treeNode.sortChildren(treeClassCache)
        );
    }

    /**
     * 按照节点名称比较
     *
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(TreeNode o1, TreeNode o2) {
        return o1.getName().compareTo(o2.getName());
    }

    /**
     * 拼接子节点的JSON字符串
     *
     * @return
     */
    @Override
    public String toString() {
        return list.stream().map(TreeNode::toJSON).toList().toString();

    }

    public List<JSONObject> toJSON() {
        return list.stream().map(TreeNode::toJSON).collect(Collectors.toList());

    }

    public List<JSONObject> toJSON(HashMap<Class, TreeNodeClassInfo> classInfo) {
        return list.stream().map(treeNode -> treeNode.toJSON(classInfo)).collect(Collectors.toList());

    }
}
