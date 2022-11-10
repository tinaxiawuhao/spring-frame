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
public final class RangeLong implements Num.IRange<Long> {
    private Long min;
    private Long max;

    /**
     * 构造数字区间
     *
     * @param min long 获取最小值
     * @param max long 获取最大值
     * @return {@link RangeLong}
     */
    public static RangeLong of(final long min, final long max) {
        if (max <= 0) {
            log.warn("参数【max】<=0");
        }
        return new RangeLong(min, max);
    }

    /**
     * 构造数字区间
     *
     * @param values {@link Long[]} 从数组中获取最小值和最大值区间
     * @return {@link RangeLong}
     */
    public static RangeLong of(final Long[] values) {
        Arrays.sort(values);
        return new RangeLong(values[0], values[values.length - 1]);
    }

    /**
     * 构造数字区间
     *
     * @param values {@link List}{@link List<Long>} 从集合中获取最小值和最大值区间
     * @return {@link RangeLong}
     */
    public static RangeLong of(final List<Long> values) {
        values.sort(Comparator.naturalOrder());
        return new RangeLong(values.get(0), values.get(values.size() - 1));
    }

    /**
     * 遍历区间，包含 min 和 max 值
     *
     * @param action {@link Consumer}{@link Consumer<Long:value>}
     */
    public void forEach(final Consumer<Long> action) {
        Objects.requireNonNull(action, "参数【action】是必须的");
        for (Long i = min; i <= max; i++) {
            action.accept(i);
        }
    }

    /**
     * 转换区间，包含 min 和 max 值
     *
     * @param mapper {@link Function}{@link Function<Long:value, R:返回数据类型>}
     * @param <R>    返回数据类型
     * @return {@link Stream<R>}
     */
    public <R> Stream<R> map(final Function<Long, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "参数【mapper】是必须的");
        return Stream.iterate(min, n -> n + 1)
                .limit(max - min + 1)
                .map(mapper);
    }
}