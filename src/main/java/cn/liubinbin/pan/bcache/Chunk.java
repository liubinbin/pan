package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.exceptions.ChunkIsFullException;
import io.netty.buffer.ByteBuf;

/**
 * @author liubinbin
 */
public abstract class Chunk {

    private int slotsize;
    private int chunkSize;
    private byte status; // opening for put; being source of compact; being target of compact
    private Chunk next;

    public Chunk(int slotSize, int chunkSize, Chunk chunk) {
        this.slotsize = slotSize;
        this.chunkSize = chunkSize;
        this.next = chunk;
    }

    public Chunk(int slotSize, int chunkSize) {
        this.slotsize = slotSize;
        this.chunkSize = chunkSize;
        this.next = null;
    }

    public abstract byte[] getByByteArray(byte[] key);

    public abstract ByteBuf getByByteBuf(byte[] key);

    /**
     * @param value
     * @return
     */
    public abstract int put(byte[] key, byte[] value) throws ChunkIsFullException;

    public abstract void delete(byte[] key);

    public abstract boolean containKey(byte[] key);

    /**
     *
     * @param length
     * @return true for can be added data, false for full chunk
     */
    public abstract boolean checkWriteForLen(int length);

    public int getSlotsize() {
        return slotsize;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public Chunk getNext() {
        return next;
    }
}
