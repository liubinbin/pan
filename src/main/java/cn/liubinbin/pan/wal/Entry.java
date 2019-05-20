package cn.liubinbin.pan.wal;

/**
 * Created by bin on 2019/5/20.
 */
public class Entry {

    private byte[] key;
    private String action;
    private long timestamp;

    public Entry(byte[] key, String action, long timestamp) {
        this.key = key;
        this.action = action;
        this.timestamp = timestamp;
    }
}
