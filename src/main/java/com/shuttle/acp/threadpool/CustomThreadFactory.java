package com.shuttle.acp.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Shuttle
 * @description: 自定义线程工厂
 */
public class CustomThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber = new AtomicInteger(1);

    private final ThreadGroup group;

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private final String threadTag;

    CustomThreadFactory(String threadTag) {
        group = Thread.currentThread().getThreadGroup();
        this.threadTag = "BizThreadPool-" + poolNumber.getAndIncrement() + "-" + threadTag + "-";
    }

    @Override
    public Thread newThread(Runnable target) {
        Thread t = new Thread(
                group,
                target,
                threadTag + threadNumber.getAndIncrement(),
                0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }

}
