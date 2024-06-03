package com.shuttle.acp.utils;

import java.util.Random;

/**
 * @author: Shuttle
 * @description: SleepUtils
 */
public class SleepUtils {

    private static final Random RANDOM = new Random();

    /**
     * 睡眠指定 millis 毫秒
     *
     * @param millis 毫秒值
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 随机睡眠 [min, max] 毫秒
     *
     * @param min 最小毫秒值
     * @param max 最大毫秒值
     */
    public static void sleepRandom(int min, int max) {
        try {
            Thread.sleep(RANDOM.nextInt(max - min + 1) + min);
        } catch (InterruptedException e) {
            // 被打断时需要重置标志位
            Thread.interrupted();
        }
    }

}
