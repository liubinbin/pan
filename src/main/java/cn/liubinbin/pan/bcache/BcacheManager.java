package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.conf.Config;
import cn.liubinbin.pan.exceptions.SlabIsFullException;
import cn.liubinbin.pan.exceptions.TooManySlabsException;
import cn.liubinbin.pan.exceptions.DataTooBiglException;
import cn.liubinbin.pan.exceptions.SlotBiggerThanSlabException;
import cn.liubinbin.pan.metrics.Metrics;
import cn.liubinbin.pan.module.OpEnum;
import cn.liubinbin.pan.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import org.apache.commons.configuration2.ex.ConfigurationException;
import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author liubinbin
 */

public class BcacheManager {

    private SlabPool slabPool;
    private Slab[] slabsInManager;
    private int[] slotSizes;
    private int hashMod;
    private sun.misc.Unsafe unsafe;
    private int NBASE;
    private int NSHIFT;
    private Metrics metrics;

    public BcacheManager(Config cacheConfig) throws SlotBiggerThanSlabException {
        this.slabPool = new SlabPool(cacheConfig);
        this.hashMod = cacheConfig.getHashMod();
        this.slotSizes = cacheConfig.getSlotSizes();
        this.slabsInManager = new ByteArraySlab[hashMod];
        for (int slabIdx = 0; slabIdx < hashMod; slabIdx++) {
            this.slabsInManager[slabIdx] = null;
        }

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

    public BcacheManager(Config cacheConfig, Metrics metrics) throws SlotBiggerThanSlabException {
        this(cacheConfig);
        this.metrics = metrics;
    }

    /**
     * must have
     *
     * @param key key to location data
     * @return return data for key in byte[]
     */
    public byte[] getByByteArray(byte[] key) {
        long startTime = System.currentTimeMillis();
        int keyHashRemainder = ByteUtils.hashCodeMod(key, hashMod);
        // find slab
        Slab slab = getSlabByIdx(keyHashRemainder);
        byte[] value = null;
        while (slab != null) {
            if ((value = slab.getByByteArray(key)) != null) {
                if (metrics != null) {
                    metrics.addOpMetrics(OpEnum.GET, (System.currentTimeMillis() - startTime));
                }
                return value;
            }
            slab = slab.getNext();
        }
        if (metrics != null) {
            metrics.addOpMetrics(OpEnum.GET, (System.currentTimeMillis() - startTime));
        }
        return null;
    }

    /**
     * must have
     *
     * @param key key to location data
     * @return return data for key in ByteBuf
     */
    public ByteBuf getByByteBuf(byte[] key) {
        long startTime = System.currentTimeMillis();
        int keyHashRemainder = ByteUtils.hashCodeMod(key, hashMod);
        // find slab
        Slab slab = getSlabByIdx(keyHashRemainder);
        ByteBuf value;
        while (slab != null) {
            if ((value = slab.getByByteBuf(key)) != null) {
                if (metrics != null) {
                    metrics.addOpMetrics(OpEnum.GET, (System.currentTimeMillis() - startTime));
                }
                return value;
            }
            slab = slab.getNext();
        }
        if (metrics != null) {
            metrics.addOpMetrics(OpEnum.GET, (System.currentTimeMillis() - startTime));
        }
        return null;
    }

    /**
     * must have
     *
     * @param key key to location data
     */
    public void delete(byte[] key) {
        long startTime = System.currentTimeMillis();
        int keyHashRemainder = ByteUtils.hashCodeMod(key, hashMod);
        // find slab
        Slab slab = getSlabByIdx(keyHashRemainder);
        while (slab != null) {
            slab.delete(key);
            slab = slab.getNext();
        }
        if (metrics != null) {
            metrics.addOpMetrics(OpEnum.DELETE, (System.currentTimeMillis() - startTime));
        }
    }

    /**
     * must have
     *
     * @param key   key to location data
     * @param value data related to key
     */
    public void put(byte[] key, byte[] value) throws DataTooBiglException, TooManySlabsException {
        long startTime = System.currentTimeMillis();
        int keyHashRemainder = ByteUtils.hashCodeMod(key, hashMod);
        while (true) {
            // find slab
            Slab slab = getSlabByIdx(keyHashRemainder);
            while (slab != null) {
                if (slab.checkWriteForLen(value.length)) {
                    break;
                } else {
                    slab = slab.getNext();
                }
            }
            // if slab is null, allocate a slab
            if (slab == null) {
                slab = slabPool.allocate(value.length);
                if (slab == null) {
                    // we cannot allocate slab, maybe we have already too many slabsInManager.
                    // TODO expire item by value.length
                    continue;
                }
                addSlab(keyHashRemainder, slab);
            }

            // put data
            try {
                slab.put(key, value);
                break;
            } catch (SlabIsFullException e) {
                // just retry, do nothing
            }
        }
        if (metrics != null) {
            metrics.addOpMetrics(OpEnum.PUT, (System.currentTimeMillis() - startTime));
        }
    }

    public void addSlab(int hashKey, Slab update) {
        if (getSlabByIdx(hashKey) == null) {
            if (casSlabByIdx(hashKey, null, update)) {
                return;
            }
        }
        Slab expected = getSlabByIdx(hashKey);
        update.setNext(expected);
        while (!casSlabByIdx(hashKey, expected, update)) {
            expected = getSlabByIdx(hashKey);
            update.setNext(expected);
        }
    }

    public int chooseSlabIdx(int valueLen) {
        for (int slabIdx = 0; slabIdx < slotSizes.length; slabIdx++) {
            if (valueLen < slotSizes[slabIdx]) {
                return slabIdx;
            }
        }
        return -1;
    }

    public boolean checkContainKey(byte[] key) {
        int keyHashRemainder = ByteUtils.hashCodeMod(key, hashMod);
        Slab slab = getSlabByIdx(keyHashRemainder);
        while (slab != null) {
            if (slab.containKey(key)) {
                return true;
            }
        }
        return false;
    }

    public Slab getSlabByIdx(int idx) {
        return (Slab)unsafe.getObjectVolatile(slabsInManager, (long) ((idx << NSHIFT) + NBASE)) ;
    }

    public boolean casSlabByIdx(int idx, Slab expected, Slab update) {
        return unsafe.compareAndSwapObject(slabsInManager, (long) ((idx << NSHIFT) + NBASE), expected, update);
    }

    public static void main(String[] args) throws ConfigurationException, IOException, DataTooBiglException, TooManySlabsException, SlotBiggerThanSlabException {
        Config cacheConfig = new Config();
        BcacheManager cacheManager = new BcacheManager(cacheConfig);
        byte[] CONTENT = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] CONTENT1 = {'j', 'a', 'v', 'a', 'i', 's', 'g', 'r', 'e', 'a', 't'};
        byte[] CONTENT2 = new byte[73060];
        CONTENT2[73060 - 1] = '1';
        cacheManager.put("abcd".getBytes(), CONTENT);
        cacheManager.put("abc".getBytes(), CONTENT1);
        cacheManager.put("abcde".getBytes(), CONTENT2);
        System.out.println(cacheManager.getByByteBuf("abcd".getBytes()));
        System.out.println(cacheManager.getByByteArray("abcd".getBytes()).length);
        System.out.println(cacheManager.checkContainKey("abcd".getBytes()));
        System.out.println(cacheManager.getByByteBuf("abcd".getBytes()));
        System.out.println(cacheManager.getByByteBuf("abcdefsdfa".getBytes()));
        System.out.println(cacheManager.getByByteBuf("abcdefsdf".getBytes()));
        System.out.println(cacheManager.getByByteBuf("abcfge".getBytes()));
        System.out.println(cacheManager.getByByteBuf("ab".getBytes()));
    }

}
