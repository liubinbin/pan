package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.exceptions.BucketIsFullException;
import io.netty.buffer.ByteBuf;

/**
 * @author liubinbin
 */
public abstract class Chunk {

    private int slotsize;
    private int chunkSize;
    private byte status; // opening for put; being source of compact; being target of compact

    public Chunk(int slotSize, int chunkSize) {
        this.slotsize = slotSize;
        this.chunkSize = chunkSize;
    }

    public abstract byte[] getByByteArray(byte[] key);

    public abstract ByteBuf getByByteBuf(byte[] key);

    /**
     * @param value
     * @return
     */
    public abstract int put(byte[] key, byte[] value) throws BucketIsFullException;

    public abstract void delete(byte[] key);

    public int getSlotsize() {
        return slotsize;
    }

    public int getChunkSize() {
        return chunkSize;
    }
}
