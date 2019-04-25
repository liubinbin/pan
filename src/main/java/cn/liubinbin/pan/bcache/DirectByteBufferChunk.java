package cn.liubinbin.pan.bcache;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liubinbin
 */
public class DirectByteBufferChunk extends Chunk {

    private int slotsize;
    private ByteBuffer data;
    private int nextFreeSlot;
    private byte status; // opening for put; being source of compact; being target of compact
    /*
        0 stands for haven't been added
     */
    private AtomicInteger dataTotalSize;

    public DirectByteBufferChunk(int slotSize, int chunkSize) {
        super(slotSize, chunkSize);
        this.slotsize = slotSize;
        this.data = ByteBuffer.allocateDirect(chunkSize);
        this.dataTotalSize = new AtomicInteger(0);
        this.nextFreeSlot = 0;
    }

    public byte[] getByByteArray(byte[] key) {
        byte[] value = new byte[1];
//		System.arraycopy(data, offset, value, 0, length);
        return value;
    }

    public ByteBuf getByByteBuf(byte[] key) {
//		ByteBuf value = Unpooled.wrappedBuffer(data, offset, length);
        return null;
    }

    /**
     * @param value
     * @return
     */
    public int put(byte[] key, byte[] value) {
        // find position


        // race to set totalsize


        // put data


//		int offset = writeIdx;
//		System.arraycopy(value, 0, data, writeIdx, value.length);
//		writeIdx += value.length;
//		dataTotalSize += value.length;
        return 1;
    }

    public void delete(byte[] key) {

        // find position


        // race to set header


        // race to set totalsize


    }

    public boolean containKey(byte[] key) {
        return getByByteArray(key) != null;
    }

    public boolean checkWriteForLen(int length) {
        return false;
    }

    public int getdataTotalSize() {
        return dataTotalSize.get();
    }
}
