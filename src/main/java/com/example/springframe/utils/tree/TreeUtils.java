package com.example.springframe.utils.tree;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.springframe.utils.util.Op;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <b> 树形节点构造器 </b>
 * <p>
 * 功能描述: 根据传入List对象动态构造树结构菜单
 * </p>
 */
@SuppressWarnings("all")
public class TreeUtils {

    public static void main(String[] args) {
        List<Example> list =new ArrayList<>();
        Map<Integer,Integer> map =new HashMap<>();
        for (int i = 0; i < 100; i++) {
            Example build = Example.builder().build();
            build.setId(i);
            if(i!=0){
                build.setParentId(i/10);
            }
            build.setName("name"+i);
            map.put(i/10, Optional.ofNullable(map.get(i/10)).orElse(0)+1);
            build.setSort(map.get(i/10));
            build.setData("测试"+i);
            list.add(build);
        }

        System.out.println(genListTreeByExtend(list));
    }
    /**
     * 树节点排序
     *
     * @param autoSort  是否排序
     * @param treeNodes 树节点列表
     */
    private static void sortTreeNode(boolean autoSort, List<TreeNode> treeNodes) {
        if (autoSort) {
            TreeNode s = new TreeNode();
            s.setChildren(new TreeChildren());
            s.getChildren().setList(treeNodes);
            s.sortChildren();
        }
    }

    /**
     * 树节点列表转json
     *
     * @param treeNodes      树节点
     * @param treeClassCache 节点实体Class信息缓存
     * @return
     */
    private static JSONArray treeToJSON(List<TreeNode> treeNodes, HashMap<Class, TreeNodeClassInfo> treeClassCache) {
        JSONArray res = new JSONArray();
        for (TreeNode treeNode :
                treeNodes) {
            res.add(treeNode.toJSON(treeClassCache));
        }
        return res;
    }

    /**
     * 设置父节点是否有叶子
     *
     * @param currentNode 当前节点
     * @param keyNodeMap  节点集合
     */
    private static void setParentLeaf(TreeNode currentNode, LinkedHashMap<Serializable, Object> keyNodeMap) {
        if (currentNode.isLeaf() || currentNode.hasLeaf()) {
            Object parent = keyNodeMap.get(currentNode.getParentId());
            //多父节点处理
            mapNodeHandle(parent, keyNodeMap);
        }

    }

    /**
     * 多父节点处理
     *
     * @param nodeObj
     * @param keyNodeMap
     */
    private static void mapNodeHandle(Object nodeObj, LinkedHashMap<Serializable, Object> keyNodeMap) {
        if (null == nodeObj) {
            return;
        }
        if (nodeObj instanceof LinkedHashMap) {
            ((LinkedHashMap) nodeObj).values().forEach(node -> mapNodeHandle(node, keyNodeMap));

        } else {
            ((TreeNode) nodeObj).setHasLeaf(true);
            ((TreeNode) nodeObj).setLeaf(false);
            setParentLeaf((TreeNode) nodeObj, keyNodeMap);
        }

    }

    /**
     * 生成纯净树
     *
     * @param treeExtends
     * @param autoSort
     * @return
     */
    private static JSONArray genCleanTree(List<? extends TreeExtend> treeExtends, boolean autoSort) {
        List<TreeNode> treeNodes = new ArrayList<>();
        //缓存节点字节码信息
        HashMap<Class, TreeNodeClassInfo> treeClassCache = new HashMap<>(1);
        if (!CollectionUtils.isEmpty(treeExtends)) {
            // 将节点列表散列为key->node结构
            LinkedHashMap<Serializable, Object> keyNodeMap = new LinkedHashMap<>(1);
            // 根据结果集构造节点列表（存入散列表）
            nodeListToMap(treeExtends, treeClassCache, keyNodeMap);
            // 构造无序的多叉树
            for (Object current : keyNodeMap.values()) {
                //多父级子节点
                if (current instanceof LinkedHashMap) {
                    for (TreeNode currentNode : ((LinkedHashMap<String, TreeNode>) current).values()) {
                        //根节点
                        if (null != currentNode.getParentId() && StrUtil.isBlank(currentNode.getParentId().toString()) || null == keyNodeMap.get(currentNode.getParentId())) {
                            treeNodes.add(currentNode);
                        }
                        //将当前节点放入父节点中
                        else {
                            TreeNode parentNode = (TreeNode) keyNodeMap.get(currentNode.getParentId());
                            parentNode.addChild(currentNode);
                            //根据当前节点设置父节点是否有叶子
                            setParentLeaf(currentNode, keyNodeMap);
                        }
                    }
                    continue;
                }
                TreeNode currentNode = (TreeNode) current;
                //根节点
                if (null != currentNode.getParentId() && StrUtil.isBlank(currentNode.getParentId().toString())
                        || null == keyNodeMap.get(currentNode.getParentId())) {
                    treeNodes.add(currentNode);
                } else {
                    Object parent = keyNodeMap.get(currentNode.getParentId());
                    if (parent instanceof TreeNode) {
                        TreeNode parentNode = (TreeNode) keyNodeMap.get(currentNode.getParentId());
                        parentNode.addChild(currentNode);
                        //根据当前节点设置父节点是否有叶子
                        setParentLeaf(currentNode, keyNodeMap);
                    } else {
                        for (TreeNode pNode :
                                ((LinkedHashMap<String, TreeNode>) parent).values()) {
                            pNode.addChild(currentNode);
                            //根据当前节点设置父节点是否有叶子
                            setParentLeaf(currentNode, keyNodeMap);
                        }
                    }
                }
            }
            //清除没有叶子的分支
            cleanNoLeaf(treeNodes);
            //排序
            sortTreeNode(autoSort, treeNodes);
            //转成json串
            return treeToJSON(treeNodes, treeClassCache);
        }
        return new JSONArray();
    }

