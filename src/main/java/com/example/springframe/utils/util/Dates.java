package com.example.springframe.utils.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.example.springframe.utils.enums.Week;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.springframe.utils.util.Dates.Pattern.*;

/**
 * 日期处理类
 */
@Slf4j
public final class Dates {
    private static final ZoneId ZONE_ID = ZoneId.of("GMT+8");

    /**
     * 枚举：定义日期格式
     */
    public enum Pattern {
//        yyyy("yyyy"),
//        MM("MM"),
//        dd("dd"),
//        HH("HH"),
//        mm("mm"),
//        ss("ss"),

        yyyy_MM_dd_HH_mm_ss_SSS("yyyy-MM-dd HH:mm:ss.SSS"),
        yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss"),
        yyyy_MM_dd("yyyy-MM-dd"),
        yyyy_MM("yyyy-MM"),
        yy_MM_dd("yy-MM-dd"),
        HH_mm_ss("HH:mm:ss"),
        HH_mm("HH:mm"),

        yyyyMMddHHmmssSSS("yyyyMMddHHmmssSSS"),
        yyyyMMddHHmmss("yyyyMMddHHmmss"),
        yyyyMMdd("yyyyMMdd"),
        yyyyMM("yyyyMM"),
        HHmmssSSS("HHmmssSSS"),
        HHmmss("HHmmss"),

        zh_yyyy_MM_dd_HH_mm_ss("yyyy年MM月dd日 HH时mm分"),
        zh_yyyy_MM_dd("yyyy年MM月dd日"),
        zh_yyyy_MM("yyyy年MM月"),
        ;
        /**
         * 枚举属性说明
         */
        private final String comment;
        private final DateTimeFormatter formatter;
        private final SimpleDateFormat format;

        public String value() {
            return this.comment;
        }

        Pattern(final String comment) {
            this.comment = comment;
            this.formatter = DateTimeFormatter.ofPattern(comment);
            this.format = new SimpleDateFormat(comment);
        }

        /**
         * 获取日期字符
         *
         * @return {@link String}
         */
        public String now() {
            return LocalDateTime.now(ZONE_ID).format(formatter);
        }

        /**
         * 转换为日期操作对象
         *
         * @param value String 日期
         * @return {@link Dates}
         */
        public Dates parse(final String value) {
            try {
                return Dates.of(format.parse(value));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            // TODO java 8 新的日期操作类，格式化容错率太低了
//            try {
//                return new Dates(LocalDateTime.parse(value, formatter));
//            } catch (DateTimeException e) {
//                try {
//                    return new Dates(LocalDate.parse(value, formatter));
//                } catch (DateTimeException e1) {
//                    return new Dates(LocalTime.parse(value, formatter));
//                }
//            }
//            return new Dates(LocalDateTime.parse(value, formatter));
        }

        /**
         * 格式化日期
         *
         * @param value {@link LocalDateTime}
         * @return {@link String}
         */
        public String format(final LocalDateTime value) {
            return value.format(formatter);
        }

        /**
         * 格式化日期
         *
         * @param value {@link Date} or {@link Timestamp}
         * @return {@link String}
         */
        public String format(final Date value) {
            return format.format(value);
        }
    }

    /**
     * 定义日期区间
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    @Accessors(chain = true)
    public static class Range {
        /**
         * 开始
         */
        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        private Timestamp begin;
        /**
         * 结束
         */
        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        private Timestamp end;

        /**
         * 以当天时间初始化区间 yyyy-MM-dd 00:00:00.000 - yyyy-MM-dd 23:59:59.999
         *
         * @return {@link Range}
         */
        public static Range today() {
            final Dates now = Dates.now();
            return Range.builder()
                    .begin(now.beginTimeOfDay().timestamp())
                    .end(now.endTimeOfDay().timestamp())
                    .build();
        }

