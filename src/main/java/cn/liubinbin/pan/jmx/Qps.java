package cn.liubinbin.pan.jmx;

/**
 * Created by bin on 2019/5/22.
 */
public class Qps implements QpsMBean {

    private volatile long getQps;
    private volatile long putQps;
    private volatile long deleteQps;
    private volatile long allQps;

    public void setGetQps(long getQps) {
        this.getQps = getQps;
    }

    public void setPutQps(long putQps) {
        this.putQps = putQps;
    }

    public void setDeleteQps(long deleteQps) {
        this.deleteQps = deleteQps;
    }

    public void setAllQps(long allQps) {
        this.allQps = allQps;
    }

    public long getGetQps() {
        return getQps;
    }

    public long getPutQps() {
        return putQps;
    }

    public long getDeleteQps() {
        return deleteQps;
    }

    public long getAllQps() {
        return allQps;
    }
}
