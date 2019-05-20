package cn.liubinbin.pan.metrics;

import cn.liubinbin.experiment.histogram.FastLongHistogram;
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

    public QpsMetrics() {
        this.getCount = new LongAdder();
        this.putCount = new LongAdder();
        this.deleteCount = new LongAdder();
        this.lastCompTime = System.currentTimeMillis();
    }

    public void inc(OpEnum opEnum) {
        allCount.increment();
        if (opEnum == OpEnum.GET){
            getCount.increment();
        } else if (opEnum == OpEnum.PUT){
            putCount.increment();
        } else if (opEnum == OpEnum.DELETE){
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
        } if (op == OpEnum.DELETE) {
            return deleteCount;
        } else if (op == OpEnum.ALL) {
            return allCount;
        } else {
            // skip
        }
        return null;
    }

    public String getQpsStr(OpEnum op) {
//        if (op == OpEnum.GET) {
//            getCount.sum();
//        } else if (op == OpEnum.PUT) {
//            return putCount;
//        } if (op == OpEnum.DELETE) {
//            return deleteCount;
//        } else if (op == OpEnum.ALL) {
//            return allCount;
//        } else {
//            // skip
//        }
        return null;
    }
}
