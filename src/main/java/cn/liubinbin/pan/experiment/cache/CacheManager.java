package main.java.cn.liubinbin.pan.experiment.cache;

import java.util.concurrent.ConcurrentSkipListMap;

import io.netty.buffer.ByteBuf;
import main.java.cn.liubinbin.pan.conf.CacheConfig;

/**
 *
 * @author liubinbin
 *
 */

public class CacheManager {
	
	private ConcurrentSkipListMap<Key, Addr> index;
	private Bucket[] buckets;
	private int[] bucketSlotSize;
	
	public CacheManager(CacheConfig cacheConfig) {
		index = new ConcurrentSkipListMap<Key, Addr>();
		bucketSlotSize = cacheConfig.getBucketSlotSize();
		buckets = new Bucket[bucketSlotSize.length];
		for (int bucketIdx = 0; bucketIdx < bucketSlotSize.length; bucketIdx ++){
			buckets[bucketIdx] = new Bucket(bucketSlotSize[bucketIdx], cacheConfig.getSegmentSize());
		}
	}

	public byte[] getByByteArray(byte[] key) {
		Addr addr = index.get(new Key(key));
		// not found for key
		if (addr == null) {
			return null;
		}
		byte[] value = buckets[addr.getBucketIdx()].getByByteArray(addr.getOffset(), addr.getLength());
		return value;
	}
	
	public ByteBuf getByByteBuf(byte[] key) {
		Addr addr = index.get(new Key(key));
		// not found for key
		if (addr == null) {
			return null;
		}
		ByteBuf value = buckets[addr.getBucketIdx()].getByByteBuf(addr.getOffset(), addr.getLength());
		return value;
	}
	
	public void put(byte[] key, byte[] value) {
		int bucketIdx = chooseBucketIdx(value.length);
		int offset = buckets[bucketIdx].put(value);
		Key key1 = new Key(key);
		Addr addr = new Addr(bucketIdx, offset, value.length);
		index.put(key1, addr);
	}
	
	public int chooseBucketIdx(int valueLen) {
		for (int bucketIdx = 0; bucketIdx  < bucketSlotSize.length; bucketIdx++) {
			if (valueLen < bucketSlotSize[bucketIdx]) {
				return bucketIdx;
			}
		}
		return -1;
	}
}
