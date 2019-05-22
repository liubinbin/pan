package cn.liubinbin.pan.jmx;

/**
 * Created by bin on 2019/5/22.
 */
public interface PutLantencyMBean {

    public long getPutLatency50th();
    public long getPutLatency90th();
    public long getPutLatency95th() ;
    public long getPutLatency99th();
    public long getPutLatency999th() ;

}
