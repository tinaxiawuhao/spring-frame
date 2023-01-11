package com.example.springframe.utils;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author
 * @date 2021-08-12 17:25
 */
public class ThreadPoolUtil {

    /**
     * 核心线程数（默认线程数）
     */
    private static final int CORE_POOL_SIZE = 10;

    /**
     * 最大线程数
     */
    private static final int MAX_POOL_SIZE = 30;

    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    private static final int KEEP_ALIVE_TIME = 60;

    /**
     * 缓冲队列数
     */
    private static final int QUEUE_CAPACITY = 200;

    /**
     * 线程池名前缀
     */
    private static final String THREAD_NAME_PREFIX = "Async-Task-";

    private final static Executor EXECUTOR;

    static {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
        executor.setAwaitTerminationSeconds(600);

        executor.initialize();
        EXECUTOR = executor;
    }

    /**
     * 获取实例
     * @return Executor
     */
    public static Executor getInstants() {
        return ThreadPoolUtil.EXECUTOR;
    }

}
