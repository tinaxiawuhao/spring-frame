package com.example.springframe.utils.enums;

import com.example.springframe.utils.util.Dates;
import com.example.springframe.utils.util.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

/**
 * <pre>
 * 枚举：星期；
 * 与新的日期操作类返回的 {@link java.time.DayOfWeek} 相似
 * 注意与原来的 {@link java.util.Calendar#DAY_OF_WEEK} 的差别
 * {@link java.util.Calendar#DAY_OF_WEEK} 获取周日为数字 0，即：{"0":"日","1":"一","2":"二","3":"三","4":"四","5":"五","6":"六"}
 * {@link java.time.DayOfWeek#SUNDAY}{@link java.time.DayOfWeek#SUNDAY#ordinal()}  获取周日为数字为 6 ，即：{"0":"一","1":"二","2":"三","3":"四","4":"五","5":"六","6":"日"}
 * {@link java.time.DayOfWeek#SUNDAY}{@link java.time.DayOfWeek#SUNDAY#getValue()} 获取周日为数字为 7 ，即：{"1":"一","2":"二","3":"三","4":"四","5":"五","6":"六","7":"日"}
 */
@Slf4j
public enum Week {
    Mon("星期一", "Monday"),
    Tue("星期二", "Tuesday"),
    Wed("星期三", "Wednesday"),
    Thu("星期四", "Thursday"),
    Fri("星期五", "Friday"),
    Sat("星期六", "Saturday"),
    Sun("星期日", "Sunday"),
    ;
    final String zh;
    final String en;

    public String value() {
        return zh;
    }

    public String zh() {
        return zh;
    }

    public String en() {
        return en;
    }

    public String json() {
        return Maps.ofSS().put("zh_CN", zh).put("en_US", en).json();
    }

    Week(final String zh, final String en) {
        this.zh = zh;
        this.en = en;
    }

    public static void main(String[] args) {
        log.info(Week.Mon.name());
        log.info(Week.Mon.json());
        Dates before = Dates.now().addDay(-6);
        log.info("输出之前一周");
        for (int i = 0; i < 7; i++) {
            log.info(before.formatDate() + ": " + before.week() + " :" + before.week().json());
            before.addDay(1);
        }
        log.info("输出之后一周");
        Dates after = Dates.now();
        Stream.iterate(0, n -> n + 1)
                .limit(7)
                .forEach(n -> {
                    log.info(after.formatDate() + ": " + after.week() + " :" + after.week().json());
                    after.addDay(1);
                });
    }
}
