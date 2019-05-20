package cn.liubinbin.pan.metrics;

import cn.liubinbin.experiment.histogram.FastLongHistogram;
import cn.liubinbin.pan.module.OpEnum;

/**
 * three methods
 *  GET
 *  PUT
 *  DELETE
 * Created by bin on 2019/5/20.
 */
public class LatencyMetrics {

    public static final double[] CUSTOM_QUANTILES =
            new double[]{0.5, 0.90, 0.95, 0.99, 0.999};

    FastLongHistogram getHistogram;
    FastLongHistogram putHistogram;
    FastLongHistogram deleteHistogram;
    FastLongHistogram allHistogram;

    public LatencyMetrics() {
        this.getHistogram = new FastLongHistogram();
        this.putHistogram = new FastLongHistogram();
        this.deleteHistogram = new FastLongHistogram();
        this.allHistogram = new FastLongHistogram();
    }

    public void add(OpEnum op, long latency){
        allHistogram.add(latency, 1);
        if (op == OpEnum.GET) {
            getHistogram.add(latency, 1);
        } else if (op == OpEnum.PUT) {
            putHistogram.add(latency, 1);
        } if (op == OpEnum.DELETE) {
            deleteHistogram.add(latency, 1);
        } else {
            // skip
        }
    }

    public FastLongHistogram getHistogram(OpEnum op) {
        if (op == OpEnum.GET) {
            return getHistogram;
        } else if (op == OpEnum.PUT) {
            return putHistogram;
        } if (op == OpEnum.DELETE) {
            return deleteHistogram;
        } else if (op == OpEnum.ALL) {
            return allHistogram;
        } else {
            // skip
            return null;
        }
    }

    public String getHistogramStr(OpEnum op) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("op ").append(op.getOp());
        long[] histogram = getHistogram(op).getQuantiles(CUSTOM_QUANTILES);
        for (int i = 0; i < histogram.length; i++) {
            stringBuilder.append(" [ quantile: ").append(CUSTOM_QUANTILES[i]).append(", latency: ").append(histogram[i]).append("] ");
        }
        return stringBuilder.toString();
    }

}
