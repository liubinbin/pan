package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.exceptions.SlabIsFullException;
import cn.liubinbin.pan.exceptions.DataTooBiglException;
import io.netty.buffer.ByteBuf;

/**
 * @author liubinbin
 */
public abstract class Slab {

    private int slotsize;
    private int slabSize;
    private byte status; // opening for put; being source of compact; being target of compact

    private Slab next;

    public Slab(int slotSize, int slabSize, Slab nextSlab) {
        System.out.println("slabSize " + slabSize + " slotSize " + slotSize);
        this.slotsize = slotSize;
        this.slabSize = slabSize;
        this.next = nextSlab;
    }

    public Slab(int slotSize, int slabSize) {
        System.out.println("slabSize " + slabSize + " slotSize " + slotSize);
        this.slotsize = slotSize;
        this.slabSize = slabSize;
        this.next = null;
    }

    public abstract byte[] getByByteArray(byte[] key);

    public abstract ByteBuf getByByteBuf(byte[] key);

    /**
     * @param value
     * @return
     */
    public abstract int put(byte[] key, byte[] value) throws SlabIsFullException, DataTooBiglException;

    public abstract void delete(byte[] key);

    public abstract boolean containKey(byte[] key);

    /**
     *
     * @param length
     * @return true for can be added data, false for full slab
     */
    public abstract boolean checkWriteForLen(int length);

    public int getSlotsize() {
        return slotsize;
    }

    public int getSlabSize() {
        return slabSize;
    }

    public Slab getNext() {
        return next;
    }

    public void setNext(Slab next) {
        this.next = next;
    }
}