    /**
     * 清除没有叶子的分支
     *
     * @param treeNodes
     */
    private static void cleanNoLeaf(List<TreeNode> treeNodes) {
        Iterator<TreeNode> it = treeNodes.iterator();
        while (it.hasNext()) {
            if (!it.next().effectNode()) {
                it.remove();
            }
        }
    }


    /**
     * 节点列表转Map
     *
     * @param treeExtends
     * @param treeClassCache
     * @param keyNodeMap
     */
    private static void nodeListToMap(List<? extends TreeExtend> treeExtends, HashMap<Class, TreeNodeClassInfo> treeClassCache, LinkedHashMap<Serializable, Object> keyNodeMap) {
        for (TreeExtend treeExtend : treeExtends) {
            //缓存节点Class对象，提升反射性能
            TreeNode dataNode = cacheTreeClassInfo(treeExtend, treeClassCache);
            // 节点主键冲突
            if (keyNodeMap.containsKey(dataNode.getId())) {
                if (keyNodeMap.get(dataNode.getId()) instanceof LinkedHashMap) {
                    ((LinkedHashMap) keyNodeMap.get(dataNode.getId())).put(dataNode.getParentId(), dataNode);
                }
                //冲突节点存放key ->{parentNodeKey->node}
                else {
                    LinkedHashMap node = new LinkedHashMap(2);
                    //当前节点
                    node.put(dataNode.getParentId(), dataNode);
                    //已存在冲突节点
                    node.put(((TreeNode) keyNodeMap.get(dataNode.getId())).getParentId(), keyNodeMap.get(dataNode.getId()));
                    keyNodeMap.put(dataNode.getId(), node);
                }
            }
            //不存在该节点则加入散列表
            else {
                keyNodeMap.put(dataNode.getId(), dataNode);
            }
        }
    }

    /**
     * 生成多根节点树
     *
     * @param treeExtends 实体信息
     * @param autoSort    是否自动排序
     * @return 树结构json串
     */
    private static JSONArray genTreeExtendList(List<? extends TreeExtend> treeExtends, boolean autoSort) {
        JSONArray res = new JSONArray();
        List<TreeNode> treeNodes = new ArrayList<>();
        //缓存节点字节码信息
        HashMap<Class, TreeNodeClassInfo> treeClassCache = new HashMap<>(1);
        if (!CollectionUtils.isEmpty(treeExtends)) {
            // 节点列表（散列表，用于临时存储节点对象）
            LinkedHashMap<Serializable, Object> nodeList = new LinkedHashMap<>(1);
            // 根节点
            TreeNode root;
            // 根据结果集构造节点列表（存入散列表）
            nodeListToMap(treeExtends, treeClassCache, nodeList);
            // 构造无序的多叉树
            for (Object record :
                    nodeList.values()) {
                //多父级子节点
                if (record instanceof LinkedHashMap) {
                    for (TreeNode reCordNode :
                            ((LinkedHashMap<String, TreeNode>) record).values()) {
                        if (null != reCordNode.getParentId() && StrUtil.isBlank(reCordNode.getParentId().toString()) || null == nodeList.get(reCordNode.getParentId())) {
                            root = reCordNode;
                            treeNodes.add(root);
                        } else {
                            ((TreeNode) nodeList.get(reCordNode.getParentId())).addChild(reCordNode);
                        }
                    }
                    continue;
                }
                TreeNode node = (TreeNode) record;
                if (null != node.getParentId() && StrUtil.isBlank(node.getParentId().toString())
                        || null == nodeList.get(node.getParentId())) {
                    root = node;
                    treeNodes.add(root);
                } else {
                    Object parentNode = nodeList.get(node.getParentId());
                    if (parentNode instanceof TreeNode) {
                        ((TreeNode) nodeList.get(node.getParentId())).addChild(node);
                    } else {
                        for (TreeNode pNode :
                                ((LinkedHashMap<String, TreeNode>) parentNode).values()) {
                            pNode.addChild(node);
                        }
                    }
                }
            }
            //是否自动排序
            if (autoSort) {
                TreeNode s = new TreeNode();
                s.setChildren(new TreeChildren());
                s.getChildren().setList(treeNodes);
                s.sortChildren();
            }
            //转成json串
            for (TreeNode treeNode :
                    treeNodes) {
                JSONObject json = treeNode.toJSON(treeClassCache);
                if (CollectionUtil.isEmpty((List) json.get("children"))) {
                    json.remove("children");
                }
                if (!(boolean) json.get("isLeaf")) {
                    json.remove("isLeaf");

                }
                res.add(json);
            }
        }
        return res;
    }

