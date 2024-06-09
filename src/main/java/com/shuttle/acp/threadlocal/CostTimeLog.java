package com.shuttle.acp.threadlocal;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: Shuttle
 * @description: CostTimeLog
 */
@Slf4j
public class CostTimeLog {

    private static final String START = "start";

    private static final String LAST = "last";

    private static final ThreadLocal<Map<String, Long>> COST_TIME_RECORD =
            ThreadLocal.withInitial(CostTimeLog::initCostTimeMap);

    /**
     * 初始化 costTimeMap
     *
     * @return 带有当前时间的 costTimeMap
     */
    public static Map<String, Long> initCostTimeMap() {
        // LinkedHashMap 保证添加顺序
        Map<String, Long> costTimeMap = new LinkedHashMap<>();
        long now = System.currentTimeMillis();
        costTimeMap.put(START, now);
        costTimeMap.put(LAST, now);
        return costTimeMap;
    }

    /**
     * 记录开始
     */
    public static void recordBegin() {
        log.info("cost time record begin...startTime: {}", System.currentTimeMillis());
        // need initial startTime
        COST_TIME_RECORD.get();
    }

    /**
     * 处理耗时调用点
     *
     * @param point 调用点
     */
    public static void costPoint(String point) {
        long last = COST_TIME_RECORD.get().get(LAST);
        // 计算耗时并保存
        long now = System.currentTimeMillis();
        long cost = now - last;
        COST_TIME_RECORD.get().put(point, cost);
        // 保存最近时间供下一次使用
        COST_TIME_RECORD.get().put(LAST, now);
    }

    /**
     * 记录结束
     */
    public static void recordEnd() {
        Map<String, Long> costTimeMap = COST_TIME_RECORD.get();
        long totalCostTime = costTimeMap.get(LAST) - costTimeMap.get(START);
        COST_TIME_RECORD.remove();
        log.info("cost time record end...totalCostTime: {} ms", totalCostTime);
    }

    /**
     * 打印各个阶段耗时日志
     */
    public static void printCostLog() {
        for (Map.Entry<String, Long> entry : COST_TIME_RECORD.get().entrySet()) {
            if (START.equals(entry.getKey()) || LAST.equals(entry.getKey())) {
                continue;
            }
            log.info(entry.getKey() + " => " + entry.getValue() + " ms");
        }
    }

}
