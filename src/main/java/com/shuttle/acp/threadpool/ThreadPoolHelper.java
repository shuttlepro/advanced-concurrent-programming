package com.shuttle.acp.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: Shuttle
 * @description: ThreadPoolHelper
 */
@Slf4j
public class ThreadPoolHelper {

    public static void shutdownThreadPoolGracefully(ExecutorService threadPool) {
        if (threadPool == null || threadPool.isTerminated()) {
            return;
        }

        threadPool.shutdown();
        try {
            // 等待 60 秒，等待线程池中的任务完成执行
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                // 调用 shutdownNow 取消正在执行的任务
                threadPool.shutdownNow();
                // 再次等待 60 秒，如果还未结束，记录错误信息
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.warn("thread pool task did not end normally!");
                }
            }
        } catch (InterruptedException ie) {
            // 捕获异常，重新调用 shutdownNow
            threadPool.shutdownNow();
            // 恢复中断状态
            Thread.currentThread().interrupt();
        }

        // 仍然没有关闭，循环关闭 10 次，每次等待 100 毫秒
        if (!threadPool.isTerminated()) {
            for (int i = 0; i < 10; i++) {
                try {
                    if (threadPool.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                        break;
                    }
                } catch (InterruptedException e) {
                    log.error("waiting shutdown but interrupted. message: {}", e.getMessage());
                    // 恢复中断状态
                    Thread.currentThread().interrupt();
                    break;
                }
                threadPool.shutdownNow();
            }
        }
    }

}
