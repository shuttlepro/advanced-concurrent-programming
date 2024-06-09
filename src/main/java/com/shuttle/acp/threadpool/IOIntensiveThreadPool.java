package com.shuttle.acp.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: Shuttle
 * @description: IO 密集型线程池
 */
public class IOIntensiveThreadPool {

    /**
     * CPU 核数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * IO 线程池最大线程数
     */
    private static final int IO_MAX_THREAD_COUNT = Math.max(2, CPU_COUNT * 2);

    /**
     * 空闲保活时限，单位为秒
     */
    private static final int KEEP_ALIVE_SECONDS = 60;

    /**
     * 有界队列 size
     */
    private static final int QUEUE_SIZE = 256;

    /**
     * ThreadPool Tag
     */
    private static final String THREAD_POOL_TAG = "IO-Intensive";

    /**
     * 懒汉式单例创建线程池
     */
    private static class IoIntensiveThreadPoolLazyHolder {

        private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
                IO_MAX_THREAD_COUNT,
                IO_MAX_THREAD_COUNT,
                KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue(QUEUE_SIZE),
                new CustomThreadFactory(THREAD_POOL_TAG));

        static {
            EXECUTOR.allowCoreThreadTimeOut(true);
            // 注册 JVM 关闭时的钩子函数
            Runtime.getRuntime().addShutdownHook(
                    new ShutdownHookThread(THREAD_POOL_TAG, (Callable<Void>) () -> {
                        ThreadPoolHelper.shutdownThreadPoolGracefully(EXECUTOR);
                        return null;
                    }));
        }
    }

    public static ThreadPoolExecutor getThreadPool() {
        return IoIntensiveThreadPoolLazyHolder.EXECUTOR;
    }

}
