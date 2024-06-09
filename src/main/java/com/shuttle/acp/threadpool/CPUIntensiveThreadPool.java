package com.shuttle.acp.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: Shuttle
 * @description: CPU 密集型线程池
 */
public class CPUIntensiveThreadPool {

    /**
     * CPU 核数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * CPU 线程池最大线程数
     */
    private static final int CPU_MAX_THREAD_COUNT = CPU_COUNT;

    /**
     * 空闲保活时限，单位为秒
     */
    private static final int KEEP_ALIVE_SECONDS = 60;

    /**
     * 有界队列 size
     */
    private static final int QUEUE_SIZE = 1000;

    /**
     * ThreadPool Tag
     */
    private static final String THREAD_POOL_TAG = "CPU-Intensive";

    /**
     * 懒汉式单例创建线程池
     */
    private static class CpuIntensiveThreadPoolLazyHolder {

        private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
                CPU_MAX_THREAD_COUNT,
                CPU_MAX_THREAD_COUNT,
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
        return CpuIntensiveThreadPoolLazyHolder.EXECUTOR;
    }

}
