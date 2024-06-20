package com.shuttle.acp.aba;

import com.shuttle.acp.util.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Shuttle
 * @description: ABA 问题的复现
 */
@Slf4j
public class ABADemo {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    public static void main(String[] args) {
        startThreadA();
        startThreadB();
        startThreadC();
    }

    public static void startThreadA() {
        // Thread-A 最后执行完，将 0 -> 2，实际上 ATOMIC_INTEGER 已经被 B、C 线程变更过
        new Thread(() -> {
            logCompareAndSet(0, 2);
            logValue();
            SleepUtils.sleep(1000);
            ATOMIC_INTEGER.compareAndSet(0, 2);
            logValue();
        }, "Thread-A").start();
    }

    public static void startThreadB() {
        // Thread-B 先执行完，将 0 -> 3
        new Thread(() -> {
            logCompareAndSet(0, 3);
            logValue();
            ATOMIC_INTEGER.compareAndSet(0, 3);
            logValue();
        }, "Thread-B").start();
    }

    public static void startThreadC() {
        // Thread-C 紧接着 B 完成，将 3 -> 0
        new Thread(() -> {
            logCompareAndSet(3, 0);
            logValue();
            SleepUtils.sleep(200);
            ATOMIC_INTEGER.compareAndSet(3, 0);
            logValue();
        }, "Thread-C").start();
    }

    private static void logCompareAndSet(Integer expectedValue, Integer newValue) {
        log.info(Thread.currentThread().getName() + " compareAndSet(expectedValue: {}, newValue: {})", expectedValue, newValue);
    }

    private static void logValue() {
        log.info(Thread.currentThread().getName() + " get() = {}", ATOMIC_INTEGER.get());
    }

}