        /**
         * 以当月时间初始化区间 yyyy-MM-01 00:00:00.00 - yyyy-MM-(28|30|31) 23:59:59.999
         *
         * @return {@link Range}
         */
        public static Range month() {
            final Dates now = Dates.now();
            return Range.builder()
                    .begin(now.firstDayOfMonth().beginTimeOfDay().timestamp())
                    .end(now.lastDayOfMonth().endTimeOfDay().timestamp())
                    .build();
        }

        /**
         * 遍历选定区间：按天
         *
         * @param action {@link BiConsumer}{@link BiConsumer<Timestamp:start, Timestamp:end> } <br>start=2018-01-01 00:00:00.000 <br>end=2018-01-01 23:59:59.999
         */
        public void forEach(BiConsumer<Timestamp, Timestamp> action) {
            Objects.requireNonNull(action, "参数【action】是必须的");
            final Dates beginDate = Dates.of(begin);
            final Dates endDate = Dates.of(end).endTimeOfDay();
            do {
                action.accept(beginDate.beginTimeOfDay().timestamp(), beginDate.endTimeOfDay().timestamp());
                beginDate.addDay(1);
            } while (beginDate.le(endDate));
        }

        /**
         * 遍历选定区间：按月
         *
         * @param action {@link BiConsumer}{@link BiConsumer<Timestamp:start, Timestamp:end> } <br>start=2018-01-01 00:00:00.000 <br>end=2018-01-31 23:59:59.999
         */
        public void forEachMonth(BiConsumer<Timestamp, Timestamp> action) {
            Objects.requireNonNull(action, "参数【action】是必须的");
            final Dates beginDate = Dates.of(begin);
            final Dates endDate = Dates.of(end).lastDayOfMonth();
            do {
                action.accept(beginDate.firstDayOfMonth().timestamp(), beginDate.lastDayOfMonth().timestamp());
                beginDate.addMonth(1);
            } while (beginDate.le(endDate));
        }

        /**
         * 保留年月日，将开始时间设置为 00:00:00.000
         * 保留年月日，将结束时间设置为 23:59:59.999
         *
         * @return {@link Range}
         */
        public Range rebuild() {
            if (Objects.nonNull(begin)) {
                begin = Dates.of(begin).beginTimeOfDay().timestamp();
            }
            if (Objects.nonNull(end)) {
                end = Dates.of(end).endTimeOfDay().timestamp();
            }
            return this;
        }

        /**
         * 校验开始时间必须小于结束时间
         *
         * @return {@link Boolean}
         */
        public boolean check() {
            try {
                check(null);
                return true;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return false;
            }
        }

        /**
         * 校验开始时间必须小于结束时间
         *
         * @return {@link Range}
         */
        public Range check(final Supplier<? extends RuntimeException> exSupplier) {
            if (Objects.isNull(begin)) {
                if (Objects.isNull(exSupplier))
                    throw new NullPointerException("begin is null");
                else
                    throw exSupplier.get();
            }
            if (Objects.nonNull(end)) {
                if (Dates.of(begin).gt(Dates.of(end))) {
                    if (Objects.isNull(exSupplier))
                        throw new RuntimeException("begin > end");
                    else
                        throw exSupplier.get();
                }
            } else {
                end = Dates.now().timestamp();
            }
            return this;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }
    }

    /**
     * 以当前时间 构造时间处理对象
     *
     * @return {@link Dates}
     */
    public static Dates now() {
        return new Dates();
    }

    /**
     * 构造时间处理对象：指定时间
     *
     * @param value {@link LocalDateTime}
     * @return {@link Dates}
     */
    public static Dates of(final LocalDateTime value) {
        Objects.requireNonNull(value, "参数【value】是必须的");
        return new Dates(value);
    }

    /**
     * 构造时间处理对象：指定时间
     *
     * @param value {@link LocalDate}
     * @return {@link Dates}
     */
    public static Dates of(final LocalDate value) {
        Objects.requireNonNull(value, "参数【value】是必须的");
        return new Dates(value.atStartOfDay());
    }

