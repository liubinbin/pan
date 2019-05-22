package cn.liubinbin.pan.jmx;

/**
 * Created by bin on 2019/5/22.
 */
public interface GetLantencyMBean {

    public long getGetLatency50th();
    public long getGetLatency90th();
    public long getGetLatency95th();
    public long getGetLatency99th();
    public long getGetLatency999th();

}
