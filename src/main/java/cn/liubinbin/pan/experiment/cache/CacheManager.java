package main.java.cn.liubinbin.pan.experiment.cache;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.configuration2.ex.ConfigurationException;

import io.netty.buffer.ByteBuf;
import main.java.cn.liubinbin.pan.conf.CacheConfig;

/**
 *
 * @author liubinbin
 *
 */

public class CacheManager {

	private ConcurrentSkipListMap<Key, Addr> index;
	// TODO do we need volatile, we have already lock.
	private Bucket[] buckets;
	private int[] bucketSlotSize;
	private ReentrantReadWriteLock readWriteLock;
	private Lock rLock;
	private Lock wLock;

	public CacheManager(CacheConfig cacheConfig) {
		index = new ConcurrentSkipListMap<Key, Addr>();
		bucketSlotSize = cacheConfig.getBucketSlotSize();
		buckets = new Bucket[bucketSlotSize.length];
		for (int bucketIdx = 0; bucketIdx < bucketSlotSize.length; bucketIdx++) {
			buckets[bucketIdx] = new Bucket(bucketSlotSize[bucketIdx], cacheConfig.getSegmentSize());
		}
		this.readWriteLock = new ReentrantReadWriteLock();
		this.rLock = readWriteLock.readLock();
		this.wLock = readWriteLock.writeLock();
	}

	public byte[] getByByteArray(byte[] key) {
		rLock.lock();
		try {
			Addr addr = index.get(new Key(key));
			// not found for key
			if (addr == null) {
				System.out.println("addr is null for key " + String.valueOf(key));
				return null;
			}
			byte[] value = buckets[addr.getBucketIdx()].getByByteArray(addr.getOffset(), addr.getLength());
			return value;
		} finally {
			rLock.unlock();
		}
	}

	public ByteBuf getByByteBuf(byte[] key) {
		rLock.lock();
		try {
			Addr addr = index.get(new Key(key));
			// not found for key
			if (addr == null) {
				return null;
			}
			ByteBuf value = buckets[addr.getBucketIdx()].getByByteBuf(addr.getOffset(), addr.getLength());
			return value;
		} finally {
			rLock.unlock();
		}
	}

	public void put(byte[] key, byte[] value) {
		rLock.lock();
		try {
			int bucketIdx = chooseBucketIdx(value.length);
			int offset = buckets[bucketIdx].put(value);
			Key key1 = new Key(key);
			Addr addr = new Addr(bucketIdx, offset, value.length);
			index.put(key1, addr);
		} finally {
			rLock.unlock();
		}
	}

	public int chooseBucketIdx(int valueLen) {
		for (int bucketIdx = 0; bucketIdx < bucketSlotSize.length; bucketIdx++) {
			if (valueLen < bucketSlotSize[bucketIdx]) {
				return bucketIdx;
			}
		}
		return -1;
	}

	public static void main(String[] args) throws FileNotFoundException, ConfigurationException, IOException {
		// byte[] key = { 'k', 'e', 'y'};
		// byte[] key1 = { 'k', 'e', 'y', '1'};
		// ConcurrentSkipListMap<Key, Addr> index = new ConcurrentSkipListMap<Key,
		// Addr>();
		// index.put(new Key(key), new Addr(1, 0, 1));
		// System.out.println("addr.toString: " + index.get(new Key(key)).toString());

		CacheManager cacheManager = new CacheManager(new CacheConfig());
		byte[] key = { 'k', 'e', 'y' };
		byte[] key1 = { 'k', 'e', 'y', '1' };
		byte[] value = { 'v', 'a', 'l', 'u', 'e' };
		byte[] value1 = { 'v', 'a', 'l', 'u', 'e', '1' };

		cacheManager.put(key, value);
		cacheManager.put(key1, value1);
		byte[] valueFromCache = cacheManager.getByByteArray(key);
		System.out.println("valueFromCache.length: " + valueFromCache.length);
		System.out.println("Arrays.equals(value, valueFromCache): " + Arrays.equals(value, valueFromCache));
	}

}
