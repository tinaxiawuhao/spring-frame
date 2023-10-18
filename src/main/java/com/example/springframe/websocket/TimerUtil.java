package com.example.springframe.websocket;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangwei
 * @date 2023-03-24 11:19
 */
public class TimerUtil {

    public static final String IOT_DATA = "Iot_Data";

    public static final String BATCH_SEND_PRE = "BatchSend";

    private static final ConcurrentHashMap<String, Timer> TIMER_CACHE = new ConcurrentHashMap<>();

    public static void startTimerTask(String key, TimerTask timerTask, long delay, long period) {
        Timer timer = new Timer();
        timer.schedule(timerTask, delay, period);
        setTimerCache(key, timer);
    }

    public static void stopTimerTask(String key) {
        Timer timer = getTimeCache(key);
        if (Objects.nonNull(timer)) {
            timer.cancel();
            removeTimeCache(key);
        }
    }

    private static void setTimerCache(String key, Timer timer) {
        TIMER_CACHE.put(key, timer);
    }

    private static Timer getTimeCache(String key) {
        return TIMER_CACHE.get(key);
    }

    public static void removeTimeCache(String key) {
        TIMER_CACHE.remove(key);
    }

    public static String getPreKey(String pre, String key) {
        return pre + "-" + key;
    }

    public static String getIotDataPreKey(String key) {
        return getPreKey(IOT_DATA, key);
    }

    public static String getBatchSendPreKey(String key) {
        return getPreKey(BATCH_SEND_PRE, key);
    }
}
