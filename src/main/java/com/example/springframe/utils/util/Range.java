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

/**
 * 数字区间操作类
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JSONType(orders = {"min", "max"})
@Slf4j
public final class Range<T extends Number> implements Num.IRange<T> {
    private T min;
    private T max;

    public static <T extends Number> Range<T> of(final T min, final T max) {
        if (max.doubleValue() <= 0) {
            log.warn("参数【max】<=0");
        }
        final Range<T> range = new Range<>();
        range.setMin(min);
        range.setMax(max);
        return range;
    }

    /**
     * 构造数字区间
     *
     * @param values {@link Number}[] 从数组中获取最小值和最大值区间
     * @return {@link Range} {@link Range<T extends Number>}
     */
    public static <T extends Number> Range of(final T[] values) {
        Arrays.sort(values);
        return new Range<T>(values[0], values[values.length - 1]);
    }

    /**
     * 构造数字区间
     *
     * @param values {@link List}{@link List<T extends Number>} 从数组中获取最小值和最大值区间
     * @return {@link Range}{@link Range<T extends Number>}
     */
    public static <T extends Number> Range<T> of(final List<T> values) {
        values.sort(Comparator.comparingDouble(Number::doubleValue));
        return new Range<T>(values.get(0), values.get(values.size() - 1));
    }
}