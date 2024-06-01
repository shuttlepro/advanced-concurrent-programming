package com.shuttle.acp.deadlock.solution;

import com.shuttle.acp.deadlock.SleepUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Shuttle
 * @description: 两个线程按序申请资源
 */
@Slf4j
public class SolutionAppliedInOrder {

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
                synchronized (A) {
                    log.info(tName + " acquire resource A");
                    SleepUtils.sleep(10);
                    synchronized (B) {
                        log.info(tName + " acquire resource B");
                    }
                }
            }
        }, "Thread-B").start();
    }

}
