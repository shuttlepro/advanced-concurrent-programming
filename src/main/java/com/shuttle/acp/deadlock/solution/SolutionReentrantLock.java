package com.shuttle.acp.deadlock.solution;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: Shuttle
 * @description: 使用 ReentrantLock 的 tryLock + timeout 替换 Object + synchronized
 */
@Slf4j
public class SolutionReentrantLock {

    private static final ReentrantLock A = new ReentrantLock();
    private static final ReentrantLock B = new ReentrantLock();

    public static void main(String[] args) {
        new Thread(() -> {
            String tName = Thread.currentThread().getName();
            while (true) {
                try {
                    // 尝试获取锁，10 ms 后超时
                    if (A.tryLock(10, TimeUnit.MILLISECONDS)) {
                        log.info(tName + " acquire resource A");
                        try {
                            if (B.tryLock(10, TimeUnit.MILLISECONDS)) {
                                log.info(tName + " acquire resource B");
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } finally {
                            // 释放锁时需判断是否正常持有锁，否则会有 IllegalMonitorStateException
                            if (B.isHeldByCurrentThread()) {
                                log.info(tName + " release resource B");
                                B.unlock();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
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
                    if (B.tryLock(10, TimeUnit.MILLISECONDS)) {
                        log.info(tName + " acquire resource B");
                        try {
                            if (A.tryLock(10, TimeUnit.MILLISECONDS)) {
                                log.info(tName + " acquire resource A");
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } finally {
                            if (A.isHeldByCurrentThread()) {
                                log.info(tName + " release resource A");
                                A.unlock();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (B.isHeldByCurrentThread()) {
                        log.info(tName + " release resource B");
                        B.unlock();
                    }
                }
            }
        }, "Thread-B").start();
    }

}
