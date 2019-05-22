package cn.liubinbin.pan.jmx;

/**
 * Created by bin on 2019/5/22.
 */
public interface QpsMBean {

    public long getGetQps();
    public long getPutQps();
    public long getDeleteQps();
    public long getAllQps();
}
