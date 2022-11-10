package com.example.springframe.exception;


import com.example.springframe.utils.ICall;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 自定义异常: 封装 {@link RuntimeException} 用于处理 lambda 表达式处理异常
 */
public final class RTException extends RuntimeException {

    public static void call(final ICall call) {
        call.call();
    }

    public static RTException of(String message) {
        return new RTException(message);
    }

    public RTException(String msg) {
        super(msg);
    }

    /**
     * 在异常抛出前，可通过该方法收集异常消息
     *
     * @param consumer {@link Consumer}{@link Consumer<String:异常消息>}
     * @return {@link RTException}
     */
    public RTException go(final Consumer<String> consumer) {
        consumer.accept(this.getMessage());
        return this;
    }

    /**
     * 在异常抛出前，可通过该方法收集异常消息；
     * 判断当异常消息为空时，不执行该方法
     *
     * @param consumer {@link Consumer}{@link Consumer<String:异常消息>}
     * @return {@link RTException}
     */
    public RTException goNonNull(final Consumer<String> consumer) {
        if (Objects.nonNull(this.getMessage())) consumer.accept(this.getMessage());
        return this;
    }

    /**
     * 判断是否抛出 {@link RuntimeException} 异常
     *
     * @param hasTrue {@link Boolean} 为 true 时抛出 {@link RuntimeException} 异常
     * @param message {@link Supplier<String>} 异常消息内容
     */
    public static void throwHasTrue(final boolean hasTrue, final Supplier<String> message) {
        if (hasTrue) throw new RuntimeException(message.get());
    }

    /**
     * 判断是否抛出 {@link RuntimeException} 异常
     *
     * @param hasFalse {@link Boolean} 为 false 时抛出 {@link RuntimeException} 异常
     * @param message  {@link Supplier<String>} 异常消息内容
     */
    public static void throwHasFalse(final boolean hasFalse, final Supplier<String> message) {
        if (hasFalse) throw new RuntimeException(message.get());
    }

    /**
     * 判断是否抛出 {@link RuntimeException} 异常
     *
     * @param obj     {@link Object} 为 null 时抛出 {@link RuntimeException} 异常
     * @param message {@link Supplier<String>} 异常消息内容
     */
    public static void throwHasNull(final Object obj, final Supplier<String> message) {
        if (Objects.isNull(obj)) throw new RuntimeException(message.get());
    }

    /**
     * 判断是否抛出 {@link RuntimeException} 异常
     *
     * @param str     {@link String} 为 null 或空字符串时抛出 {@link RuntimeException} 异常
     * @param message {@link Supplier<String>} 异常消息内容
     */
    public static void throwHasEmpty(final String str, final Supplier<String> message) {
        if (Objects.isNull(str) || Objects.equals("", str)) throw new RuntimeException(message.get());
    }

}