    /**
     * 构造时间处理对象：指定时间
     *
     * @param value {@link LocalTime}
     * @return {@link Dates}
     */
    public static Dates of(final LocalTime value) {
        Objects.requireNonNull(value, "参数【value】是必须的");
        return new Dates(value.atDate(LocalDate.now(ZONE_ID)));
    }

    /**
     * 构造时间处理对象：指定时间
     *
     * @param value {@link Timestamp}
     * @return {@link Dates}
     */
    public static Dates of(final Timestamp value) {
        Objects.requireNonNull(value, "参数【value】是必须的");
        return new Dates(value.toLocalDateTime());
    }

    /**
     * 构造时间处理对象：指定时间
     *
     * @param value {@link Date}
     * @return {@link Dates}
     */
    public static Dates of(final Date value) {
        Objects.requireNonNull(value, "参数【value】是必须的");
        return new Dates(new Timestamp(value.getTime()).toLocalDateTime());
    }

    /**
     * 构造时间处理对象：指定时间
     *
     * @param value long
     * @return {@link Dates}
     */
    public static Dates of(final long value) {
        return new Dates(new Timestamp(value).toLocalDateTime());
    }

    /**
     * 构造时间处理对象：指定时间
     *
     * @param value   String 日期字符串
     * @param pattern {@link Pattern} 日期格式
     * @return {@link Dates}
     */
    public static Dates of(final String value, final Pattern pattern) {
        Objects.requireNonNull(value, "参数【value】是必须的");
        Objects.requireNonNull(pattern, "参数【pattern】是必须的");
        try {
            return of(new SimpleDateFormat(pattern.value()).parse(value).getTime());
//            return pattern.parse(value); // LocalDateTime 容错性低，格式必须完全匹配才能转换
        } catch (ParseException e) {
            throw new IllegalArgumentException(String.format("日期转换失败，value:%s > pattern:%s", value, pattern));
        }
    }

    /**
     * 构造时间处理对象
     *
     * @param value String 日期字符串
     * @return {@link Dates}
     */
    public static Dates parse(String value) {
        Objects.requireNonNull(value, "参数【value】是必须的");
        Objects.requireNonNull("".equals(value.trim()) ? null : "", "参数【value】是必须的");
        try {
            value = value.trim().replaceAll("[^\\d]", "");
            String pattern;
            switch (value.length()) {
                case 17:
                    pattern = Integer.parseInt(value.substring(4, 6)) > 12 ? "yyyyddMMHHmmssSSS" : "yyyyMMddHHmmssSSS";
                    break;
                case 14:
                    pattern = Integer.parseInt(value.substring(4, 6)) > 12 ? "yyyyddMMHHmmss" : "yyyyMMddHHmmss";
                    break;
                case 9:
                    pattern = "HHmmssSSS";
                    break;
                case 8:
                    pattern = Integer.parseInt(value.substring(4, 6)) > 12 ? "yyyyddMM" : "yyyyMMdd";
                    break;
                case 6:
                    pattern = "HHmmss";
                    break;
                default:
                    throw new IllegalArgumentException("未识别的日期格式:".concat(value));
            }
            return of(new SimpleDateFormat(pattern).parse(value).getTime());
//            return new Dates(LocalDateTime.parse(value, DateTimeFormatter.ofPattern(pattern)));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(String.format("日期转换失败，value:%s", value));
        }
    }


    private Dates() {
        this.obj = LocalDateTime.now();
    }

    private Dates(final LocalTime value) {
        this(value.atDate(LocalDate.now()));
    }

    private Dates(final LocalDate value) {
        this(value.atStartOfDay());
    }

    private Dates(final LocalDateTime value) {
        this.obj = value;
    }

    private LocalDateTime obj;

    /**
     * @return {@link LocalDateTime}
     */
    public LocalDateTime get() {
        return obj;
    }

