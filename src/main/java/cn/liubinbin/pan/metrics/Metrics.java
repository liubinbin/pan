package cn.liubinbin.pan.metrics;

import cn.liubinbin.pan.module.OpEnum;
import javafx.concurrent.ScheduledService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by bin on 2019/5/20.
 */
public class Metrics {

    private LatencyMetrics latencyMetrics;
    private QpsMetrics qpsMetrics;

    public Metrics() {
        this.latencyMetrics = new LatencyMetrics();
        this.qpsMetrics = new QpsMetrics();
    }

    public void addOpMetrics(OpEnum op, long latency) {
        System.out.println("addOpMetrics");
        this.latencyMetrics.add(op, latency);
        this.qpsMetrics.inc(op);
    }

    class MetricsShowTask implements Runnable {

        @Override
        public void run() {
            // latency
            System.out.println("latencyMetrics get latency " + latencyMetrics.getHistogramStr(OpEnum.GET));
            System.out.println("latencyMetrics put latency " + latencyMetrics.getHistogramStr(OpEnum.PUT));
            System.out.println("latencyMetrics delete latency " + latencyMetrics.getHistogramStr(OpEnum.DELETE));
            System.out.println("latencyMetrics all latency " + latencyMetrics.getHistogramStr(OpEnum.ALL));
            // qps
            System.out.println("qpsMetrics " + qpsMetrics.getQpsStr());
        }
    }

    public void start() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        MetricsShowTask metricsShowTask = new MetricsShowTask();
        scheduledExecutorService.scheduleAtFixedRate(metricsShowTask, 1, 5, TimeUnit.SECONDS);
    }
}
