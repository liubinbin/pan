package cn.liubinbin.pan.jmx;

/**
 * Created by bin on 2019/5/22.
 */
public interface AllLatencyMBean {

    public long getAllLatency50th();

    public long getAllLatency90th();

    public long getAllLatency95th();

    public long getAllLatency99th();

    public long getAllLatency999th();

}
