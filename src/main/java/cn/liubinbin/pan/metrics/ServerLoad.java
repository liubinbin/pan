package cn.liubinbin.pan.metrics;

import cn.liubinbin.pan.jmx.QpsMBean;

/**
 * Created by bin on 2019/5/21.
 */
public class ServerLoad implements QpsMBean{

    private volatile long getQps;
    private volatile long putQps;
    private volatile long deleteQps;
    private volatile long allQps;

    // new double[]{0.5, 0.90, 0.95, 0.99, 0.999};
    private volatile long getLatency50th;
    private volatile long getLatency90th;
    private volatile long getLatency95th;
    private volatile long getLatency99th;
    private volatile long getLatency999th;

    private volatile long putLatency50th;
    private volatile long putLatency90th;
    private volatile long putLatency95th;
    private volatile long putLatency99th;
    private volatile long putLatency999th;

    private volatile long deleteLatency50th;
    private volatile long deleteLatency90th;
    private volatile long deleteLatency95th;
    private volatile long deleteLatency99th;
    private volatile long deleteLatency999th;

    private volatile long allLatency50th;
    private volatile long allLatency90th;
    private volatile long allLatency95th;
    private volatile long allLatency99th;
    private volatile long allLatency999th;

    public synchronized String getQpsStr() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("qps ");
        stringBuilder.append(" [ get: ");
        stringBuilder.append(getQps);

        stringBuilder.append(" ], [ put: ");
        stringBuilder.append(putQps);

        stringBuilder.append(" ], [ delete: ");
        stringBuilder.append(deleteQps);

        stringBuilder.append(" ], [ all: ");
        stringBuilder.append(allQps);

