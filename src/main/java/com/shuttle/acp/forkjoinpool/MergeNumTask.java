package com.shuttle.acp.forkjoinpool;

import java.util.concurrent.RecursiveTask;

/**
 * @author: Shuttle
 * @description: MergeNumTask
 */
public class MergeNumTask extends RecursiveTask<Integer> {

    /**
     * 累加初始值
     */
    private final int start;

    /**
     * 累加结束值
     */
    private final int end;

    /**
     * 拆分阈值，如果 start 与 end 相差小于等于阈值则直接进行计算即可，不需要拆分
     */
    private static final int THRESHOLD = 2;

    public MergeNumTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        // 代表当前任务是否需要拆分的标识
        boolean isSplit = (end - start) <= THRESHOLD;
        if (isSplit) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            // 需要进行任务拆分
            int mid = (start + end) / 2;
            MergeNumTask leftTask = new MergeNumTask(start, mid);
            MergeNumTask rightTask = new MergeNumTask(mid + 1, end);
            // 执行子任务
            leftTask.fork();
            rightTask.fork();
            // 拿到子任务结果
            Integer leftResult = leftTask.join();
            Integer rightResult = rightTask.join();
            // 合并任务
            sum = leftResult + rightResult;
        }

        return sum;
    }

}
