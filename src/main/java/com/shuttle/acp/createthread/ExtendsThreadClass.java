package com.shuttle.acp.createthread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: Shuttle
 * @description: 线程创建方式一：继承 Thread 类
 */
@Slf4j
public class ExtendsThreadClass extends Thread {

    private static final int COUNT = 3;

    private static int threadNo = 1;

    public static void main(String[] args) {
        for (int i = 0; i < COUNT; i++) {
            new ExtendsThreadStaticClass().start();
        }
    }

    static class ExtendsThreadStaticClass extends Thread {

        ExtendsThreadStaticClass() {
            super("ExtendsThreadStaticClass-" + threadNo++);
        }

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