    /**
     * 生成树形节点 单根节点
     * 默认包含4个字段 value 为节点主键 name 为节点名称 code为节点 parentId 为上级节点主键
     *
     * @param treeExtends 扩展树节点
     * @param autoSort    是否自动排序
     * @return
     */
    private static JSONObject genTreeExtend(List<? extends TreeExtend> treeExtends, boolean autoSort) {
        //缓存节点字节码信息
        HashMap<Class, TreeNodeClassInfo> treeClassCache = new HashMap<>(1);
        List<TreeNode> dataList = new ArrayList<>();
        treeExtends.forEach(
                treeExtend -> dataList.add(cacheTreeClassInfo(treeExtend, treeClassCache))
        );
        JSONObject res = new JSONObject();
        if (null != dataList && 0 < dataList.size()) {
            // 根节点
            TreeNode root = null;
            // 节点列表（散列表，用于临时存储节点对象）
            LinkedHashMap<Serializable, TreeNode> nodeList = dataList.stream()
                    .collect(
                            Collectors.toMap(
                                    TreeNode::getId,
                                    node -> node,
                                    (n1, n2) -> n1,
                                    LinkedHashMap::new
                            )
                    );
            // 构造多叉树
            for (TreeNode node : nodeList.values()) {
                if (null != node.getParentId() && StrUtil.isBlank(node.getParentId().toString()) || null == nodeList.get(node.getParentId())) {
                    root = node;
                } else {
                    nodeList.get(node.getParentId()).addChild(node);
                }
            }
            //排序
            if (null != root) {
                if (autoSort) {
                    root.sortChildren(treeClassCache);
                }
                res = root.toJSON(treeClassCache);
            }
        }
        return res;
    }

    /**
     * 缓存树节点Class对象，提升反射性能
     *
     * @param treeExtend
     * @param treeClassCache
     * @param <T>
     * @return
     */
    private static <T extends TreeExtend> TreeNode cacheTreeClassInfo(T treeExtend, HashMap<Class, TreeNodeClassInfo> treeClassCache) {
        Class className = treeExtend.getClass();
        TreeNodeClassInfo classInfo = treeClassCache.get(className);
        //缓存字节码信息
        if (null == classInfo) {
            classInfo = new TreeNodeClassInfo();
            treeClassCache.put(className, classInfo);
            classInfo.setType(className);
            List<Field> fields = new ArrayList<>(Arrays.asList(treeExtend.getClass().getDeclaredFields()));
            Field[] superField = treeExtend.getClass().getSuperclass().getDeclaredFields();
            fields.addAll(Arrays.asList(superField));
            //缓存树节点元字段注解
            List<TreeFieldInfo> treeFieldInfoList = new ArrayList<>();
            classInfo.setFieldInfoList(treeFieldInfoList);
            fields.forEach(
                    field -> {
                        field.setAccessible(true);
                        TreeFieldInfo treeFieldInfo = new TreeFieldInfo();
                        treeFieldInfo.setField(field);
                        treeFieldInfo.setTreeSort(field.getAnnotation(TreeSort.class));
                        treeFieldInfo.setTreeProperty(field.getAnnotation(TreeProperty.class));
                        treeFieldInfo.setTreeFieldPrefix(field.getAnnotation(TreeFieldPrefix.class));
                        treeFieldInfoList.add(treeFieldInfo);
                    }
            );
        }
        return parseTreeExtend(treeExtend, classInfo);
    }

