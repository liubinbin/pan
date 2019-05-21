package cn.liubinbin.pan.metrics;

import cn.liubinbin.experiment.histogram.FastLongHistogram;
import cn.liubinbin.pan.module.OpEnum;

/**
 * three methods
 * GET
 * PUT
 * DELETE
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

    public void add(OpEnum op, long latency) {
        allHistogram.add(latency, 1);
        if (op == OpEnum.GET) {
            getHistogram.add(latency, 1);
        } else if (op == OpEnum.PUT) {
            putHistogram.add(latency, 1);
        }
        if (op == OpEnum.DELETE) {
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
        }
        if (op == OpEnum.DELETE) {
            return deleteHistogram;
        } else if (op == OpEnum.ALL) {
            return allHistogram;
        } else {
            // skip
            return null;
        }
    }

    public void getHisToServerLoad(ServerLoad serverLoad) {
        long[] histogram = getHistogram(OpEnum.GET).getQuantiles(CUSTOM_QUANTILES);
        serverLoad.setGetLatency50th(histogram[0]);
        serverLoad.setGetLatency90th(histogram[1]);
        serverLoad.setGetLatency95th(histogram[2]);
        serverLoad.setGetLatency99th(histogram[3]);
        serverLoad.setGetLatency999th(histogram[4]);
    }

    public void putHisToServerLoad(ServerLoad serverLoad) {
        long[] histogram = getHistogram(OpEnum.PUT).getQuantiles(CUSTOM_QUANTILES);
        serverLoad.setPutLatency50th(histogram[0]);
        serverLoad.setPutLatency90th(histogram[1]);
        serverLoad.setPutLatency95th(histogram[2]);
        serverLoad.setPutLatency99th(histogram[3]);
        serverLoad.setPutLatency999th(histogram[4]);
    }

    public void deleteHisToServerLoad(ServerLoad serverLoad) {
        long[] histogram = getHistogram(OpEnum.DELETE).getQuantiles(CUSTOM_QUANTILES);
        serverLoad.setDeleteLatency50th(histogram[0]);
        serverLoad.setDeleteLatency90th(histogram[1]);
        serverLoad.setDeleteLatency95th(histogram[2]);
        serverLoad.setDeleteLatency99th(histogram[3]);
        serverLoad.setDeleteLatency999th(histogram[4]);
    }

    public void allHisToServerLoad(ServerLoad serverLoad) {
        long[] histogram = getHistogram(OpEnum.ALL).getQuantiles(CUSTOM_QUANTILES);
        serverLoad.setAllLatency50th(histogram[0]);
        serverLoad.setAllLatency90th(histogram[1]);
        serverLoad.setAllLatency95th(histogram[2]);
        serverLoad.setAllLatency99th(histogram[3]);
        serverLoad.setAllLatency999th(histogram[4]);
    }


}
