package com.shuttle.acp.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * @author: Shuttle
 * @description: JVM 关闭时启动的线程
 */
@Slf4j
public class ShutdownHookThread extends Thread {

    private volatile boolean hasShutdown = false;

    private final Callable callback;

    public ShutdownHookThread(String name, Callable callback) {
        super("JVM_SHUTDOWN_HOOK(" + name + ")");
        this.callback = callback;
    }

    @Override
    public void run() {
        synchronized (this) {
            log.info(getName() + " starting...");
            if (!this.hasShutdown) {
                this.hasShutdown = true;
                long beginTime = System.currentTimeMillis();
                try {
                    this.callback.call();
                } catch (Exception e) {
                    log.error(getName() + " error: {}", e.getMessage());
                }
                long spendTime = System.currentTimeMillis() - beginTime;
                log.info(getName() + "  spendTime: {} ms", spendTime);
            }
        }
    }

}
