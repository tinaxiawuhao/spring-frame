package com.example.springframe.utils.tree;

import java.lang.annotation.*;

/**
 * <b> TreeFieldPrefix </b>
 * <p>
 * 功能描述:树节点属性前缀
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface TreeFieldPrefix {
    String value();

    String name() default "";
}
