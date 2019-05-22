package cn.liubinbin.pan.jmx;

/**
 * Created by bin on 2019/5/22.
 */
public class DeleteLatency implements DeleteLatencyMBean {

    private volatile long deleteLatency50th;
    private volatile long deleteLatency90th;
    private volatile long deleteLatency95th;
    private volatile long deleteLatency99th;
    private volatile long deleteLatency999th;

    @Override
    public long getDeleteLatency50th() {
        return deleteLatency50th;
    }

    public void setDeleteLatency50th(long deleteLatency50th) {
        this.deleteLatency50th = deleteLatency50th;
    }

    @Override
    public long getDeleteLatency90th() {
        return deleteLatency90th;
    }

    public void setDeleteLatency90th(long deleteLatency90th) {
        this.deleteLatency90th = deleteLatency90th;
    }

    @Override
    public long getDeleteLatency95th() {
        return deleteLatency95th;
    }

    public void setDeleteLatency95th(long deleteLatency95th) {
        this.deleteLatency95th = deleteLatency95th;
    }

    @Override
    public long getDeleteLatency99th() {
        return deleteLatency99th;
    }

    public void setDeleteLatency99th(long deleteLatency99th) {
        this.deleteLatency99th = deleteLatency99th;
    }

    @Override
    public long getDeleteLatency999th() {
        return deleteLatency999th;
    }

    public void setDeleteLatency999th(long deleteLatency999th) {
        this.deleteLatency999th = deleteLatency999th;
    }
}
