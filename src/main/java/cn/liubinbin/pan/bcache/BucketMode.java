package cn.liubinbin.pan.bcache;

public enum BucketMode {

    BYTE_ARRAY((byte) 0), HEAP_BYTEBUFFER((byte) 1), DIRECT_BYTEBUFFER((byte) 2);

    private final byte value;

    BucketMode(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}