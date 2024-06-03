package com.shuttle.acp.createthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author: Shuttle
 * @description: 线程创建方式三：实现 Callable 接口配合使用 FutureTask
 */
@Slf4j
public class ImplCallableInterface {

    private static final int COUNT = 3;

    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= COUNT; i++) {
            ImplCallableInterfaceStaticClass callable = new ImplCallableInterfaceStaticClass();
            FutureTask<String> futureTask = new FutureTask<>(callable);
            Thread thread = new Thread(futureTask);
            thread.setName("Thread-" + i);
            thread.start();
            // 阻塞获取结果
//            log.info(futureTask.get());
        }
    }

    static class ImplCallableInterfaceStaticClass implements Callable<String> {

        @Override
        public String call() {
            String tName = Thread.currentThread().getName();
            for (int i = 1; i <= COUNT; i++) {
                log.info(tName + " running...");
            }
            log.info(tName + " run complete...");

            return tName + " call return";
        }

    }

}
