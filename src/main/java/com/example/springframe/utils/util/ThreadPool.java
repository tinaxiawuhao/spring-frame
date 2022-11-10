package com.example.springframe.utils.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * 线程池管理，
 * 参考：
 * <pre>
 * https://mp.weixin.qq.com/s/VDW6i5Hz38UdLamj_9vOVQ
 * https://mp.weixin.qq.com/s/zFBXJEaQ2s3dvNwYLz_MRA
 * </pre>
 */
@Slf4j
public abstract class ThreadPool {
    private ThreadPool() {
    }

    /**
     * 启动线程，执行run方法
     *
     * @param runnable 线程方法
     */
    public static void run(final Runnable runnable) {
        Multi.getInstance().execute(runnable);
    }

    /**
     * 多线程池对象
     *
     * @return {@link Multi}
     */
    public static Multi multi() {
        return Multi.getInstance();
    }

    /**
     * 但线程池对象
     *
     * @return {@link Single}
     */
    public static Single single() {
        return Single.getInstance();
    }

    /**
     * 获得执行对象；默认使用多线程方式
     *
     * @return {@link ThreadPoolExecutor}
     */
    public abstract ThreadPoolExecutor getExecutor();

    /**
     * 启动线程，执行run方法
     *
     * @param runnable 线程方法
     */
    public void execute(final Runnable runnable) {
        getExecutor().execute(runnable);
        if (log.isDebugEnabled()) {
            log.debug(String.format("当前活动的线程数量：%d", getExecutor().getActiveCount()));
            log.debug(String.format("当前任务数量：%d", getExecutor().getTaskCount()));
            log.debug(String.format("线程总数：%d", getExecutor().getPoolSize()));
        }
    }

    /**
     * 执行任务，等待返回结果
     *
     * @param callable {@link Callable}
     * @return {@link Future}
     */
    public <T> Future<T> submit(final Callable<T> callable) {
        return getExecutor().submit(callable);
    }

    /**
     * 执行任务，等待返回结果，执行完成之后返回 null 值
     *
     * @param runnable {@link Runnable}
     * @return {@link Future}
     */
    public Future<?> submit(final Runnable runnable) {
        return getExecutor().submit(runnable);
    }

    /**
     * 执行任务，等待返回结果，执行完成之后返回 指定结果
     *
     * @param runnable {@link Runnable}
     * @param result   T 指定返回结果
     * @return {@link Future}
     */
    public <T> Future<T> submit(final Runnable runnable, final T result) {
        return getExecutor().submit(runnable, result);
    }

