package com.example.springframe.utils.tree;

import java.lang.annotation.*;

/**
 * <b> TreeProperty </b>
 * <p>
 * 功能描述:树节点字段属性注解服务
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface TreeProperty {
    TreeField value() default TreeField.ID;
}
