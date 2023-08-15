package com.example.springframe.utils.tree;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <b> SortUtils </b>
 * <p>
 * 功能描述:对实体对象进行排序，安装指定的字段规则排序
 * </p>
 *
 */

public final class SortUtils {

    /**
     * 树节点排序方法
     *
     * @param data
     */
    public static void sortTree(List<TreeNode> data) {
        if (!CollectionUtil.isEmpty(data)) {
            sort(data, genTreeComparator(data.get(0).getExtend()));
        }
    }


    /**
     * 树节点排序方法
     *
     * @param data
     */
    public static void sortTree(List<TreeNode> data, HashMap<Class, TreeNodeClassInfo> treeClassCache) {
        if (!CollectionUtil.isEmpty(data)) {
            treeClassCache.get(data.get(0).getClass().getName());
            sort(data, genTreeComparator(data.get(0).getExtend()));
        }
    }

    /**
     * 数组排序，根据指定的比较器进行排序
     *
     * @param data       待排序数据
     * @param comparator 比较器
     */
    public static <T> void sort(List<T> data, Comparator<T> comparator) {
        //生成比较器
        if (null == comparator) {
            return;
        }
        //进行数组排序
        data.sort(comparator);
    }

    /**
     * 数组排序 升序，排序规则根据实体类型内的Sort注解
     *
     * @param data  待排序数据
     * @param field 排序字段
     */
    public static <T> void sortASC(List<T> data, String field) {
        if (!CollectionUtil.isEmpty(data)) {
            sort(data, genComparatorASC((Class<T>) data.get(0).getClass(), field));
        }
    }

    /**
     * 数组排序 降序，排序规则根据实体类型内的Sort注解
     *
     * @param data 待排序数据
     */
    public static <T> void sortDESC(List<T> data, String field) {
        if (!CollectionUtil.isEmpty(data)) {
            sort(data, genComparatorDESC((Class<T>) data.get(0).getClass(), field));
        }
    }

    /**
     * 数组排序，排序规则根据实体类型内的Sort注解
     *
     * @param data 待排序数据
     */
    public static <T> void sort(List<T> data) {
        if (!CollectionUtil.isEmpty(data)) {
            sort(data, genComparator((Class<T>) data.get(0).getClass()));
        }
    }

