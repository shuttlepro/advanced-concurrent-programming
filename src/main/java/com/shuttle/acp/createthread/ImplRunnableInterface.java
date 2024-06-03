package com.shuttle.acp.createthread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: Shuttle
 * @description: 线程创建方式二：实现 Runnable 接口
 */
@Slf4j
public class ImplRunnableInterface {

    private static final int COUNT = 3;

    public static void main(String[] args) {
        for (int i = 1; i <= COUNT; i++) {
            ImplRunnableInterfaceStaticClass runnable = new ImplRunnableInterfaceStaticClass();
            Thread thread = new Thread(runnable);
            thread.setName("Thread-" + i);
            thread.start();
        }
    }

    static class ImplRunnableInterfaceStaticClass implements Runnable {

        @Override
        public void run() {
            String tName = Thread.currentThread().getName();
            for (int i = 1; i <= COUNT; i++) {
                log.info(tName + " running...");
            }
            log.info(tName + " run complete...");
        }

    }

}
