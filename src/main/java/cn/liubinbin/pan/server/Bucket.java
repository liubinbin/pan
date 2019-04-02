package cn.liubinbin.pan.server;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author liubinbin
 */
public class Bucket {

	private int slotsize;
	private byte[] data;
	private volatile int writeIdx;
	private ReentrantReadWriteLock readWriteLock;
	private Lock rLock;
	private Lock wLock;
	private int dataTotalSize;

	public Bucket(int slotSize, int segmentSize) {
		this.slotsize = slotSize;
		this.data = new byte[segmentSize];
		this.writeIdx = 0;
		this.readWriteLock = new ReentrantReadWriteLock();
		this.rLock = readWriteLock.readLock();
		this.wLock = readWriteLock.writeLock();
		this.dataTotalSize = 0;
	}

	public byte[] getByByteArray(int offset, int length) {
		rLock.lock();
		try {
			byte[] value = new byte[length];
			System.arraycopy(data, offset, value, 0, length);
			return value;
		} finally {
			rLock.unlock();
		}
	}

	public ByteBuf getByByteBuf(int offset, int length) {
		rLock.lock();
		try {
			ByteBuf value = Unpooled.wrappedBuffer(data, offset, length);
			return value;
		} finally {
			rLock.unlock();
		}
	}

	/**
	 * TODO shoule use rLock ? 
	 * @param value
	 * @return
	 */
	public int put(byte[] value) {
		wLock.lock();
		try {
			int offset = writeIdx;
			System.arraycopy(value, 0, data, writeIdx, value.length);
			writeIdx += value.length;
			dataTotalSize += value.length;
			return offset;
		} finally {
			wLock.unlock();
		}
	}

	public void delete(byte[] value, int offset, int length) {
		wLock.lock();
		try {
			dataTotalSize -= length;
		} finally {
			wLock.unlock();
		}
	}
	
	public boolean checkWriteForLen(int length) {
		return (writeIdx + length > slotsize) ? false : true;
	}

	public int getWriteIdx() {
		return writeIdx;
	}

	public int getdataTotalSize() {
		return dataTotalSize;
	}
}