        stringBuilder.append(" ]");
        return stringBuilder.toString();
    }

    // new double[]{0.5, 0.90, 0.95, 0.99, 0.999};
    public String getGetHistogramStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("op ").append("get");
        stringBuilder.append(" [ quantile: ").append(" 0.50 ").append(", latency: ").append(getGetLatency50th()).append("] ");
        stringBuilder.append(", [ quantile: ").append(" 0.90 ").append(", latency: ").append(getGetLatency90th()).append("] ");
        stringBuilder.append(", [ quantile: ").append(" 0.95 ").append(", latency: ").append(getGetLatency95th()).append("] ");
        stringBuilder.append(", [ quantile: ").append(" 0.99 ").append(", latency: ").append(getGetLatency99th()).append("] ");
        stringBuilder.append(", [ quantile: ").append(" 0.999 ").append(", latency: ").append(getGetLatency999th()).append("] ");
        return stringBuilder.toString();
    }

    public String getPutHistogramStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("op ").append("put");
        stringBuilder.append(" [ quantile: ").append(" 0.50 ").append(", latency: ").append(getPutLatency50th()).append("] ");
        stringBuilder.append(", [ quantile: ").append(" 0.90 ").append(", latency: ").append(getPutLatency90th()).append("] ");
        stringBuilder.append(", [ quantile: ").append(" 0.95 ").append(", latency: ").append(getPutLatency95th()).append("] ");
        stringBuilder.append(", [ quantile: ").append(" 0.99 ").append(", latency: ").append(getPutLatency99th()).append("] ");
        stringBuilder.append(", [ quantile: ").append(" 0.999 ").append(", latency: ").append(getPutLatency999th()).append("] ");
        return stringBuilder.toString();
    }

    public String getDeleteHistogramStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("op ").append("delete");
        stringBuilder.append(" [ quantile: ").append(" 0.50 ").append(", latency: ").append(getDeleteLatency50th()).append("] ");
        stringBuilder.append(", [ quantile: ").append(" 0.90 ").append(", latency: ").append(getDeleteLatency90th()).append("] ");
        stringBuilder.append(", [ quantile: ").append(" 0.95 ").append(", latency: ").append(getDeleteLatency95th()).append("] ");
        stringBuilder.append(", [ quantile: ").append(" 0.99 ").append(", latency: ").append(getDeleteLatency99th()).append("] ");
        stringBuilder.append(", [ quantile: ").append(" 0.999 ").append(", latency: ").append(getDeleteLatency999th()).append("] ");
        return stringBuilder.toString();
    }

    public String getAllHistogramStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("op ").append("all");
        stringBuilder.append(" [ quantile: ").append(" 0.50 ").append(", latency: ").append(getAllLatency50th()).append("] ");
        stringBuilder.append(", [ quantile: ").append(" 0.90 ").append(", latency: ").append(getAllLatency90th()).append("] ");
        stringBuilder.append(", [ quantile: ").append(" 0.95 ").append(", latency: ").append(getAllLatency95th()).append("] ");
        stringBuilder.append(", [ quantile: ").append(" 0.99 ").append(", latency: ").append(getAllLatency99th()).append("] ");
        stringBuilder.append(", [ quantile: ").append(" 0.999 ").append(", latency: ").append(getAllLatency999th()).append("] ");
        return stringBuilder.toString();
    }

    public long getGetQps() {
        return getQps;
    }

    public void setGetQps(long getQps) {
        this.getQps = getQps;
    }

    public long getPutQps() {
        return putQps;
    }

    public void setPutQps(long putQps) {
        this.putQps = putQps;
    }

    public long getDeleteQps() {
        return deleteQps;
    }

    public void setDeleteQps(long deleteQps) {
        this.deleteQps = deleteQps;
    }

    public long getAllQps() {
        return allQps;
    }

    public void setAllQps(long allQps) {
        this.allQps = allQps;
    }

    public long getGetLatency50th() {
        return getLatency50th;
    }

    public void setGetLatency50th(long getLatency50th) {
        this.getLatency50th = getLatency50th;
    }

    public long getGetLatency90th() {
        return getLatency90th;
    }

    public void setGetLatency90th(long getLatency90th) {
        this.getLatency90th = getLatency90th;
    }

    public long getGetLatency95th() {
        return getLatency95th;
    }

    public void setGetLatency95th(long getLatency95th) {
        this.getLatency95th = getLatency95th;
    }

    public long getGetLatency99th() {
        return getLatency99th;
    }

    public void setGetLatency99th(long getLatency99th) {
        this.getLatency99th = getLatency99th;
    }

    public long getGetLatency999th() {
        return getLatency999th;
    }

    public void setGetLatency999th(long getLatency999th) {
        this.getLatency999th = getLatency999th;
    }

    public long getPutLatency50th() {
        return putLatency50th;
    }

    public void setPutLatency50th(long putLatency50th) {
        this.putLatency50th = putLatency50th;
    }

    public long getPutLatency90th() {
        return putLatency90th;
    }

    public void setPutLatency90th(long putLatency90th) {
        this.putLatency90th = putLatency90th;
    }

    public long getPutLatency95th() {
        return putLatency95th;
    }

    public void setPutLatency95th(long putLatency95th) {
        this.putLatency95th = putLatency95th;
    }

    public long getPutLatency99th() {
        return putLatency99th;
    }

    public void setPutLatency99th(long putLatency99th) {
        this.putLatency99th = putLatency99th;
    }

    public long getPutLatency999th() {
        return putLatency999th;
    }

    public void setPutLatency999th(long putLatency999th) {
        this.putLatency999th = putLatency999th;
    }

    public long getDeleteLatency50th() {
        return deleteLatency50th;
    }

    public void setDeleteLatency50th(long deleteLatency50th) {
        this.deleteLatency50th = deleteLatency50th;
    }

    public long getDeleteLatency90th() {
        return deleteLatency90th;
    }

    public void setDeleteLatency90th(long deleteLatency90th) {
        this.deleteLatency90th = deleteLatency90th;
    }

    public long getDeleteLatency95th() {
        return deleteLatency95th;
    }

    public void setDeleteLatency95th(long deleteLatency95th) {
        this.deleteLatency95th = deleteLatency95th;
    }

    public long getDeleteLatency99th() {
        return deleteLatency99th;
    }

    public void setDeleteLatency99th(long deleteLatency99th) {
        this.deleteLatency99th = deleteLatency99th;
    }

    public long getDeleteLatency999th() {
        return deleteLatency999th;
    }

    public void setDeleteLatency999th(long deleteLatency999th) {
        this.deleteLatency999th = deleteLatency999th;
    }

    public long getAllLatency50th() {
        return allLatency50th;
    }

    public void setAllLatency50th(long allLatency50th) {
        this.allLatency50th = allLatency50th;
    }

    public long getAllLatency90th() {
        return allLatency90th;
    }

    public void setAllLatency90th(long allLatency90th) {
        this.allLatency90th = allLatency90th;
    }

    public long getAllLatency95th() {
        return allLatency95th;
    }

    public void setAllLatency95th(long allLatency95th) {
        this.allLatency95th = allLatency95th;
    }

    public long getAllLatency99th() {
        return allLatency99th;
    }

    public void setAllLatency99th(long allLatency99th) {
        this.allLatency99th = allLatency99th;
    }

    public long getAllLatency999th() {
        return allLatency999th;
    }

    public void setAllLatency999th(long allLatency999th) {
        this.allLatency999th = allLatency999th;
    }
}
