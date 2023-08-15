package com.example.springframe.utils.tree;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * <b> 树结构扩展信息 </b>
 * <p>
 * 功能描述:树形结构 扩展信息
 * </p>
 */
@Data
@Accessors(chain = true)
public class TreeExtend {

    /**
     * 是否是叶子节点
     */
    @ApiModelProperty("是否为叶子节点，true:是叶子节点 false:不是叶子节点")
    protected Boolean isLeaf = false;

    /**
     * 是否有叶子节点
     */
    @ApiModelProperty("是否有子节点，true:有子节点 false:没有子节点")
    protected Boolean hasLeaf = true;


    /**
     * 格式化扩展属性
     *
     * @return
     */
    @Override
    public String toString() {
        return toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject res = new JSONObject();
        try {

            for (Field f : this.getClass().getDeclaredFields()) {
                //如果扩展属性与树节点默认属性重名，则自动跳过，由树节点产生该属性信息
                f.setAccessible(true);

                TreeProperty tf = f.getAnnotation(TreeProperty.class);
                if (null != tf && tf.value().name().toLowerCase().equals(f.getName())) {
                    continue;
                }
                TreeFieldPrefix fieldPrefix = f.getAnnotation(TreeFieldPrefix.class);
                if (null != fieldPrefix) {
                    preFixField(res, f, fieldPrefix);
                } else {
                    Object value = TreeUtils.getFieldValue(f, this);
                    res.put(f.getName(), value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public JSONObject toJSON(HashMap<Class, TreeNodeClassInfo> classInfo) {
        JSONObject json = new JSONObject();
        try {

            for (TreeFieldInfo treeFieldInfo : classInfo.get(this.getClass()).getFieldInfoList()
            ) {
                Field f = treeFieldInfo.getField();
                TreeProperty tf = treeFieldInfo.getTreeProperty();
                if (null != tf && tf.value().name().toLowerCase().equals(treeFieldInfo.getField().getName())) {
                    continue;
                }
                if (f.get(this) instanceof TreeComplexField) {
                    json.put(f.getName(), ((TreeComplexField) f.get(this)).toJSON());
                    continue;
                }
                TreeFieldPrefix fieldPrefix = treeFieldInfo.getTreeFieldPrefix();
                if (null != fieldPrefix) {
                    preFixField(json, f, fieldPrefix);
                } else {
                    Object value = TreeUtils.getFieldValue(f, this);
                    json.put(f.getName(), value);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void preFixField(JSONObject json, Field field, TreeFieldPrefix fieldPrefix) throws IllegalAccessException {
        String prefix = fieldPrefix.value();
        if (StrUtil.isBlank(prefix)) {
            json.put(field.getName(), TreeUtils.getFieldValue(field, this));
        } else {
            JSONObject prefixJSON = json.getJSONObject(prefix);
            if (null == prefixJSON) {
                prefixJSON = new JSONObject();
                json.put(prefix, prefixJSON);
            }
            String name = fieldPrefix.name();
            if ("".equals(name)) {
                name = field.getName();
            }
            prefixJSON.put(name, TreeUtils.getFieldValue(field, this));
        }
    }

}
