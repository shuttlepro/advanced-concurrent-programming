package com.shuttle.acp.threadpool;

import com.shuttle.acp.util.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: Shuttle
 * @description: ThreadPoolApplication
 */
@Slf4j
public class ThreadPoolApplication {
    public static void main(String[] args) {
        ThreadPoolExecutor cpuIntensiveThreadPool = CPUIntensiveThreadPool.getThreadPool();
        for (int i = 0; i < 10; i++) {
            cpuIntensiveThreadPool.execute(() -> {
                for (int j = 0; j < 100; j++) {
                    SleepUtils.sleepRandom(100, 1000);
                    log.info("print: {}", j);
                }
            });
        }
        ThreadPoolExecutor ioIntensiveThreadPool = IOIntensiveThreadPool.getThreadPool();
        for (int i = 0; i < 10; i++) {
            ioIntensiveThreadPool.execute(() -> {
                for (int j = 0; j < 100; j++) {
                    SleepUtils.sleepRandom(100, 1000);
                    log.info("print: {}", j);
                }
            });
        }
    }
}
