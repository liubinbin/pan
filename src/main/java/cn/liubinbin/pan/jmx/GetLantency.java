package cn.liubinbin.pan.jmx;

/**
 * Created by bin on 2019/5/22.
 */
public class GetLantency implements GetLantencyMBean {

    private volatile long getLatency50th;
    private volatile long getLatency90th;
    private volatile long getLatency95th;
    private volatile long getLatency99th;
    private volatile long getLatency999th;

    @Override
    public long getGetLatency50th() {
        return getLatency50th;
    }

    public void setGetLatency50th(long getLatency50th) {
        this.getLatency50th = getLatency50th;
    }

    @Override
    public long getGetLatency90th() {
        return getLatency90th;
    }

    public void setGetLatency90th(long getLatency90th) {
        this.getLatency90th = getLatency90th;
    }

    @Override
    public long getGetLatency95th() {
        return getLatency95th;
    }

    public void setGetLatency95th(long getLatency95th) {
        this.getLatency95th = getLatency95th;
    }

    @Override
    public long getGetLatency99th() {
        return getLatency99th;
    }

    public void setGetLatency99th(long getLatency99th) {
        this.getLatency99th = getLatency99th;
    }

    @Override
    public long getGetLatency999th() {
        return getLatency999th;
    }

    public void setGetLatency999th(long getLatency999th) {
        this.getLatency999th = getLatency999th;
    }
}
