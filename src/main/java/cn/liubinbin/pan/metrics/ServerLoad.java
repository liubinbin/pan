package cn.liubinbin.pan.metrics;

import cn.liubinbin.pan.jmx.*;

/**
 * Created by bin on 2019/5/21.
 */
public class ServerLoad {

    private Qps qps;
    private GetLatency getLatency;
    private PutLatency putLatency;
    private DeleteLatency deleteLatency;
    private AllLatency allLatency;

    public ServerLoad() {
        this.qps = new Qps();
        this.getLatency = new GetLatency();
        this.putLatency = new PutLatency();
        this.deleteLatency = new DeleteLatency();
        this.allLatency = new AllLatency();
    }

    public String getQpsStr() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("qps ");
        stringBuilder.append(" [ get: ");
        stringBuilder.append(getGetQps());

        stringBuilder.append(" ], [ put: ");
        stringBuilder.append(getPutQps());

        stringBuilder.append(" ], [ delete: ");
        stringBuilder.append(getDeleteQps());

        stringBuilder.append(" ], [ all: ");
        stringBuilder.append(getAllQps());

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

    public Qps getQps() {
        return qps;
    }

    public GetLatency getGetLatency() {
        return getLatency;
    }

    public PutLatency getPutLatency() {
        return putLatency;
    }

    public DeleteLatency getDeleteLatency() {
        return deleteLatency;
    }

    public AllLatency getAllLatency() {
        return allLatency;
    }

    public long getGetQps() {
        return qps.getGetQps();
    }

    public void setGetQps(long getQps) {
        qps.setGetQps(getQps);
    }

    public long getPutQps() {
        return qps.getPutQps();
    }

    public void setPutQps(long putQps) {
        qps.setPutQps(putQps);
    }

    public long getDeleteQps() {
        return qps.getDeleteQps();
    }

    public void setDeleteQps(long deleteQps) {
        qps.setDeleteQps(deleteQps);
    }

    public long getAllQps() {
        return qps.getAllQps();
    }

    public void setAllQps(long allQps) {
        qps.setAllQps(allQps);
    }

    public long getGetLatency50th() {
        return getLatency.getGetLatency50th();
    }

    public void setGetLatency50th(long getLatency50th) {
        getLatency.setGetLatency50th(getLatency50th);
    }

    public long getGetLatency90th() {
        return getLatency.getGetLatency90th();
    }

    public void setGetLatency90th(long getLatency90th) {
        getLatency.setGetLatency90th(getLatency90th);
    }

    public long getGetLatency95th() {
        return getLatency.getGetLatency95th();
    }

    public void setGetLatency95th(long getLatency95th) {
        getLatency.setGetLatency95th(getLatency95th);
    }

    public long getGetLatency99th() {
        return getLatency.getGetLatency99th();
    }

    public void setGetLatency99th(long getLatency99th) {
        getLatency.setGetLatency99th(getLatency99th);
    }

    public long getGetLatency999th() {
        return getLatency.getGetLatency999th();
    }

    public void setGetLatency999th(long getLatency999th) {
        getLatency.setGetLatency999th(getLatency999th);
    }

    public long getPutLatency50th() {
        return putLatency.getPutLatency50th();
    }

    public void setPutLatency50th(long putLatency50th) {
        putLatency.setPutLatency50th(putLatency50th);
    }

    public long getPutLatency90th() {
        return putLatency.getPutLatency90th();
    }

    public void setPutLatency90th(long putLatency90th) {
        putLatency.setPutLatency90th(putLatency90th);
    }

    public long getPutLatency95th() {
        return putLatency.getPutLatency95th();
    }

    public void setPutLatency95th(long putLatency95th) {
        putLatency.setPutLatency95th(putLatency95th);
    }

    public long getPutLatency99th() {
        return putLatency.getPutLatency99th();
    }

    public void setPutLatency99th(long putLatency99th) {
        putLatency.setPutLatency99th(putLatency99th);
    }

    public long getPutLatency999th() {
        return putLatency.getPutLatency999th();
    }

    public void setPutLatency999th(long putLatency999th) {
        putLatency.setPutLatency999th(putLatency999th);
    }

    public long getDeleteLatency50th() {
        return deleteLatency.getDeleteLatency50th();
    }

    public void setDeleteLatency50th(long deleteLatency50th) {
        deleteLatency.setDeleteLatency50th(deleteLatency50th);
    }

    public long getDeleteLatency90th() {
        return deleteLatency.getDeleteLatency90th();
    }

    public void setDeleteLatency90th(long deleteLatency90th) {
        deleteLatency.setDeleteLatency90th(deleteLatency90th);
    }

    public long getDeleteLatency95th() {
        return deleteLatency.getDeleteLatency95th();
    }

    public void setDeleteLatency95th(long deleteLatency95th) {
        deleteLatency.setDeleteLatency95th(deleteLatency95th);
    }

    public long getDeleteLatency99th() {
        return deleteLatency.getDeleteLatency99th();
    }

    public void setDeleteLatency99th(long deleteLatency99th) {
        deleteLatency.setDeleteLatency99th(deleteLatency99th);
    }

    public long getDeleteLatency999th() {
        return deleteLatency.getDeleteLatency999th();
    }

    public void setDeleteLatency999th(long deleteLatency999th) {
        deleteLatency.setDeleteLatency999th(deleteLatency999th);
    }

    public long getAllLatency50th() {
        return allLatency.getAllLatency50th();
    }

    public void setAllLatency50th(long allLatency50th) {
        allLatency.setAllLatency50th(allLatency50th);
    }

    public long getAllLatency90th() {
        return allLatency.getAllLatency90th();
    }

    public void setAllLatency90th(long allLatency90th) {
        allLatency.setAllLatency90th(allLatency90th);
    }

    public long getAllLatency95th() {
        return allLatency.getAllLatency95th();
    }

    public void setAllLatency95th(long allLatency95th) {
        allLatency.setAllLatency95th(allLatency95th);
    }

    public long getAllLatency99th() {
        return allLatency.getAllLatency99th();
    }

    public void setAllLatency99th(long allLatency99th) {
        allLatency.setAllLatency99th(allLatency99th);
    }

    public long getAllLatency999th() {
        return allLatency.getAllLatency999th();
    }

    public void setAllLatency999th(long allLatency999th) {
        allLatency.setAllLatency999th(allLatency999th);
    }
}
