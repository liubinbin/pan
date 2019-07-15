package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.exceptions.ChunkIsFullException;
import cn.liubinbin.pan.exceptions.DataTooBiglException;
import io.netty.buffer.ByteBuf;

/**
 * @author liubinbin
 */
public abstract class Slab {

    private int slotsize;
    private int chunkSize;
    private byte status; // opening for put; being source of compact; being target of compact

    private Slab next;

    public Slab(int slotSize, int chunkSize, Slab chunk) {
        System.out.println("chunkSize " + chunkSize + " slotSize " + slotSize);
        this.slotsize = slotSize;
        this.chunkSize = chunkSize;
        this.next = chunk;
    }

    public Slab(int slotSize, int chunkSize) {
        System.out.println("chunkSize " + chunkSize + " slotSize " + slotSize);
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
    public abstract int put(byte[] key, byte[] value) throws ChunkIsFullException, DataTooBiglException;

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

    public Slab getNext() {
        return next;
    }

    public void setNext(Slab next) {
        this.next = next;
    }
}
