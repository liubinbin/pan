package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.conf.Config;
import cn.liubinbin.pan.exceptions.DataTooBiglException;
import cn.liubinbin.pan.exceptions.SlotBiggerThanSlabException;
import cn.liubinbin.pan.exceptions.TooManySlabsException;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * slabs are orginzed by slotsiz.
 * slabs of the same size are int the same idx of slabsInPool.
 * <p>
 * Created by bin on 2019/4/24.
 */
public class SlabPool {

    private Slab[] slabsInPool;
    private int[] slotSizes;
    private int biggestSlotSize;
    private int slabSize;
    private int slabMaxCount;
    private AtomicInteger currSlabCount;
    private sun.misc.Unsafe unsafe;
    private int NBASE;
    private int NSHIFT;

    public SlabPool(Config cacheConfig) throws SlotBiggerThanSlabException {
        this.slotSizes = cacheConfig.getSlotSizes();
        this.slabSize = cacheConfig.getSlabSize();
        this.slabMaxCount = cacheConfig.getSlabMaxCount();
        this.currSlabCount = new AtomicInteger(0);
        this.slabsInPool = new ByteArraySlab[slotSizes.length];
        for (int slabIdx = 0; slabIdx < slotSizes.length; slabIdx++) {
            this.slabsInPool[slabIdx] = null;
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

    public Slab getSlabByIdx(int idx) {
        return (Slab) unsafe.getObjectVolatile(slabsInPool, (long) ((idx << NSHIFT) + NBASE));
    }

    public boolean casSlabByIdx(int idx, Slab expected, Slab update) {
        return unsafe.compareAndSwapObject(slabsInPool, (long) ((idx << NSHIFT) + NBASE), expected, update);
    }

    // TODO we should use datalen( keylength + valuelength)
    public synchronized Slab allocate(int size) throws DataTooBiglException, TooManySlabsException {
        if (currSlabCount.get() > slabMaxCount) {
            throw new TooManySlabsException();
        }
        int choosenSlabIdx = chooseSlabIdx(size);
        if (choosenSlabIdx < 0) {
            throw new DataTooBiglException("object is too big, biggestSlotSize is " + biggestSlotSize);
        }
        Slab slab = new ByteArraySlab(getSlotSizeForIdx(choosenSlabIdx), slabSize);
        addSlab(choosenSlabIdx, slab);
        currSlabCount.getAndIncrement();
        return slab;
    }

    public void addSlab(int choosenSlabIdx, Slab update) {
        if (getSlabByIdx(choosenSlabIdx) == null) {
            if (casSlabByIdx(choosenSlabIdx, null, update)) {
                return;
            }
        }
        Slab expected = getSlabByIdx(choosenSlabIdx);
        update.setNext(expected);
        while (!casSlabByIdx(choosenSlabIdx, expected, update)) {
            expected = getSlabByIdx(choosenSlabIdx);
            update.setNext(expected);
        }
    }

    public int chooseSlabIdx(int size) {
        for (int slabIdx = 0; slabIdx < slotSizes.length; slabIdx++) {
            if (size < slotSizes[slabIdx]) {
                return slabIdx;
            }
        }
        return -1;
    }

    public int getSlotSizeForIdx(int idx) {
        return slotSizes[idx];
    }

}
