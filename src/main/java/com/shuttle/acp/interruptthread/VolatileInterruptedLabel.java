package com.shuttle.acp.interruptthread;

import com.shuttle.acp.util.SleepUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Shuttle
 * @description: 使用 volatile 修饰中断标识位
 */
@Slf4j
public class VolatileInterruptedLabel {

    // volatile 可以保证 isInterrupted 变量在多线程环境下发生变化时被所有线程感知
    private volatile boolean isInterrupted = false;

    public static void main(String[] args) {
        new VolatileInterruptedLabel().interruptThread();
    }

    private void interruptThread() {
        // 被中断线程，每隔 1s 输出 working...
        new Thread(() -> {
            String tName = Thread.currentThread().getName();
            while (!isInterrupted) {
                log.info(tName + " working...");
                SleepUtils.sleep(1000);
            }
            log.info(tName + " interrupted...");
        }, "Thread-Interrupted").start();

        // 中断线程，5s 后设置标志位
        new Thread(() -> {
            String tName = Thread.currentThread().getName();
            SleepUtils.sleep(5000);
            isInterrupted = true;
            log.info(tName + " interrupt...");
        }, "Thread-Interrupt").start();
    }

}