    /**
     * 生成单根节点树 默认自动排序
     *
     * @param dataList 源数据
     * @return 树结构json串
     */
    public static JSONObject genSingleRootTreeByExtend(List<? extends TreeExtend> dataList) {
        return genTreeExtend(dataList, true);
    }

    /**
     * 生成单根节点树
     *
     * @param dataList 源数据
     * @param autoSort 是否自动排序
     * @return 树结构json串
     */
    public static JSONObject genSingleRootTreeByExtend(List<? extends TreeExtend> dataList, boolean autoSort) {
        return genTreeExtend(dataList, autoSort);
    }

    /**
     * 生成多根节点树 默认自动排序
     *
     * @param dataList 源数据
     * @return 树结构json串
     */
    public static JSONArray genListTreeByExtend(List<? extends TreeExtend> dataList) {
        return genTreeExtendList(dataList, true);
    }

    /**
     * 生成多根节点树
     *
     * @param dataList 源数据
     * @param autoSort 是否自动排序
     * @return
     */
    public static JSONArray genListTreeByExtend(List<? extends TreeExtend> dataList, boolean autoSort) {
        return genTreeExtendList(dataList, autoSort);
    }

    /**
     * 生成简洁树 自动剪出没有叶子的分支
     *
     * @param dataList 源数据
     * @param autoSort 是否自动排序
     * @return
     */
    public static JSONArray genCleanTreeByExtend(List<? extends TreeExtend> dataList, boolean autoSort) {
        return genCleanTree(dataList, autoSort);
    }

    /**
     * 将扩展树节点转换成树节点
     *
     * @param treeExtend        扩展树节点
     * @param treeNodeClassInfo 节点字节码信息
     * @return 树节点
     */
    private static TreeNode parseTreeExtend(final TreeExtend treeExtend, TreeNodeClassInfo treeNodeClassInfo) {
        TreeNode node = new TreeNode();
        node.setExtend(treeExtend);
        treeNodeClassInfo.getFieldInfoList().forEach(
                treeFieldInfo -> {
                    try {
                        Field f = treeFieldInfo.getField();
                        TreeProperty treeProp = treeFieldInfo.getTreeProperty();
                        nodeDefaultFieldHandle(treeProp, node, f, treeExtend);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
        );
        return node;
    }

    /**
     * 树节点默认字段处理
     *
     * @param treeProp   树节点默认属性
     * @param node       树节点标准对象
     * @param f          当前字段
     * @param treeExtend 树节点扩展对象
     * @throws IllegalAccessException
     */
    private static void nodeDefaultFieldHandle(TreeProperty treeProp, TreeNode node, Field f, TreeExtend treeExtend) throws IllegalAccessException {
        if (null != treeProp) {
            //根据注解设置树节点对应属性值
            //节点主键
            if (TreeField.ID.equals(treeProp.value())) {
                node.setId((Serializable) f.get(treeExtend));
            }
            //节点父id
            else if (TreeField.PARENT_ID.equals(treeProp.value())) {
                node.setParentId((Serializable) f.get(treeExtend));
            }
            //节点展示名称
            else if (TreeField.NAME.equals(treeProp.value())) {
                node.setName(toString(f.get(treeExtend)));
            }
            //节点展示名称
            else if (TreeField.SORT.equals(treeProp.value())) {
                node.setSort(toInteger(f.get(treeExtend)));
            }
            //是否叶子节点
            else if ("isLeaf".equals(f.getName())) {
                node.setLeaf(f.getBoolean(treeExtend));
            }
            //是否有叶子节点
            else if ("hasLeaf".equals(f.getName())) {
                node.setHasLeaf(f.getBoolean(treeExtend));
            }

        }
    }

    static Object getFieldValue(Field field, Object target) throws IllegalAccessException {
        Object value = field.get(target);
        if (field.getType() == Boolean.class || field.getType() == boolean.class) {
            value = value == null ? false : value;
            return value;
        }
        return value;
    }

    static String toString(Object o) {
        return StrUtil.nullToDefault(StrUtil.toString(o), "");
    }
    static Integer toInteger(Object o) {
        return Integer.valueOf(Optional.ofNullable(o).orElse(-1).toString());
    }
}

@Data
class Node extends DefaultTreeExtend {

    private Meta meta;
}

@Data
class Meta implements TreeComplexField {
    private String name;

    private String icon;

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("icon", icon);
        return json;
    }
}
