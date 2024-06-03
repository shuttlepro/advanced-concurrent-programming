package com.shuttle.acp.deadlock.solution;

import com.shuttle.acp.util.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author: Shuttle
 * @description: 利用一个资源协调者
 */
@Slf4j
public class SolutionCoordinator {

    private static final Object A = new Object();
    private static final Object B = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            String tName = Thread.currentThread().getName();
            while (true) {
                // 一次性获取所有资源
                if (Coordinator.acquire(A, B)) {
                    synchronized (A) {
                        log.info(tName + " acquire resource A");
                        SleepUtils.sleep(10);
                        synchronized (B) {
                            log.info(tName + " acquire resource B");
                        }
                        // 用完就释放
                        Coordinator.release(A, B);
                    }
                }
            }
        }, "Thread-A").start();

        new Thread(() -> {
            String tName = Thread.currentThread().getName();
            while (true) {
                if (Coordinator.acquire(A, B)) {
                    synchronized (B) {
                        log.info(tName + " acquire resource B");
                        SleepUtils.sleep(10);
                        synchronized (A) {
                            log.info(tName + " acquire resource A");
                        }
                        Coordinator.release(A, B);
                    }
                }
            }
        }, "Thread-B").start();
    }

    static class Coordinator {

        static final Set<Object> RESOURCE_COORDINATOR = new HashSet<>();

        synchronized static boolean acquire(Object... resources) {
            for (Object resource : resources) {
                if (RESOURCE_COORDINATOR.contains(resource)) {
                    return false;
                }
                RESOURCE_COORDINATOR.add(resource);
            }
            return true;
        }

        synchronized static void release(Object... resources) {
            for (Object resource : resources) {
                RESOURCE_COORDINATOR.remove(resource);
            }
        }

    }

}
