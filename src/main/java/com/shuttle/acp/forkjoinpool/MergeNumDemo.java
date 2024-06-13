package com.shuttle.acp.forkjoinpool;

import com.shuttle.acp.util.FunctionUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

/**
 * @author: Shuttle
 * @description: 用 ForkJoin 方式实现 1 + 2 + 3 + ... + N
 */
@Slf4j
public class MergeNumDemo {

    public static void main(String[] args) throws Exception {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MergeNumTask myForkJoinTask = new MergeNumTask(1, 1000000);
        ForkJoinTask<Integer> result = forkJoinPool.submit(myForkJoinTask);
        FunctionUtils.callAndPrintTimeConsumption(() -> {
            try {
                return result.get(); // 123 ms
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        // JDK8 提供的并行流计算比 ForkJoinPool 更快
        // 实测串行流在 end < 100000000 时比并行流更快
        FunctionUtils.callAndPrintTimeConsumption(() -> LongStream.rangeClosed(1, 1000000).parallel().sum()); // 26 ms
    }

}
