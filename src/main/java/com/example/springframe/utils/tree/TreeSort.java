package com.example.springframe.utils.tree;

import java.lang.annotation.*;

/**
 * <b> TreeSort </b>
 * <p>
 * 功能描述:树节点排序
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface TreeSort {
    /**
     * 排序类型 升降
     *
     * @return
     */
    SortType value() default SortType.ASC;

    /**
     * 排序优先级 值越小级别越高
     *
     * @return
     */
    int index() default 0;

    /**
     * 比较器
     *
     * @return
     */
    String comparator() default "";
}
