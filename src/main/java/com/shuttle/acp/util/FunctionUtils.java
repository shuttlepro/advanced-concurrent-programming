package com.shuttle.acp.util;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * @author: Shuttle
 * @description: FunctionUtils
 */
@Slf4j
public class FunctionUtils {

    public static <T> void callAndPrintTimeConsumption(Supplier<T> supplier) {
        long startTime = System.currentTimeMillis();
        log.info("startTime:{}", startTime);

        T result = supplier.get();
        log.info("result:{}", result);

        long endTime = System.currentTimeMillis();
        log.info("endTime:{}, spendTime:{} ms", endTime, endTime - startTime);
    }

}
