package com.shuttle.acp.interruptthread;

import com.shuttle.acp.util.SleepUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Shuttle
 * @description: 使用 Thread 类的 Interrupt 方法
 */
@Slf4j
public class ThreadInterruptMethod {

    public static void main(String[] args) {
        new ThreadInterruptMethod().interruptThread();
    }

    private void interruptThread() {
        // 被中断线程，每隔 1s 输出 working...
        Thread interruptedThread = new Thread(() -> {
            String tName = Thread.currentThread().getName();
            while (!Thread.currentThread().isInterrupted()) {
                log.info(tName + " working...");
                try {
                    // 这里不使用工具方法，因为 sleep 时被打断时会重置标志位，这里需要主动再打断一次
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            log.info(tName + " interrupted...");
        }, "Thread-Interrupted");
        interruptedThread.start();

        // 中断线程，5s 后打断 interruptedThread
        new Thread(() -> {
            String tName = Thread.currentThread().getName();
            SleepUtils.sleep(5000);
            interruptedThread.interrupt();
            log.info(tName + " interrupt...");
        }, "Thread-Interrupt").start();
    }

}