    /**
     * invokeAll()是同步的，其需要等待任务的完成，才能返回。submit()是异步的
     *
     * @param tasks {@link Collection>}{@link Collection<Callable>} Callable 任务集合
     * @param <T>   执行任务后返回结果
     * @return {@link List}{@link List<Future>}
     */
    @SneakyThrows
    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) {
        return getExecutor().invokeAll(tasks);
    }

    /**
     * invokeAll()是同步的，其需要等待任务的完成，才能返回。submit()是异步的
     *
     * @param tasks   {@link Collection}{@link Collection<Callable>} Callable 任务集合
     * @param <T>     执行任务后返回结果
     * @param timeout long 超时时间
     * @return {@link List}{@link List<Future>}
     */
    @SneakyThrows
    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout) {
        return getExecutor().invokeAll(tasks, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * invokeAny() 取第一个任务的返回值，然后调用interrupt方法中断其它任务。
     *
     * @param tasks {@link Collection}{@link Collection<Callable>} Callable 任务集合
     * @param <T>   执行任务后返回结果
     * @return T
     */
    @SneakyThrows
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks) {
        return getExecutor().invokeAny(tasks);
    }

    /**
     * invokeAny() 取第一个任务的返回值，然后调用interrupt方法中断其它任务。
     *
     * @param tasks   {@link Collection}{@link Collection<Callable>} Callable 任务集合
     * @param <T>     执行任务后返回结果
     * @param timeout long 超时时间
     * @return T
     */
    @SneakyThrows
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout) {
        return getExecutor().invokeAny(tasks, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 释放线程
     */
    public void shutdown() {
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s 当前活动的线程数量：%d", getClass().getSimpleName(), getExecutor().getActiveCount()));
            log.debug(String.format("%s 当前任务数量：%d", getClass().getSimpleName(), getExecutor().getTaskCount()));
            log.debug(String.format("%s 线程总数：%d", getClass().getSimpleName(), getExecutor().getPoolSize()));
        }
        if (!getExecutor().isShutdown()) {
            getExecutor().shutdown();
        }
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s 已停止：%b", getClass().getSimpleName(), getExecutor().isTerminated()));
            log.debug(String.format("%s 已关闭：%b", getClass().getSimpleName(), getExecutor().isShutdown()));
        }
    }

    /**
     * 释放线程
     */
    public static void shutdownAll() {
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s 当前活动的线程数量：%d", Multi.class.getSimpleName(), Multi.getInstance().getExecutor().getActiveCount()));
            log.debug(String.format("%s 当前任务数量：%d", Multi.class.getSimpleName(), Multi.getInstance().getExecutor().getTaskCount()));
            log.debug(String.format("%s 线程总数：%d", Multi.class.getSimpleName(), Multi.getInstance().getExecutor().getPoolSize()));
        }
        if (!Multi.getInstance().getExecutor().isShutdown()) {
            Multi.getInstance().getExecutor().shutdown();
        }
        if (!Single.getInstance().getExecutor().isShutdown()) {
            Single.getInstance().getExecutor().shutdown();
        }
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s 已停止：%b", Multi.class.getSimpleName(), Multi.getInstance().getExecutor().isTerminated()));
            log.debug(String.format("%s 已关闭：%b", Multi.class.getSimpleName(), Multi.getInstance().getExecutor().isShutdown()));
            log.debug(String.format("%s 已停止：%b", Single.class.getSimpleName(), Single.getInstance().getExecutor().isTerminated()));
            log.debug(String.format("%s 已关闭：%b", Single.class.getSimpleName(), Single.getInstance().getExecutor().isShutdown()));
        }
    }

    /**
     * 单线程池
     */
    public static class Single extends ThreadPool {
        private static volatile Single instance = null;
        private static volatile ThreadPoolExecutor executor = null;

        private Single() {
//		(1) Executors.newCachedThreadPool
//		创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
//		线程池为无限大，当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程。
//		(2) Executors.newFixedThreadPool
//		创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
//		定长线程池的大小最好根据系统资源进行设置。如Runtime.getRuntime().availableProcessors()
//		(3) Executors.newScheduledThreadPool
//		创建一个定长线程池，支持定时及周期性任务执行。
//		(4) Executors.newSingleThreadExecutor
//		创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
            executor = new ThreadPoolExecutor(
                    1,
                    1,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(10024),
                    new ThreadFactoryBuilder().setNameFormat("single-thread-%d").build(),
                    new ThreadPoolExecutor.AbortPolicy()
            );
        }

        static Single getInstance() {
            if (Objects.isNull(instance)) {
                synchronized (Single.class) {
                    if (Objects.isNull(instance)) {
                        instance = new Single();
                    }
                }
            }
            return instance;
        }

        @Override
        public ThreadPoolExecutor getExecutor() {
            return executor;
        }

    }

    /**
     * 多线程池
     */
    public static class Multi extends ThreadPool {
        private static volatile Multi instance = null;
        private static volatile ThreadPoolExecutor executor = null;

        private Multi() {
//		(1) Executors.newCachedThreadPool
//		创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
//		线程池为无限大，当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程。
//		(2) Executors.newFixedThreadPool
//		创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
//		定长线程池的大小最好根据系统资源进行设置。如Runtime.getRuntime().availableProcessors()
//		(3) Executors.newScheduledThreadPool
//		创建一个定长线程池，支持定时及周期性任务执行。
//		(4) Executors.newSingleThreadExecutor
//		创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
            executor = new ThreadPoolExecutor(
                    16,
                    16 * 4,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(10024),
//                    new LinkedBlockingQueue<>(100),
                    new ThreadFactoryBuilder().setNameFormat("multi-thread-%d").build(),
                    new ThreadPoolExecutor.AbortPolicy()
            );
        }

        static Multi getInstance() {
            if (Objects.isNull(instance)) {
                synchronized (Multi.class) {
                    if (Objects.isNull(instance)) {
                        instance = new Multi();
                    }
                }
            }
            return instance;
        }

        @Override
        public ThreadPoolExecutor getExecutor() {
            return executor;
        }
    }


    @SneakyThrows
    public static void main(String[] args) {
        final long start = System.currentTimeMillis();
        if (false) {
            final Runnable runnable = new Runnable() {
                private ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10) {{
                    offer(1);
                    offer(2);
                    offer(3);
                    offer(4);
                    offer(5);
                    offer(6);
                    offer(7);
                    offer(8);
                    offer(9);
                    offer(10);
                }};
                private String name = "Runnable";

                @Override
                public void run() {
                    while (queue.size() > 0) {
                        Integer value = queue.poll();
                        if (null != value) {
                            long ms = queue.size() * 100;
                            System.out.println(Thread.currentThread().getName() + " > " + name + ":" + value + ", waiting:" + ms + "ms, " + queue.toString());
                            try {
                                Thread.sleep(ms);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
            ThreadPool.run(runnable);
            ThreadPool.run(runnable);
            ThreadPool.run(runnable);
        }
        if (false) {
            final Thread thread = new Thread() {
                private ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10) {{
                    offer(1);
                    offer(2);
                    offer(3);
                    offer(4);
                    offer(5);
                    offer(6);
                    offer(7);
                    offer(8);
                    offer(9);
                    offer(10);
                }};
                private String name = "Thread";

                @Override
                public void run() {
                    while (queue.size() > 0) {
                        Integer value = queue.poll();
                        if (null != value) {
                            long ms = queue.size() * 100;
                            System.out.println(Thread.currentThread().getName() + " > " + name + ":" + value + ", waiting:" + ms + "ms, " + queue.toString());
                            try {
                                Thread.sleep(ms);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
            ThreadPool.run(thread);
            ThreadPool.run(thread);
            ThreadPool.run(thread);
        }

        if (false) { // 测试返回值同步任务
            final Callable<String> callable = () -> {
                long id = Num.of(Util.random(4)).intValue();
                System.out.println(id + ":call" + ":" + (System.currentTimeMillis() - start));
                for (int i = 0; i < 5; i++) {
                    System.out.println(id + ":" + i + ":" + (System.currentTimeMillis() - start));
                    Thread.sleep(id);
                }
                return id + ":call执行结束" + ":" + (System.currentTimeMillis() - start);
            };
            final Future<String> A = ThreadPool.multi().submit(callable);
            final Future<String> B = ThreadPool.multi().submit(callable);
            final Future<String> C = ThreadPool.multi().submit(callable);
            try {
                System.out.println("start > " + start);
                System.out.println(A.get()); // 这里开始阻塞，永远等待 print A ，才执行 print B
                System.out.println("wait A > " + start);
                System.out.println(B.get());
                System.out.println("wait B > " + start);
                System.out.println(C.get());
                System.out.println("wait C > " + start);
                System.out.println("最终耗时 > " + (System.currentTimeMillis() - start));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        if (false) { // 测试返回值同步任务
            final Callable<String> callable = () -> {
                long id = Num.of(Util.random(4)).intValue();
                System.out.println(id + ":call" + ":" + (System.currentTimeMillis() - start));
                for (int i = 0; i < 5; i++) {
                    System.out.println(id + ":" + i + ":" + (System.currentTimeMillis() - start));
                    Thread.sleep(id);
                }
                return id + ":call执行结束" + ":" + (System.currentTimeMillis() - start);
            };
            final List<Future<String>> futures = ThreadPool.multi().invokeAll(Arrays.asList(callable, callable, callable));
            // 这里开始阻塞，start 必须要等所有 futures 返回之后打印
            System.out.println("start > " + start);
            for (Future<String> future : futures) {
                System.out.println(future.get());
            }
            System.out.println("end > " + (System.currentTimeMillis() - start));
        }
        if (false) {
            final Callable<String> callable = () -> {
                long id = Num.of(Util.random(4)).intValue();
                System.out.println(id + ":call" + ":" + (System.currentTimeMillis() - start));
                for (int i = 0; i < 5; i++) {
                    System.out.println(id + ":" + i + ":" + (System.currentTimeMillis() - start));
                    Thread.sleep(id);
                }
                return id + ":call执行结束" + ":" + (System.currentTimeMillis() - start);
            };
            final String value = ThreadPool.multi().invokeAny(Arrays.asList(callable, callable, callable));
            // 这里开始阻塞，start 必须要等 future 返回之后打印
            System.out.println("start > " + start);
            System.out.println(value);
            System.out.println("end > " + (System.currentTimeMillis() - start));
        }
        if (true) {
            RangeInt.of(1, 1000).forEach(i -> {
                ThreadPool.run(() -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(i + " > " + Thread.currentThread().getId());
                });
            });
        }
        ThreadPool.shutdownAll();
    }
}
