package com.example.springframe.utils.util;


import com.example.springframe.utils.ICall;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 补充 java8 下 {@link Optional} 对 boolean 判断不友好的地方
 */
public final class Bool<T> {
    private final boolean booleanValue;
    private final Optional<T> optional;

    private Bool(final Boolean booleanValue) {
        this.booleanValue = Objects.nonNull(booleanValue) && booleanValue;
        this.optional = (Optional<T>) Optional.of(booleanValue);
    }

    private Bool(final Optional<T> optional) {
        this.booleanValue = Objects.requireNonNull(optional).isPresent();
        this.optional = optional;
    }

    public static Bool<Boolean> of(final Boolean booleanValue) {
        return new Bool<>(booleanValue);
    }

    public static <T> Bool<T> of(final Optional<T> optional) {
        return new Bool<>(optional);
    }

    public static <T> Bool<T> of(final T value) {
        return new Bool<>(Optional.ofNullable(value));
    }

//    public static Bool ofNonEmpty(final Optional<String> optional) {
//        return Bool.of(Objects.requireNonNull(optional).isPresent() && !Objects.equals("", optional.get()));
//    }

    /**
     * 当值为 true 时，执行 {@link ICall#call} 方法
     *
     * @param call {@link ICall#call}
     * @return {@link Bool}
     */
    public Bool<T> hasTrue(final ICall call) {
        if (booleanValue) call.call();
        return this;
    }

    /**
     * 当值为 true 时，执行 {@link Consumer#accept(Object)} 方法
     *
     * @param consumer {@link Consumer#accept(Object)}
     * @return {@link Bool}
     */
    public Bool<T> hasTrue(final Consumer<T> consumer) {
        if (booleanValue) consumer.accept(optional.get());
        return this;
    }

    /**
     * 当值为 false 时，执行 {@link ICall#call} 方法
     *
     * @param call {@link ICall#call}
     * @return {@link Bool}
     */
    public Bool<T> hasFalse(final ICall call) {
        if (!booleanValue) call.call();
        return this;
    }

    /**
     * 获取 {@link Optional} 值，可能为 null
     *
     * @return T
     */
    public T optionalGetOrNull() {
        return optional.orElse(null);
    }

    /**
     * 获取 {@link Optional}
     *
     * @return {@link Optional<T>}
     */
    public Optional<T> optional() {
        return optional;
    }

    public static void main(String[] args) {
        final AtomicInteger counter = new AtomicInteger(1);
        Bool.of(true)
                .hasTrue((v) -> System.out.println(counter.getAndIncrement() + " => TRUE"))
                .hasFalse(() -> System.out.println(counter.getAndIncrement() + " => FALSE"));
        Bool.of(false)
                .hasTrue((v) -> System.out.println(counter.getAndIncrement() + " => TRUE"))
                .hasFalse(() -> System.out.println(counter.getAndIncrement() + " => FALSE"));
        Bool.of(Optional.empty())
                .hasTrue((v) -> System.out.println(counter.getAndIncrement() + " => nonEmpty"))
                .hasFalse(() -> System.out.println(counter.getAndIncrement() + " => empty"));
        Bool.of(Optional.of("nonEmpty"))
                .hasTrue((v) -> System.out.println(counter.getAndIncrement() + " => nonEmpty"))
                .hasFalse(() -> System.out.println(counter.getAndIncrement() + " => empty"));
        Bool.of(Optional.of("nonEmpty")).hasTrue((Consumer<String>) System.out::println);
        Bool.of(Optional.of(new String[]{"1", "a"})).hasTrue((value) -> System.out.println(value.length));
        Bool.of(Optional.of(new Object[]{1, "a"})).hasTrue((value) -> System.out.println(value.length));
        Bool.of(Optional.of(Arrays.asList(1, "a"))).hasTrue((value) -> System.out.println(value.size()));
    }
}