    /**
     * 转换为long
     *
     * @return long
     */
    public long getTimeMillis() {
        return Timestamp.valueOf(obj).getTime();
    }

    /**
     * 转换为 Timestamp
     *
     * @return {@link Timestamp}
     */
    public Timestamp timestamp() {
        return Timestamp.valueOf(obj);
    }

    /**
     * 转换为 Date
     *
     * @return {@link Date}
     */
    public Date date() {
        return Timestamp.valueOf(obj);
    }

    /**
     * 格式化为字符串, 必须指定格式
     *
     * @param pattern {@link Pattern}
     * @return String
     */
    public String format(final Pattern pattern) {
        Objects.requireNonNull(pattern, "参数【pattern】是必须的");
        return pattern.format(obj);
    }

    /**
     * 格式化为字符串, 必须指定格式
     *
     * @param pattern {@link Pattern}
     * @return String
     */
    public String format(final String pattern) {
        Objects.requireNonNull(pattern, "参数【pattern】是必须的");
        return obj.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化为字符串, 示例：yyyy-MM-dd
     *
     * @return String
     */
    public String formatDate() {
        return yyyy_MM_dd.format(obj);
    }

    /**
     * 格式化为字符串, 示例：HH:mm:ss
     *
     * @return String
     */
    public String formatTime() {
        return HH_mm_ss.format(obj);
    }

    /**
     * 格式化为字符串, 示例：yyyy-MM-dd HH:mm:ss
     *
     * @return String
     */
    public String formatDateTime() {
        return yyyy_MM_dd_HH_mm_ss.format(obj);
    }

    /**
     * 获取：年
     *
     * @return int
     */
    public int year() {
        return obj.getYear();
    }

    /**
     * 获取：月
     *
     * @return int
     */
    public int month() {
        return obj.getMonthValue();
    }

    /**
     * 获取：日
     *
     * @return int
     */
    public int day() {
        return obj.getDayOfMonth();
    }

    /**
     * 获取：星期
     *
     * @return {@link Week}
     */
    public Week week() {
        return Week.values()[obj.getDayOfWeek().ordinal()];
    }

    /**
     * 获取：时
     *
     * @return int
     */
    public int h() {
        return obj.getHour();
    }

    /**
     * 获取：分
     *
     * @return int
     */
    public int m() {
        return obj.getMinute();
    }

    /**
     * 获取：秒
     *
     * @return int
     */
    public int s() {
        return obj.getSecond();
    }

    /**
     * 获取：微秒
     *
     * @return int
     */
    public int ns() {
        return obj.getNano();
    }

    /**
     * 指定：年
     *
     * @param value int
     * @return {@link Dates}
     */
    public Dates year(int value) {
        obj = obj.withYear(value);
        return this;
    }

    /**
     * 指定：月
     *
     * @param value int
     * @return {@link Dates}
     */
    public Dates month(int value) {
        obj = obj.withMonth(Math.min(12, value));
        return this;
    }

    /**
     * 指定：日
     *
     * @param value int
     * @return {@link Dates}
     */
    public Dates day(int value) {
        obj = obj.withDayOfMonth(Math.min(value, obj.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth()));
        return this;
    }

    /**
     * 指定：时
     *
     * @param value int
     * @return {@link Dates}
     */
    public Dates h(int value) {
        obj = obj.withHour(Math.min(23, value));
        return this;
    }

    /**
     * 指定：分
     *
     * @param value int
     * @return {@link Dates}
     */
    public Dates m(int value) {
        obj = obj.withMinute(Math.min(59, value));
        return this;
    }

    /**
     * 指定：秒
     *
     * @param value int
     * @return {@link Dates}
     */
    public Dates s(int value) {
        obj = obj.withSecond(Math.min(59, value));
        return this;
    }

    /**
     * 指定：纳秒
     *
     * @param value int
     * @return {@link Dates}
     */
    public Dates ns(int value) {
        obj = obj.withNano(value);
        return this;
    }

    /**
     * 年【增加|减少】
     *
     * @param value int 正数为增加，负数表示减少
     * @return {@link Dates}
     */
    public Dates addYear(int value) {
        obj = obj.plusYears(value);
        return this;
    }

    /**
     * 月【增加|减少】
     *
     * @param value int 正数为增加，负数表示减少
     * @return {@link Dates}
     */
    public Dates addMonth(int value) {
        obj = obj.plusMonths(value);
        return this;
    }

    /**
     * 日【增加|减少】
     *
     * @param value int 正数为增加，负数表示减少
     * @return {@link Dates}
     */
    public Dates addDay(int value) {
        obj = obj.plusDays(value);
        return this;
    }

    /**
     * 星期【增加|减少】
     *
     * @param value int 正数为增加，负数表示减少
     * @return {@link Dates}
     */
    public Dates addWeek(int value) {
        obj = obj.plusWeeks(value);
        return this;
    }

    /**
     * 时【增加|减少】
     *
     * @param value int 正数为增加，负数表示减少
     * @return {@link Dates}
     */
    public Dates addHour(int value) {
        obj = obj.plusHours(value);
        return this;
    }

    /**
     * 分【增加|减少】
     *
     * @param value int 正数为增加，负数表示减少
     * @return {@link Dates}
     */
    public Dates addMinute(int value) {
        obj = obj.plusMinutes(value);
        return this;
    }

    /**
     * 秒【增加|减少】
     *
     * @param value int 正数为增加，负数表示减少
     * @return {@link Dates}
     */
    public Dates addSecond(int value) {
        obj = obj.plusSeconds(value);
        return this;
    }

    /**
     * 计算并设置为上周一的日期
     *
     * @return {@link Dates}
     */
    public Dates prevMonday() {
        obj = obj.minusWeeks(1) // minusWeeks(1) ；即上周
                .minusDays(obj.getDayOfWeek().ordinal()) // 设置为周一
        ;
//        obj = obj.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); // 上一个周一，当前为周一时有bug
        return this;
    }

    /**
     * 计算并设置为下周一的日期
     *
     * @return {@link Dates}
     */
    public Dates nextMonday() {
        obj = obj.plusWeeks(1) // plusWeeks(1) ；即下周
                .minusDays(obj.getDayOfWeek().ordinal()) // 设置为周一
        ;
//        obj = obj.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));  // 下一个周一，当前为周一时有bug
        return this;
    }

    /**
     * 当天的开始时间
     * 设置为当天 0 时 0 分 0 秒 0 纳秒
     *
     * @return {@link Dates}
     */
    public Dates beginTimeOfDay() {
        h(0).m(0).s(0).ns(0);
        return this;
    }

    /**
     * 当天的结束时间
     * 设置为当天 23 时 59 分 59 秒 999 纳秒
     *
     * @return {@link Dates}
     */
    public Dates endTimeOfDay() {
        h(23).m(59).s(59).ns(999999999);
        return this;
    }

    /**
     * 设置为当月第一天
     *
     * @return {@link Dates}
     */
    public Dates firstDayOfMonth() {
        day(1);
        return this;
    }

    /**
     * 设置为下个月1号
     *
     * @return {@link Dates}
     */
    public Dates firstDayOfNextMonth() {
        obj = obj.with(TemporalAdjusters.firstDayOfNextMonth());
        return this;
    }

    /**
     * 设置为当月最后一天
     *
     * @return {@link Dates}
     */
    public Dates lastDayOfMonth() {
        obj = obj.with(TemporalAdjusters.lastDayOfMonth());
        return this;
    }

    /**
     * 比对两个日期
     * <pre>
     *     小于 destDate 返回 -1；左小，右大；2018-01-01 | 2018-01-02=-1
     *     大于 destDate 返回 1； 右大，左小；2018-01-02 | 2018-01-01= 1
     *     相等返回 0
     *
     * @param destDate Dates
     * @return int
     */
    public int compare(Dates destDate) {
        return this.obj.compareTo(destDate.get());
    }

    /**
     * 比对两个日期，左边 > 右边
     *
     * @param destDate Dates
     * @return boolean
     */
    public boolean gt(Dates destDate) {
        return 1 == compare(destDate);
    }

    /**
     * 比对两个日期，左边 < 右边
     *
     * @param destDate Dates
     * @return boolean
     */
    public boolean lt(Dates destDate) {
        return -1 == compare(destDate);
    }

    /**
     * 比对两个日期，左边 >= 右边
     *
     * @param destDate Dates
     * @return boolean
     */
    public boolean ge(Dates destDate) {
        return -1 != compare(destDate);
    }

    /**
     * 比对两个日期，左边 <= 右边
     *
     * @param destDate Dates
     * @return boolean
     */
    public boolean le(Dates destDate) {
        return 1 != compare(destDate);
    }

    /**
     * 比对两个日期，左边 == 右边
     *
     * @param destDate Dates
     * @return boolean
     */
    public boolean eq(Dates destDate) {
        return 0 == compare(destDate);
    }

    /**
     * 获取时间间隔
     *
     * @return {@link Duration}
     */
    public Duration getTimeConsuming() {
// import java.time.Duration;
// import java.time.Period;
        return Duration.between(obj, LocalDateTime.now());
    }

    /**
     * 获取时间间隔，m分s秒
     *
     * @return String
     */
    public String getTimeConsumingText() {
        final Duration duration = getTimeConsuming();
        return (Math.abs(duration.toHours()) > 0 ? String.format("%d时", duration.toHours()) : "")
                .concat(Math.abs(duration.toMinutes()) > 0 ? String.format("%d分", duration.toMinutes() % 60) : "")
                .concat(String.format("%d秒", (duration.toMillis() / 1000) % 60));
    }

    /**
     * 获取两个日期之间相差的天数
     * 目标日期destDate - 当前dates
     *
     * @param destDate Dates 目标日期
     * @return int 相差天数
     */
    public int getDifferDay(Dates destDate) {
        return (int) Duration.between(obj, destDate.get()).toDays();
    }

    /**
     * 获取本年按季度划分的时间区间集合
     * 数据示例：[{"begin":"2017-01-01 00:00:00","end":"2017-03-31 23:59:59"}, {"begin":"2017-04-01 00:00:00","end":"2017-06-30 23:59:59"}, {"begin":"2017-07-01 00:00:00","end":"2017-09-30 23:59:59"}, {"begin":"2017-10-01 00:00:00","end":"2017-12-31 23:59:59"}]
     *
     * @return {@link List}{@link List<Range>}
     */
    public List<Range> getRangeOfQuarter() {
        return Stream.of(
                new int[]{1, 3},
                new int[]{4, 6},
                new int[]{7, 9},
                new int[]{10, 12}
        )
                .map(arr -> Range.builder()
                        .begin(month(arr[0]).firstDayOfMonth().beginTimeOfDay().timestamp())
                        .end(month(arr[1]).lastDayOfMonth().endTimeOfDay().timestamp())
                        .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * 获取当月时间区间
     * 数据示例：{"begin":"2017-01-01 00:00:00","end":"2017-1-31 23:59:59"}
     *
     * @return {@link Range}
     */
    public Range getRangeOfMonth() {
        return Range.builder()
                .begin(firstDayOfMonth().beginTimeOfDay().timestamp())
                .end(lastDayOfMonth().endTimeOfDay().timestamp())
                .build();
    }

    /**
     * 获取当年时间区间
     * 数据示例：{"begin":"2017-01-01 00:00:00","end":"2017-12-31 23:59:59"}
     *
     * @return {@link Range}
     */
    public Range getRangeOfYear() {
        return Range.builder()
                .begin(month(1).firstDayOfMonth().beginTimeOfDay().timestamp())
                .end(month(12).lastDayOfMonth().endTimeOfDay().timestamp())
                .build();
    }

    @Override
    public String toString() {
        return formatDateTime();
    }

    public static void main(String[] args) {
        log.info(Dates.now().format(Pattern.yyyy_MM));
        log.info(Dates.now().formatTime());
        log.info(Dates.now().formatDate());
        log.info(Dates.now().formatDateTime());
        log.info(Dates.of(new Date()).addYear(1).format("yyyy-MM-dd"));
        log.info(Dates.of(new Timestamp(System.currentTimeMillis())).formatDateTime());
        log.info("{}", Dates.now().getRangeOfMonth());
        log.info("{}", Dates.now().getRangeOfYear());
        log.info("{}", Dates.now().getRangeOfQuarter());
        log.info("{}", JSON.parseObject("{\"begin\":\"2017-11-01\",\"end\":\"2017-11-30\"}", Range.class).rebuild());
        Dates dates = Dates.now();
        log.info(dates.formatDateTime());
        log.info("{}", dates.get());
        log.info("{}", Dates.of(dates.formatDateTime(), yyyy_MM_dd_HH_mm_ss).get());
        log.info(Dates.of("2017-01-17 08:56:03 +0000", yyyy_MM_dd).formatDate());

        log.info("左 > 右 true：{}", Dates.now().addDay(1).gt(Dates.now()));
        log.info("左 > 右 false：{}", Dates.now().gt(Dates.now().addDay(1)));
        log.info("左 < 右 true：{}", Dates.now().lt(Dates.now().addDay(1)));
        log.info("左 < 右 false：{}", Dates.now().addDay(1).lt(Dates.now()));
        log.info("左 = 右 true：{}", Dates.now().beginTimeOfDay().eq(Dates.now().beginTimeOfDay()));
        log.info("左 = 右 false：{}", Dates.now().addDay(1).beginTimeOfDay().eq(Dates.now().beginTimeOfDay()));
        log.info("左 >= 右 true：{}", Dates.now().beginTimeOfDay().ge(Dates.now().beginTimeOfDay()));
        log.info("左 >= 右 true：{}", Dates.now().addDay(1).beginTimeOfDay().ge(Dates.now().beginTimeOfDay()));
        log.info("左 >= 右 false：{}", Dates.now().beginTimeOfDay().ge(Dates.now().addDay(1).beginTimeOfDay()));
        log.info("左 <= 右 true：{}", Dates.now().beginTimeOfDay().le(Dates.now().beginTimeOfDay()));
        log.info("左 <= 右 true：{}", Dates.now().beginTimeOfDay().le(Dates.now().addDay(1).beginTimeOfDay()));
        log.info("左 <= 右 false：{}", Dates.now().addDay(1).beginTimeOfDay().le(Dates.now().beginTimeOfDay()));

        try {
            log.info("{}", LocalDateTime.parse("2018-12-12", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (DateTimeException e) {
            try {
                log.info("{}", LocalDate.parse("2018-12-12", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            } catch (DateTimeException e1) {
                log.info("{}", LocalTime.parse("2018-12-12", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        }
        log.info("{}", LocalDateTime.parse("2018-12-12 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        log.info("{} => {}", yyyy_MM_dd_HH_mm_ss_SSS.comment, yyyy_MM_dd_HH_mm_ss_SSS.parse("2018-12-12 00:00:00.000"));
        log.info("{} => {}", yyyy_MM_dd_HH_mm_ss.comment, yyyy_MM_dd_HH_mm_ss.parse("2018-12-12 00:00:00.000"));
        log.info("{} => {}", yyyy_MM_dd.comment, yyyy_MM_dd.parse("2018-12-12"));
        log.info("{} => {}", yyyy_MM.comment, yyyy_MM.parse("2018-12"));
    }

}
