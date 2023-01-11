package com.example.springframe.aop.log;

import java.lang.annotation.*;

/**
 * @author
 * @date 2021-10-12 16:07
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SysLog {

    /**
     * 标题 属于哪个模块
     */
    String title();

    /**
     * 操作日志的描述， 支持spring 的 SpEL 表达式。
     */
    String value();

    /**
     * 是否记录方法的入参
     *
     * @return 是否记录方法的入参
     */
    boolean request() default true;

    /**
     * 是否记录返回值
     *
     * @return 是否记录返回值
     */
    boolean response() default true;

}
