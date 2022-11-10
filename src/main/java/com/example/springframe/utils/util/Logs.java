package com.example.springframe.utils.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Random;

/**
 * 日志工具类
 */
@Slf4j
public final class Logs {

    private enum Event {
        // 调试日志
        DEBUG("调试日志"),
        INFO("基本信息日志"),
        WARN("警告日志"),
        ERROR("异常日志"),
        ;
        /**
         * 枚举属性说明
         */
        final String comment;

        Event(String comment) {
            this.comment = comment;
        }
    }

    private final String filePath;
    private final FileWriter writer;

    @SneakyThrows
    private Logs(final File file) {
        this.filePath = file.getAbsolutePath();
        if (!file.getParentFile().mkdirs())
            throw new NullPointerException(String.format("目录创建失败：%s", file.getParentFile().getAbsolutePath()));
        this.writer = new FileWriter(file, true);
    }

    @SneakyThrows
    private Logs(Class<?> clazz, String uid) {
        String home = System.getProperty("catalina.home");
        if (Objects.isNull(home)) {
            home = System.getProperty("user.dir");
        }
        if (Objects.isNull(home)) {
            home = File.separator;
        }
        uid = (Objects.nonNull(uid) && uid.length() > 0) ? uid : String.format("%06d", new Random().nextInt(999999));
        this.filePath = Paths.get(
                home,
                "logs",
                clazz.getSimpleName(),
                new SimpleDateFormat("yyyyMMddHHmmssSSS").format(System.currentTimeMillis()).concat("_").concat(uid).concat(".log")
        ).toAbsolutePath().toString();
        final File file = new File(filePath);
        if (!file.getParentFile().mkdirs())
            throw new NullPointerException(String.format("目录创建失败：%s", file.getParentFile().getAbsolutePath()));
        writer = new FileWriter(file, true);
    }

    /**
     * 开始记录日志文件.初始化并创建文件
     */
    public static Logs start(Class<?> clazz) {
        return new Logs(clazz, null);
    }

    public static Logs start(Class<?> clazz, String uid) {
        return new Logs(clazz, uid);
    }

    public static Logs start(final File file) {
        return new Logs(file);
    }

    /**
     * 获取日志文件路径
     *
     * @return String
     */
    public String getLogPath() {
        return filePath;
    }

    /**
     * 写入日志结束，关闭文件
     *
     * @return String 文件绝对路径
     */
    public String end() {
        if (Objects.nonNull(writer)) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.filePath;
    }

    /**
     * 关闭日志文件，同 {@link Logs#end()} 一样
     */
    public void close() {
        this.end();
    }


    private void write(StackTraceElement element, Event event, String message, Exception e) {
        if (Objects.isNull(message)) {
            message = "";
        }
//		log.info(message, e);
        String exception = "";
        if (Objects.nonNull(e)) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            exception = sw.toString();
        }
        try {
            writer.write(String.format("%s %s [%s:%s:%d] - %s%n%s",
                    new Timestamp(System.currentTimeMillis()).toString(),
                    event.toString(),
                    element.getClassName(),
                    element.getMethodName(),
                    element.getLineNumber(),
                    message,
                    exception
            ));
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Logs d(String message) {
        d(message, null);
        return this;
    }

    public Logs d(Exception exception) {
        d(null, exception);
        return this;
    }

    public Logs d(String message, Exception exception) {
        write(Thread.currentThread().getStackTrace()[(Objects.isNull(message) || Objects.isNull(exception)) ? 3 : 2]
                , Event.DEBUG, message, exception);
        return this;
    }

    public Logs i(String message) {
        i(message, null);
        return this;
    }

    public Logs i(Exception exception) {
        i(null, exception);
        return this;
    }

    public Logs i(String message, Exception exception) {
        write(Thread.currentThread().getStackTrace()[(Objects.isNull(message) || Objects.isNull(exception)) ? 3 : 2]
                , Event.INFO, message, exception);
        return this;
    }

    public Logs w(String message) {
        w(message, null);
        return this;
    }

    public Logs w(Exception exception) {
        w(null, exception);
        return this;
    }

    public Logs w(String message, Exception exception) {
        write(Thread.currentThread().getStackTrace()[(Objects.isNull(message) || Objects.isNull(exception)) ? 3 : 2]
                , Event.WARN, message, exception);
        return this;
    }

    public Logs e(String message) {
        e(message, null);
        return this;
    }

    public Logs e(Exception exception) {
        e(null, exception);
        return this;
    }

    public Logs e(String message, Exception exception) {
        write(Thread.currentThread().getStackTrace()[(Objects.isNull(message) || Objects.isNull(exception)) ? 3 : 2]
                , Event.ERROR, message, exception);
        return this;
    }
}