    /**
     * 为实体类型生成比较器 根据实体类型上的Sort注解生成
     *
     * @param type 类型
     * @return 实体比较器
     */
    static <T> Comparator<T> genComparator(Class<T> type) {

        List<Field> sortFields = getSortField(type);
        //空集合直接返回null
        if (CollectionUtil.isEmpty(sortFields)) {
            return null;
        }
        //根据字段注解生成比较器对象
        HashMap<Field, Comparator> comparatorMap = new HashMap<>(1);
        try {
            for (Field sortField : sortFields) {
                String comparatorName = sortField.getAnnotation(TreeSort.class).comparator();
                if (StrUtil.isNotBlank(comparatorName)) {
                    Class comparatorClass = Class.forName(comparatorName);
                    Comparator comparator = (Comparator) comparatorClass.newInstance();
                    comparatorMap.put(sortField, comparator);
                }
            }
            return (o1, o2) -> {
                int compareRes = 0;
                try {
                    int temp = 0;
                    for (Field sortField : sortFields) {
                        sortField.setAccessible(true);
                        Object v1 = sortField.get(o1);
                        Object v2 = sortField.get(o2);
                        //使用注解比较器
                        Comparator comparator = comparatorMap.get(sortField);
                        if (null != comparator) {
                            return comparator.compare(v1, v2);
                        }
                        temp = compareBaseValue(v1, v2, sortField.getAnnotation(TreeSort.class).value());
                        if (0 != temp) {
                            break;
                        }
                    }
                    return temp;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return compareRes;
                }
            };

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static <T> Comparator<T> genComparator(TreeNodeClassInfo classInfo) {
        //根据字段注解生成比较器对象
        HashMap<TreeFieldInfo, Comparator> comparatorMap = new HashMap<>(1);
        classInfo.getFieldInfoList().stream().filter(treeFieldInfo -> null != treeFieldInfo.getTreeSort()).map(
                treeFieldInfo -> {
                    try {
                        String comparatorName = treeFieldInfo.getTreeSort().comparator();
                        if (StrUtil.isNotBlank(comparatorName)) {
                            Class comparatorClass = Class.forName(comparatorName);
                            Comparator comparator = (Comparator) comparatorClass.newInstance();
                            comparatorMap.put(treeFieldInfo, comparator);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
        ).count();
        return (o1, o2) -> {
            int compareRes = 0;
            try {
                for (Map.Entry<TreeFieldInfo, Comparator> entry :
                        comparatorMap.entrySet()) {
                    Field sortField = entry.getKey().getField();
                    Object v1 = sortField.get(o1);
                    Object v2 = sortField.get(o2);
                    //使用注解比较器
                    Comparator comparator = comparatorMap.get(sortField);
                    if (null != comparator) {
                        compareRes = comparator.compare(v1, v2);
                    } else {
                        compareRes = compareBaseValue(v1, v2, entry.getKey().getTreeSort().value());
                    }
                    if (0 != compareRes) {
                        break;
                    }
                }
                return compareRes;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return compareRes;
            }
        };
    }


    /**
     * 基础类型值比较
     *
     * @param v1       比较值
     * @param v2       比较值
     * @param sortType 排序类型
     * @return 比较基础类型
     */
    private static <T> int compareBaseValue(T v1, T v2, SortType sortType) {
        if (v1 == v2 || null == sortType) {
            return 0;
        }
        boolean isASC = SortType.ASC.equals(sortType);
        if (null == v1) {
            return isASC ? -1 : 1;
        } else if (null == v2) {
            return !isASC ? -1 : 1;
        }
        //数字比较
        String type = v1.getClass().getName();
        if (Integer.class.getName().equals(type)
                || Short.class.getName().equals(type)
                || Long.class.getName().equals(type)
                || Double.class.getName().equals(type)
                || Float.class.getName().equals(type)
        ) {
            return isASC ? NumberUtil.compare(Double.valueOf(v1.toString()), Double.valueOf(v2.toString()))
                    : NumberUtil.compare(Double.valueOf(v2.toString()), Double.valueOf(v1.toString()));
        }
        //字符串比较
        return isASC ? StrUtil.toString(v1).compareTo(StrUtil.toString(v2))
                : StrUtil.toString(v2).compareTo(StrUtil.toString(v1));
    }

    /**
     * 生成树节点比较器
     *
     * @return 返回树节点比较器
     */
    public static <T extends TreeExtend> Comparator<TreeNode> genTreeComparator(T treeExtend) {
        //获取扩展属性比较器
        Comparator<T> comparator = genComparator((Class<T>) treeExtend.getClass());
        return (o1, o2) -> {
            //如果没有Sort注解默认按节点名称升序排列
            if (null == comparator) {
                if (o1 == o2) {
                    return 0;
                }
                if (o1 == null) {
                    return -1;
                }
                if (o2 == null) {
                    return 1;
                }
                if (o1.getName() == o2.getName()) {
                    return 0;
                }
                return o1.getName().compareTo(o2.getName());
            }
            //比较扩展属性
            return comparator.compare((T) o1.getExtend(), (T) o2.getExtend());
        };
    }

    public static <T extends TreeExtend> Comparator<TreeNode> genTreeComparator(TreeNodeClassInfo classInfo) {
        //获取扩展属性比较器
        Comparator<T> comparator = genComparator(classInfo);
        return (o1, o2) -> {
            //如果没有Sort注解默认按节点名称升序排列
            if (null == comparator) {
                return o1.getName().compareTo(o2.getName());
            }
            //比较扩展属性
            return comparator.compare((T) o1.getExtend(), (T) o2.getExtend());
        };
    }

    /**
     * 为指定字段生成升序比较器，字段只支持基础类型和String
     *
     * @param type      实体类型
     * @param fieldName 字段名称
     * @return 按字段排序的比较器
     */
    private static <T> Comparator<T> genComparator(Class<T> type, String fieldName, SortType sortType) {
        final Field field = getFieldByName(type, fieldName);
        if (null == field) {
            return null;
        }
        return (o1, o2) -> {
            try {
                Object v1 = field.get(o1);
                Object v2 = field.get(o2);
                return compareBaseValue(v1, v2, sortType);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return 0;
            }
        };
    }

    /**
     * 为指定字段生成比较器，字段只支持基础类型和String
     *
     * @param type      实体类型
     * @param fieldName 字段名称
     * @return 按字段排序的比较器
     */
    private static <T> Comparator<T> genComparatorASC(Class<T> type, String fieldName) {
        return genComparator(type, fieldName, SortType.ASC);
    }

    /**
     * 为指定字段生成升序比较器，字段只支持基础类型和String
     *
     * @param type      实体类型
     * @param fieldName 字段名称
     * @return 按字段排序的比较器
     */
    private static <T> Comparator<T> genComparatorDESC(Class<T> type, String fieldName) {
        return genComparator(type, fieldName, SortType.DESC);
    }

    /**
     * 根据字段名获取字段对象
     *
     * @param type      需要比较的类型
     * @param fieldName 字段名称
     * @return 获取指定对象的指定字段
     */
    private static Field getFieldByName(Class type, String fieldName) {
        try {
            return type.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成排序字段比较器
     *
     * @return 根据字段上的注解信息 生成字段比较器
     */
    private static Comparator<Field> genSortFieldComparator() {
        return (o1, o2) -> {
            TreeSort s1 = o1.getAnnotation(TreeSort.class);
            TreeSort s2 = o2.getAnnotation(TreeSort.class);
            //升序
            if (s1.value().equals(SortType.ASC)) {
                return s1.index() - s2.index();
            }
            //降序
            return s2.index() - s1.index();
        };
    }

    /**
     * 获取排序字段
     *
     * @param c 排序实体类型
     * @return 返回实体上有排序注解的字段
     */
    private static List<Field> getSortField(Class c) {
        Field[] fields = c.getDeclaredFields();
        List<Field> res = new ArrayList<>();
        TreeSort treeSort;
        for (Field field :
                fields) {
            treeSort = field.getAnnotation(TreeSort.class);
            if (null != treeSort) {
                res.add(field);
            }
        }
        if (!CollectionUtil.isEmpty(res)) {
            res.sort(genSortFieldComparator());
        }
        return res;
    }
}
