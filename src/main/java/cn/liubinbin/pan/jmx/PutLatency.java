package cn.liubinbin.pan.jmx;

/**
 * Created by bin on 2019/5/22.
 */
public class PutLatency implements PutLatencyMBean {

    private volatile long putLatency50th;
    private volatile long putLatency90th;
    private volatile long putLatency95th;
    private volatile long putLatency99th;
    private volatile long putLatency999th;

    @Override
    public long getPutLatency50th() {
        return putLatency50th;
    }

    public void setPutLatency50th(long putLatency50th) {
        this.putLatency50th = putLatency50th;
    }

    @Override
    public long getPutLatency90th() {
        return putLatency90th;
    }

    public void setPutLatency90th(long putLatency90th) {
        this.putLatency90th = putLatency90th;
    }

    @Override
    public long getPutLatency95th() {
        return putLatency95th;
    }

    public void setPutLatency95th(long putLatency95th) {
        this.putLatency95th = putLatency95th;
    }

    @Override
    public long getPutLatency99th() {
        return putLatency99th;
    }

    public void setPutLatency99th(long putLatency99th) {
        this.putLatency99th = putLatency99th;
    }

    @Override
    public long getPutLatency999th() {
        return putLatency999th;
    }

    public void setPutLatency999th(long putLatency999th) {
        this.putLatency999th = putLatency999th;
    }
}
