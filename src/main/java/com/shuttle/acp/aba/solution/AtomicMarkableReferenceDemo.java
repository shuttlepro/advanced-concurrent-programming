package com.shuttle.acp.aba.solution;

import com.shuttle.acp.util.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @author: Shuttle
 * @description: AtomicMarkableReference 类解决 ABA 问题
 */
@Slf4j
public class AtomicMarkableReferenceDemo {

    /**
     * 关联更新引用对象，设置初始值和标识位
     */
    private static final AtomicMarkableReference<Integer> ATOMIC_MARKABLE_REFERENCE =
            new AtomicMarkableReference<>(0, false);

    public static void main(String[] args) {
        startThreadA();
        startThreadB();
        startThreadC();
    }

    public static void startThreadA() {
        // Thread-A 最后执行完，value 0 -> 2 变更失败，mark == true
        new Thread(() -> {
            logCompareAndSet(0, 2, false, true);
            logValue();
            SleepUtils.sleep(1000);
            ATOMIC_MARKABLE_REFERENCE.compareAndSet(0, 2, false, true);
            logValue();
        }, "Thread-A").start();
    }

    public static void startThreadB() {
        // Thread-B 先执行完，value 0 -> 3 变更成功，mark false -> true
        new Thread(() -> {
            logCompareAndSet(0, 3, false, true);
            logValue();
            ATOMIC_MARKABLE_REFERENCE.compareAndSet(0, 3, false, true);
            logValue();
        }, "Thread-B").start();
    }

    public static void startThreadC() {
        // Thread-C 紧接着 B 完成，value 3 -> 0 变更失败，mark == true
        new Thread(() -> {
            logCompareAndSet(3, 0, false, true);
            logValue();
            SleepUtils.sleep(200);
            ATOMIC_MARKABLE_REFERENCE.compareAndSet(3, 0, false, true);
            logValue();
        }, "Thread-C").start();
    }

    private static void logCompareAndSet(Integer expectedValue, Integer newValue, boolean expectedMark, boolean newMark) {
        log.info(Thread.currentThread().getName() + " compareAndSet(expectedValue: {}, newValue: {}, expectedMark: {}, newMark: {})",
                expectedValue, newValue, expectedMark, newMark);
    }

    private static void logValue() {
        log.info(Thread.currentThread().getName() + " getReference() = {}, isMarked() = {}",
                ATOMIC_MARKABLE_REFERENCE.getReference(), ATOMIC_MARKABLE_REFERENCE.isMarked());
    }

}
