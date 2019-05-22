package cn.liubinbin.pan.jmx;

/**
 * Created by bin on 2019/5/22.
 */
public class AllLatency implements AllLantencyMBean {

    private volatile long allLatency50th;
    private volatile long allLatency90th;
    private volatile long allLatency95th;
    private volatile long allLatency99th;
    private volatile long allLatency999th;

    @Override
    public long getAllLatency50th() {
        return allLatency50th;
    }

    public void setAllLatency50th(long allLatency50th) {
        this.allLatency50th = allLatency50th;
    }

    @Override
    public long getAllLatency90th() {
        return allLatency90th;
    }

    public void setAllLatency90th(long allLatency90th) {
        this.allLatency90th = allLatency90th;
    }

    @Override
    public long getAllLatency95th() {
        return allLatency95th;
    }

    public void setAllLatency95th(long allLatency95th) {
        this.allLatency95th = allLatency95th;
    }

    @Override
    public long getAllLatency99th() {
        return allLatency99th;
    }

    public void setAllLatency99th(long allLatency99th) {
        this.allLatency99th = allLatency99th;
    }

    @Override
    public long getAllLatency999th() {
        return allLatency999th;
    }

    public void setAllLatency999th(long allLatency999th) {
        this.allLatency999th = allLatency999th;
    }
}
