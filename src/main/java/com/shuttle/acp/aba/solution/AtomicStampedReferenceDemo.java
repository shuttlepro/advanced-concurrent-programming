package com.shuttle.acp.aba.solution;

import com.shuttle.acp.util.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author: Shuttle
 * @description: AtomicStampedReference 类解决 ABA 问题
 */
@Slf4j
public class AtomicStampedReferenceDemo {

    /**
     * 关联更新引用对象，设置初始值和版本号为 0
     */
    private static final AtomicStampedReference<Integer> ATOMIC_STAMPED_REFERENCE =
            new AtomicStampedReference<>(0, 0);

    public static void main(String[] args) {
        startThreadA();
        startThreadB();
        startThreadC();
    }

    public static void startThreadA() {
        // Thread-A 最后执行完，value 0 -> 2 变更失败，stamp == 2
        new Thread(() -> {
            logCompareAndSet(0, 2, 0, 1);
            logValue();
            SleepUtils.sleep(1000);
            ATOMIC_STAMPED_REFERENCE.compareAndSet(0, 2, 0, 1);
            logValue();
        }, "Thread-A").start();
    }

    public static void startThreadB() {
        // Thread-B 先执行完，value 0 -> 3，stamp 0 -> 1
        new Thread(() -> {
            logCompareAndSet(0, 3, 0, 1);
            logValue();
            ATOMIC_STAMPED_REFERENCE.compareAndSet(0, 3, 0, 1);
            logValue();
        }, "Thread-B").start();
    }

    public static void startThreadC() {
        // Thread-C 紧接着 B 完成，value 3 -> 0，stamp 1 -> 2
        new Thread(() -> {
            logCompareAndSet(3, 0, 1, 2);
            logValue();
            SleepUtils.sleep(200);
            ATOMIC_STAMPED_REFERENCE.compareAndSet(3, 0, 1, 2);
            logValue();
        }, "Thread-C").start();
    }

    private static void logCompareAndSet(Integer expectedValue, Integer newValue, Integer expectedStamp, Integer newStamp) {
        log.info(Thread.currentThread().getName() + " compareAndSet(expectedValue: {}, newValue: {}, expectedStamp: {}, newStamp: {})",
                expectedValue, newValue, expectedStamp, newStamp);
    }

    private static void logValue() {
        log.info(Thread.currentThread().getName() + " getReference() = {}, getStamp() = {}",
                ATOMIC_STAMPED_REFERENCE.getReference(), ATOMIC_STAMPED_REFERENCE.getStamp());
    }

}
