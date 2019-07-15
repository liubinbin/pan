package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.conf.Config;
import cn.liubinbin.pan.exceptions.ChunkTooManyException;
import cn.liubinbin.pan.exceptions.DataTooBiglException;
import cn.liubinbin.pan.exceptions.SlotBiggerThanChunkException;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * Created by bin on 2019/4/24.
 */
public class ChunkPool {

    private Slab[] slabsInPool;
    private int[] slotSizes;
    private int biggestSlotSize;
    private int slabSize;
    private int slabMaxCount;
    private AtomicInteger chunkCurrCount;
    private sun.misc.Unsafe unsafe;
    private int NBASE;
    private int NSHIFT;

    public ChunkPool(Config cacheConfig) throws SlotBiggerThanChunkException {
        this.slotSizes = cacheConfig.getSlotSizes();
        this.slabSize = cacheConfig.getSlabSize();
        this.slabMaxCount = cacheConfig.getSlabMaxCount();
        this.chunkCurrCount = new AtomicInteger(0);
        this.slabsInPool = new ByteArraySlab[slotSizes.length];
        for (int chunkIdx = 0; chunkIdx < slotSizes.length; chunkIdx++) {
            this.slabsInPool[chunkIdx] = null;
        }
        this.biggestSlotSize = slotSizes[slotSizes.length - 1];

        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Class nc = Slab[].class;
        NBASE = unsafe.arrayBaseOffset(nc);
        int ns = unsafe.arrayIndexScale(nc);
        NSHIFT = 31 - Integer.numberOfLeadingZeros(ns);
    }

    public Slab getChunkByIdx(int idx){
        return (Slab)unsafe.getObjectVolatile(slabsInPool, (long) ((idx << NSHIFT) + NBASE));
    }

    public boolean casChunkByIdx(int idx, Slab expected, Slab update){
        return unsafe.compareAndSwapObject(slabsInPool, (long) ((idx << NSHIFT) + NBASE), expected, update);
    }

    // TODO we should use datalen( keylength + valuelength)
    public Slab allocate(int size) throws DataTooBiglException, ChunkTooManyException {
        if (chunkCurrCount.get() > slabMaxCount){
            throw new ChunkTooManyException();
        }
        int choosenChunkIdx = chooseChunkIdx(size);
        if (choosenChunkIdx < 0) {
            throw new DataTooBiglException("object is too big, biggestSlotSize is " + biggestSlotSize);
        }
        Slab chunk = new ByteArraySlab(getSlotSizeForIdx(choosenChunkIdx), slabSize);
        addChunk(choosenChunkIdx, chunk);
        return chunk;
    }

    public void addChunk(int hashKey, Slab update) {
        if (getChunkByIdx(hashKey) == null) {
            if (casChunkByIdx(hashKey, null, update)) {
                return;
            }
        }
        Slab expected = getChunkByIdx(hashKey);
        update.setNext(expected);
        while (!casChunkByIdx(hashKey, expected, update)) {
            expected = getChunkByIdx(hashKey);
            update.setNext(expected);
        }
    }

    public int chooseChunkIdx(int size) {
        for (int chunkIdx = 0; chunkIdx < slotSizes.length; chunkIdx++) {
            if (size < slotSizes[chunkIdx]) {
                return chunkIdx;
            }
        }
        return -1;
    }

    public int getSlotSizeForIdx(int idx) {
        return slotSizes[idx];
    }

}
