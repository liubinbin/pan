package cn.liubinbin.pan.metrics;

import cn.liubinbin.pan.module.OpEnum;

import java.util.concurrent.atomic.LongAdder;

/**
 * Created by bin on 2019/5/20.
 */
public class QpsMetrics {

    private LongAdder getCount;
    private LongAdder putCount;
    private LongAdder deleteCount;
    private LongAdder allCount;
    private long lastCompTime;
    private long lastGetCount;
    private long lastPutCount;
    private long lastDeleteCount;
    private long lastAllCount;

    public QpsMetrics() {
        this.getCount = new LongAdder();
        this.putCount = new LongAdder();
        this.deleteCount = new LongAdder();
        this.allCount = new LongAdder();
        this.lastCompTime = System.currentTimeMillis();
        this.lastGetCount = 0;
        this.lastPutCount = 0;
        this.lastDeleteCount = 0;
        this.lastAllCount = 0;
    }

    public void inc(OpEnum opEnum) {
        allCount.increment();
        if (opEnum == OpEnum.GET) {
            getCount.increment();
        } else if (opEnum == OpEnum.PUT) {
            putCount.increment();
        } else if (opEnum == OpEnum.DELETE) {
            deleteCount.increment();
        } else {
            // skip
        }
    }

    public LongAdder getCount(OpEnum op) {
        if (op == OpEnum.GET) {
            return getCount;
        } else if (op == OpEnum.PUT) {
            return putCount;
        }
        if (op == OpEnum.DELETE) {
            return deleteCount;
        } else if (op == OpEnum.ALL) {
            return allCount;
        } else {
            // skip
        }
        return null;
    }

    public synchronized String getQpsStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("qps ");
        long now = System.currentTimeMillis();
        stringBuilder.append(" [ get: ");
        stringBuilder.append((getCount.sum() - lastGetCount) * 1000 / (now - lastCompTime));
        lastGetCount = getCount.sum();

        stringBuilder.append(" ], [ put: ");
        stringBuilder.append((putCount.sum() - lastPutCount) * 1000 / (now - lastCompTime));
        lastPutCount = putCount.sum();

        stringBuilder.append(" ], [ delete: ");
        stringBuilder.append((deleteCount.sum() - lastDeleteCount) * 1000 / (now - lastCompTime));
        lastDeleteCount = deleteCount.sum();

        stringBuilder.append(" ], [ all: ");
        stringBuilder.append((allCount.sum() - lastAllCount) * 1000 / (now - lastCompTime));
        lastAllCount = allCount.sum();

        stringBuilder.append(" ]");
        lastCompTime = now;

        return stringBuilder.toString();
    }
}
