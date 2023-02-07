package com.example.springframe.utils.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author
 * @date 2021-08-12 17:25
 */
@Slf4j
public class ThreadPoolUtil {

    /**
     * cpu 核心数
     */
    private static final int AVALIABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    /**
     * 缓冲队列数
     */
    private static final int QUEUE_CAPACITY = 200;

    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    private static final int KEEP_ALIVE_TIME = 60;

    /**
     * 线程池名前缀
     */
    private static final String THREAD_NAME_PREFIX = "Async-Task-";

    private final static Executor EXECUTOR;

    // 最大超时时间
    private static final int TIMEOUT_VALUE = 1500;

    // 时间单位
    private static final TimeUnit TIMEOUT_UNIT = TimeUnit.MILLISECONDS;

    static {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(AVALIABLE_PROCESSORS);
        executor.setMaxPoolSize(3*AVALIABLE_PROCESSORS);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
        executor.setAwaitTerminationSeconds(TIMEOUT_VALUE);

        executor.initialize();
        EXECUTOR = executor;
    }

    public static final class Delayer {

        static final ScheduledThreadPoolExecutor delayer;

        /**
         * 异常线程，不做请求处理，只抛出异常
         */
        static {
            delayer = new ScheduledThreadPoolExecutor(1, new DaemonThreadFactory());
            delayer.setRemoveOnCancelPolicy(true);
        }

        static ScheduledFuture<?> delay(Runnable command, long delay, TimeUnit unit) {
            return delayer.schedule(command, delay, unit);
        }

        static final class DaemonThreadFactory implements ThreadFactory {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setName("CompletableFutureScheduler");
                return t;
            }
        }
    }

    /**
     * 获取实例
     * @return Executor
     */
    public static Executor getInstants() {
        return ThreadPoolUtil.EXECUTOR;
    }

    /**
     * 有返回值的异步
     * @param supplier
     * @param <T>
     * @return
     */
    public static  <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier){
        return supplyAsync(TIMEOUT_VALUE,TIMEOUT_UNIT,supplier);
    }

    /**
     * 有返回值的异步 - 可设置超时时间
     * @param timeout
     * @param unit
     * @param supplier
     * @param <T>
     * @return
     */
    public static  <T> CompletableFuture<T> supplyAsync(long timeout, TimeUnit unit, Supplier<T> supplier){
        return CompletableFuture.supplyAsync(supplier, EXECUTOR)
                .applyToEither(timeoutAfter(timeout,unit), Function.identity())
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    log.error(throwable.getMessage());
                    return null;
                });
    }

    /**
     * 无返回值的异步
     * @param runnable
     * @return
     */
    public static CompletableFuture runAsync(Runnable runnable){
        return runAsync(TIMEOUT_VALUE,TIMEOUT_UNIT,runnable);
    }

    /**
     * 无返回值的异步 - 可设置超时时间
     * @param runnable
     * @return
     */
    public static CompletableFuture runAsync(long timeout, TimeUnit unit,Runnable runnable){
        return CompletableFuture.runAsync(runnable,EXECUTOR)
                .applyToEither(timeoutAfter(timeout,unit), Function.identity())
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    log.error(throwable.getMessage());
                    return null;
                });
    }

    /**
     * 统一处理异步结果
     * @param futures
     * @return
     */
    public static CompletableFuture allOf(CompletableFuture... futures){
        return allOf(TIMEOUT_VALUE,TIMEOUT_UNIT,futures);
    }

    /**
     * 统一处理异步结果 - 可设置超时时间
     * @param futures
     * @return
     */
    public static CompletableFuture allOf(long timeout, TimeUnit unit, CompletableFuture... futures){
        return CompletableFuture.allOf(futures)
                .applyToEither(timeoutAfter(timeout,unit), Function.identity())
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    log.error(throwable.getMessage());
                    return null;
                });
    }

    /**
     * 异步超时处理
     * @param timeout
     * @param unit
     * @param <T>
     * @return
     */
    public static <T> CompletableFuture<T> timeoutAfter(long timeout, TimeUnit unit) {
        CompletableFuture<T> result = new CompletableFuture<T>();
        // timeout 时间后 抛出TimeoutException 类似于sentinel / watcher
        Delayer.delayer.schedule(() -> result.completeExceptionally(new TimeoutException()), timeout, unit);
        return result;
    }

    public static <T> CompletableFuture<T> timeoutAfter() {
        CompletableFuture<T> result = new CompletableFuture<T>();
        // timeout 时间后 抛出TimeoutException 类似于sentinel / watcher
        Delayer.delayer.schedule(() -> result.completeExceptionally(new TimeoutException()), TIMEOUT_VALUE, TIMEOUT_UNIT);
        return result;
    }

}
