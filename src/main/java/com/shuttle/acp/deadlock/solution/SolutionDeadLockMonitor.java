package com.shuttle.acp.deadlock.solution;

import com.shuttle.acp.util.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: Shuttle
 * @description: 另起一个后台监视线程，如果发现有死锁进行打断
 */
@Slf4j
public class SolutionDeadLockMonitor {

    private static final ReentrantLock A = new ReentrantLock();
    private static final ReentrantLock B = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        deadLock();
        TimeUnit.SECONDS.sleep(2);
        checkDeadLock();
    }

    public static void deadLock() {
        new Thread(() -> {
            String tName = Thread.currentThread().getName();
            while (true) {
                try {
                    SleepUtils.sleepRandom(10, 50);
                    // 在当前线程未被打断的条件下尝试获取锁
                    A.lockInterruptibly();
                    log.info(tName + " acquire resource A");
                    try {
                        B.lockInterruptibly();
                        log.info(tName + " acquire resource B");
                    } catch (InterruptedException e) {
                        log.info(tName + " interrupted");
                        // 被打断时需要重置标志位，防止其它使用此属性的逻辑受影响
                        Thread.interrupted();
                    } finally {
                        if (B.isHeldByCurrentThread()) {
                            log.info(tName + " release resource B");
                            B.unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    log.info(tName + " interrupted");
                    Thread.interrupted();
                } finally {
                    if (A.isHeldByCurrentThread()) {
                        log.info(tName + " release resource A");
                        A.unlock();
                    }
                }
            }
        }, "Thread-A").start();

        new Thread(() -> {
            String tName = Thread.currentThread().getName();
            while (true) {
                try {
                    // 在当前线程未被打断的条件下尝试获取锁
                    SleepUtils.sleepRandom(10, 50);
                    B.lockInterruptibly();
                    log.info(tName + " acquire resource B");
                    try {
                        A.lockInterruptibly();
                        log.info(tName + " acquire resource A");
                    } catch (InterruptedException e) {
                        log.info(tName + " interrupted");
                        Thread.interrupted();
                    } finally {
                        if (A.isHeldByCurrentThread()) {
                            log.info(tName + " release resource A");
                            A.unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    log.info(tName + " interrupted");
                    Thread.interrupted();
                } finally {
                    if (B.isHeldByCurrentThread()) {
                        log.info(tName + " release resource B");
                        B.unlock();
                    }
                }
            }
        }, "Thread-B").start();
    }

    public static void checkDeadLock() {
        new Thread(() -> {
            while (true) {
                ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
                long[] deadLockThreadIds = threadMXBean.findDeadlockedThreads();

                if (deadLockThreadIds != null && deadLockThreadIds.length > 0) {
                    Set<Long> deadLockThreadIdSets = Arrays.stream(threadMXBean.getThreadInfo(deadLockThreadIds))
                            .map(ThreadInfo::getThreadId)
                            .collect(Collectors.toSet());
                    Map<Long, Thread> idToThreadMap = Thread.getAllStackTraces().keySet().stream()
                            .collect(Collectors.toMap(Thread::threadId, Function.identity()));

                    String tName = Thread.currentThread().getName();
                    idToThreadMap.entrySet().stream()
                            .filter(entry -> deadLockThreadIdSets.contains(entry.getKey()))
                            .map(Map.Entry::getValue)
                            .peek(thread -> log.info(tName + " interrupt " + thread.getName()))
                            .forEach(Thread::interrupt);
                }
                SleepUtils.sleep(100);
            }
        }, "Thread-DeadLockMonitor").start();
    }

}
