package cn.liubinbin.pan.jmx;

/**
 * Created by bin on 2019/5/22.
 */
public interface DeleteLatencyMBean {

    public long getDeleteLatency50th();

    public long getDeleteLatency90th();

    public long getDeleteLatency95th();

    public long getDeleteLatency99th();

    public long getDeleteLatency999th();

}
