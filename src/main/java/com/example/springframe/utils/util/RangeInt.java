package com.example.springframe.utils.util;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 数字区间操作类
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JSONType(orders = {"min", "max"})
@Slf4j
public final class RangeInt implements Num.IRange<Integer> {
    private Integer min;
    private Integer max;

    public RangeInt min(final Integer min) {
        this.min = min;
        return this;
    }

    public RangeInt max(final Integer max) {
        this.max = max;
        return this;
    }

    /**
     * 构造数字区间
     *
     * @param min int 获取最小值
     * @param max int 获取最大值
     * @return {@link RangeInt}
     */
    public static RangeInt of(final int min, final int max) {
        if (max <= 0) {
            log.warn("参数【max】<=0");
        }
        return new RangeInt(min, max);
    }

    /**
     * 构造数字区间
     *
     * @param values {@link Integer[]} 从数组中获取最小值和最大值区间
     * @return {@link RangeInt}
     */
    public static RangeInt of(final Integer[] values) {
        Arrays.sort(values);
        return new RangeInt(values[0], values[values.length - 1]);
    }

    /**
     * 构造数字区间
     *
     * @param values {@link List}{@link List<Integer>} 从数组中获取最小值和最大值区间
     * @return {@link RangeInt}
     */
    public static RangeInt of(final List<Integer> values) {
        values.sort(Comparator.naturalOrder());
        return new RangeInt(values.get(0), values.get(values.size() - 1));
    }

    /**
     * 遍历区间，包含 min 和 max 值
     *
     * @param action {@link Consumer <Integer:value>}
     */
    public void forEach(final Consumer<Integer> action) {
        Objects.requireNonNull(action, "参数【action】是必须的");
        for (int i = min; i <= max; i++) {
            action.accept(i);
        }
    }

    /**
     * 转换区间，包含 min 和 max 值
     *
     * @param mapper {@link Function}{@link Function <Integer:value, R:返回数据类型>}
     * @param <R>    返回数据类型
     * @return {@link Stream<R>}
     */
    public <R> Stream<R> map(final Function<Integer, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "参数【mapper】是必须的");
        return Stream.iterate(min, n -> n + 1)
                .limit(max - min + 1)
                .map(mapper);
    }
}