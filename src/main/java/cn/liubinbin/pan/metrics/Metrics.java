package cn.liubinbin.pan.metrics;

import cn.liubinbin.pan.module.OpEnum;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by bin on 2019/5/20.
 */
public class Metrics {

    private LatencyMetrics latencyMetrics;
    private QpsMetrics qpsMetrics;
    private ServerLoad serverLoad;

    public Metrics() {
        this.latencyMetrics = new LatencyMetrics();
        this.qpsMetrics = new QpsMetrics();
        this.serverLoad = new ServerLoad();
    }

    public void addOpMetrics(OpEnum op, long latency) {
        this.latencyMetrics.add(op, latency);
        this.qpsMetrics.inc(op);
    }

    class MetricsShowTask implements Runnable {

        @Override
        public void run() {
            // latency
            latencyMetrics.getHisToServerLoad(serverLoad);
            latencyMetrics.putHisToServerLoad(serverLoad);
            latencyMetrics.deleteHisToServerLoad(serverLoad);
            latencyMetrics.allHisToServerLoad(serverLoad);
            // qps
            qpsMetrics.toServerLoad(serverLoad);

            // print
            System.out.println("qps " + serverLoad.getQpsStr());
            System.out.println("gethistogram " + serverLoad.getGetHistogramStr());
            System.out.println("puthistogram " + serverLoad.getPutHistogramStr());
            System.out.println("deletehistogram " + serverLoad.getDeleteHistogramStr());
        }
    }

    public void start() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        MetricsShowTask metricsShowTask = new MetricsShowTask();
        scheduledExecutorService.scheduleAtFixedRate(metricsShowTask, 1, 5, TimeUnit.SECONDS);
    }
}
