package com.example.springframe.utils.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 对 {@link Optional} 友好，建议使用 {@link Optional}，因为该类的部分功能已经在 jdk11 后实现
 */
@Slf4j
public final class Op<T> {

    private Optional<T> op = Optional.empty();

    private Op() {
    }

    private Op(final Optional optional) {
        this.op = optional;
    }

    public static <T> Op<T> of(final Optional<T> optional) {
        return new Op<T>(optional);
    }

    public static <T> Op<T> of(final T value) {
        return new Op<T>(Optional.of(value));
    }

    public static <T> Op<T> ofNullable(T value) {
        return new Op<T>(Optional.ofNullable(value));
    }

    /**
     * isPresent() == true
     *
     * @param consumer {@link Consumer}{@link Consumer<Op>}
     * @return {@link Op}
     */
    public Op<T> ifPresent(final Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
        op.ifPresent(consumer::accept);
        return this;
    }

    /**
     * isPresent() == false
     *
     * @param consumer {@link Consumer}{@link Consumer<Op>}
     * @return {@link Op}
     */
    public Op<T> elsePresent(final Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
        if (!op.isPresent()) consumer.accept(null);
        return this;
    }

    public Op<T> orElseOf(final Supplier<Optional<T>> other) {
        Objects.requireNonNull(other);
        if (!op.isPresent()) {
            return Op.ofNullable(other.get().orElse(null));
        }
        return this;
    }

    public Optional<T> optional() {
        return op;
    }

    /**
     * Returns the hash code value of the present value, if any, or 0 (zero) if
     * no value is present.
     *
     * @return hash code value of the present value or 0 if no value is present
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(op);
    }

    /**
     * Returns a non-empty string representation of this Op suitable for
     * debugging. The exact presentation format is unspecified and may vary
     * between implementations and versions.
     *
     * @return the string representation of this instance
     * @implSpec If a value is present the result must include its string
     * representation in the result. Empty and present Ops must be
     * unambiguously differentiable.
     */
    @Override
    public String toString() {
        return op.toString();
    }

    public static void main(String[] args) {
        Op.ofNullable(null)
                .ifPresent(o -> log.debug("true:{}", JSON.toJSONString(o)))
                .elsePresent(o -> log.debug("false:{}", JSON.toJSONString(o)))
        ;
        Op.of(Maps.bySS("language", "java"))
                .ifPresent(o -> log.debug("true:{}", JSON.toJSONString(o)))
                .elsePresent(o -> log.debug("false:{}", JSON.toJSONString(o)))
        ;
        Op.ofNullable(null)
                .orElseOf(() -> Optional.of(Maps.bySS("language", "java")))
                .orElseOf(() -> Optional.of(Maps.bySS("language", "js")))
                .ifPresent(o -> log.debug("true:{}", JSON.toJSONString(o)))
                .elsePresent(o -> log.debug("false:{}", JSON.toJSONString(o)))
        ;
        Op.of(Optional.of(Maps.bySS("language", "java")))
                .ifPresent(o -> log.debug("true:{}", JSON.toJSONString(o)))
                .elsePresent(o -> log.debug("false:{}", JSON.toJSONString(o)))
        ;
        Op.of(Optional.empty())
                .orElseOf(() -> Optional.of(Maps.bySS("language", "js")))
                .ifPresent(o -> log.debug("true:{}", JSON.toJSONString(o)))
                .elsePresent(o -> log.debug("false:{}", JSON.toJSONString(o)))
        ;
    }
}
