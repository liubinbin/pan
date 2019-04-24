package cn.liubinbin.pan.module;

/**
 * @author liubinbin
 */
public class Record {

    private long keySize;
    private long valueSize;
    private byte[] key;
    private byte[] value;

    public Record() {

    }

    public long getKeySize() {
        return keySize;
    }

    public void setKeySize(long keySize) {
        this.keySize = keySize;
    }

    public long getValueSize() {
        return valueSize;
    }

    public void setValueSize(long valueSize) {
        this.valueSize = valueSize;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
