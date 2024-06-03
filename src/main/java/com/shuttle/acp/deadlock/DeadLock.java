package com.shuttle.acp.deadlock;

import com.shuttle.acp.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Shuttle
 * @description: 定义两个资源 A、B，模拟两个互相争抢
 */
@Slf4j
public class DeadLock {

    private static final Object A = new Object();
    private static final Object B = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            String tName = Thread.currentThread().getName();
            while (true) {
                synchronized (A) {
                    log.info(tName + " acquire resource A");
                    SleepUtils.sleep(10);
                    synchronized (B) {
                        log.info(tName + " acquire resource B");
                    }
                }
            }
        }, "Thread-A").start();

        new Thread(() -> {
            String tName = Thread.currentThread().getName();
            while (true) {
                synchronized (B) {
                    log.info(tName + " acquire resource B");
                    SleepUtils.sleep(10);
                    synchronized (A) {
                        log.info(tName + " acquire resource A");
                    }
                }
            }
        }, "Thread-B").start();
    }

}
