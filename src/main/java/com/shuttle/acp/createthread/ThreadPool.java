package com.shuttle.acp.createthread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: Shuttle
 * @description: 线程创建方式四：使用线程池
 */
public class ThreadPool {

    /**
     * Executors 工厂类创建线程池：⚠️ 这里仅仅是为了演示，生产环境禁用 ⚠️，线程池使用详见 package: com.shuttle.acp.threadpool
     */
    private static ExecutorService threadPool = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        /**
         * 方法一：执行一个 Runnable 类型的 target 执行目标实例，无返回
         *        void execute(Runnable command);
         * 方法二：提交一个 Callable 类型的 target 执行目标实例，返回一个 Future 异步任务实例
         *        <T> Future<T> submit(Callable<T> task);
         * 方法三：提交一个 Runnable类型的 target执行目标实例，返回一个Future异步任务实例
         *        Future<?> submit(Runnable task);
         */
    }

}
