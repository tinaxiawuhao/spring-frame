package com.example.springframe.utils.util;


import com.example.springframe.utils.ICall;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 构建链式调用，只能操作对象，不支持原始数据类型
 */
public final class Then<T> {
    /**
     * 构造链式调用对象
     *
     * @param obj T
     * @return {@link Then}
     */
    public static <T> Then<T> of(final T obj) {
        return new Then<>(obj);
    }

    /**
     * hasTrue == true 时才执行 supplier，获得新
     *
     * @param hasTrue  boolean
     * @param supplier {@link Supplier}
     * @return {@link Then}
     */
    public static <T> Then<T> of(final boolean hasTrue, final Supplier<T> supplier) {
        return new Then<>(hasTrue ? supplier.get() : null);
    }

    private Then() {
        this.obj = null;
    }

    public Then(final T obj) {
        this.obj = obj;
    }

    private T obj;

    /**
     * 获取链式操作的值
     *
     * @return T
     */
    public T get() {
        return obj;
    }

    /**
     * 执行 consumer
     *
     * @param consumer {@link Consumer}
     * @return {@link Then}
     */
    public Then<T> then(final Consumer<T> consumer) {
        Objects.requireNonNull(consumer, "参数【consumer】不能为null").accept(obj);
        return this;
    }

    /**
     * Objects.nonNull(value) 时才执行 consumer
     *
     * @param value    {@link Object}
     * @param consumer {@link Consumer}
     * @return {@link Then}
     */
    public Then<T> then(final Object value, final Consumer<T> consumer) {
        if (Objects.nonNull(value))
            Objects.requireNonNull(consumer, "参数【consumer】不能为null").accept(obj);
        return this;
    }

    /**
     * hasTrue == true 时才执行 consumer
     *
     * @param hasTrue  boolean
     * @param consumer {@link Consumer}
     * @return {@link Then}
     */
    public Then<T> then(final boolean hasTrue, final Consumer<T> consumer) {
        if (hasTrue)
            Objects.requireNonNull(consumer, "参数【consumer】不能为null").accept(obj);
        return this;
    }

    /**
     * 重新设置值
     *
     * @param value <T>
     * @return {@link Then}
     */
    public Then<T> map(final T value) {
        this.obj = value;
        return this;
    }

    /**
     * 执行 function，获得新值
     *
     * @param function {@link Function}
     * @return {@link Then}
     */
    public <R> Then<R> map(final Function<T, R> function) {
        return Then.of(Objects.requireNonNull(function, "参数【function】不能为null").apply(obj));
    }

//    /**
//     * Objects.nonNull(value) 时才执行 supplier，获得新值
//     *
//     * @param value    {@link Object}
//     * @param supplier {@link Supplier}
//     * @return {@link Then}
//     */
//    public Then<T> map(final Object value, final Supplier<T> supplier) {
//        if (Objects.nonNull(value))
//            this.obj = Objects.requireNonNull(supplier, "参数【supplier】不能为null").get();
//        return this;
//    }

    /**
     * hasTrue == true 时才执行 supplier，获得新值
     *
     * @param hasTrue  boolean
     * @param supplier {@link Supplier}
     * @return {@link Then}
     */
    public Then<T> map(final boolean hasTrue, final Supplier<T> supplier) {
        if (hasTrue)
            this.obj = Objects.requireNonNull(supplier, "参数【supplier】不能为null").get();
        return this;
    }

    /**
     * 判断 obj != null
     *
     * @return {@link Boolean} true:非空，false:空
     */
    public boolean isPresent() {
        return Objects.nonNull(obj);
    }

    /**
     * <pre>
     * 当 obj != null 时 执行 consumer 方法
     * ifPresent(hasError->{})
     *
     * @param consumer {@link Consumer}
     * @return {@link Then}
     */
    public Then<T> ifPresent(final Consumer<T> consumer) {
        if (Objects.nonNull(obj)) consumer.accept(obj);
        return this;
    }

    /**
     * 当 obj == null 时 执行 call 方法
     *
     * @param call {@link ICall}
     * @return {@link Then}
     */
    public Then<T> elsePresent(final ICall call) {
        if (Objects.isNull(obj)) call.call();
        return this;
    }

    /**
     * 将 obj 置空
     *
     * @return {@link Then}
     */
    public Then<T> end() {
        this.obj = null;
        return this;
    }

    /**
     * 判断 Then 数组任何一个值 != null ，就返回 true
     *
     * @param arrs {@link Then}[]
     * @return {@link Then<Boolean>} true:有非空，false:无非空
     */
    public static Then<Boolean> parallelNonNull(Then... arrs) {
        for (Then arr : arrs) {
            if (arr.isPresent()) return Then.of(true);
        }
        return Then.of(null);
    }

    public static void main(String[] args) {
        System.out.println(
                Then.of(RangeInt.builder().min(0).max(10).build())
                        .then(range -> System.out.println("10:" + range.toString()))
                        .then(range -> range.setMax(100))
                        .then(range -> System.out.println("100:" + range.toString()))
                        .then(null, range -> range.setMax(1000))
                        .then(range -> System.out.println("100:" + range.toString()))
                        .then("", range -> range.setMax(1000))
                        .then(range -> System.out.println("1000:" + range.toString()))
                        .then(false, range -> range.setMax(10000))
                        .then(range -> System.out.println("1000:" + range.toString()))
                        .then(true, range -> range.setMax(10000))
                        .then(range -> System.out.println("10000:" + range.toString()))
                        .then(range -> range.setMin(1))
                        .then(range -> System.out.println(range.toString()))
                        .map(range -> RangeInt.builder().min(range.getMin()).max(1).build())
                        .get()
                        .toString()
        );
    }
}