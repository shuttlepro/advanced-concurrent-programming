package com.shuttle.acp.threadlocal;

import com.shuttle.acp.util.SleepUtils;

/**
 * @author: Shuttle
 * @description: ThreadLocal 实战
 */
public class ThreadLocalDemo {
    public static void main(String[] args) {
        new ThreadLocalDemo().testThreadCostTime();
    }

    /**
     * 线程方法调用的耗时
     */
    public void testThreadCostTime() {
        new Thread(() -> {
            // 开始耗时记录
            CostTimeLog.recordBegin();
            // 调用模拟业务方法
            callService();
            // 打印耗时
            CostTimeLog.printCostLog();
            // 结束耗时记录
            CostTimeLog.recordEnd();
        }).start();

        SleepUtils.sleep(10);// 等待 10s 看结果
    }

    /**
     * 模拟业务方法
     */
    public void callService() {
        SleepUtils.sleepRandom(50, 300);
        CostTimeLog.costPoint("point-1 service");
        callRpc();
        callDataBase();
    }

    /**
     * 模拟调用 RPC 接口
     */
    public void callRpc() {
        SleepUtils.sleepRandom(500, 1000);
        CostTimeLog.costPoint("point-2 rpc");
    }

    /**
     * 模拟数据库处理
     */
    public void callDataBase() {
        SleepUtils.sleepRandom(100, 1000);
        CostTimeLog.costPoint("point-3 database");
    }

}
