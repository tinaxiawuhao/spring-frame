package com.example.springframe.utils.tree;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <b> 树结构默认扩展实体 </b>
 * <p>
 * 功能描述:提供默认的树节点扩展实体，自定义的树节点扩展实体继承该实体即可实现树节点属性扩展，如果自定义实体使用@Data注解，需重写toString方法
 * </p>
 */
@Data
@Accessors(chain = true)
public class DefaultTreeExtend extends TreeExtend {

    @TreeProperty(TreeField.ID)
    protected Serializable id;

    @TreeProperty(TreeField.PARENT_ID)
    protected Serializable parentId;

    @TreeProperty(TreeField.NAME)
    protected String name;

    @Override
    public DefaultTreeExtend setHasLeaf(Boolean hasLeaf) {
        super.setHasLeaf(hasLeaf);
        return this;
    }

    @Override
    public DefaultTreeExtend setIsLeaf(Boolean isLeaf) {
        super.setIsLeaf(isLeaf);
        return this;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
