package cn.liubinbin.experiment.binfile.filev1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author liubinbin
 */
public class BinFile {

    private String filePath;
    private int postion;
    private RandomAccessFile randomAccessFile;
    private FileChannel fileChannel;

    public BinFile(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.postion = 0;
        File file = new File(filePath);

        this.randomAccessFile = new RandomAccessFile(file, "rw");
        this.fileChannel = randomAccessFile.getChannel();
    }

    public void writeTo(KeyValue keyValue) throws IOException {
        // key size - 4
        // value size - 4
        // ts (long) - 8
        // key - key.size
        // value - value.size
        int KVSizeInByte = 16 + keyValue.getKey().length + keyValue.getValue().length;
        ByteBuffer byteBuffer = ByteBuffer.allocate(KVSizeInByte);
        byteBuffer.clear();
        byteBuffer.putInt(keyValue.getKey().length);
        byteBuffer.putInt(keyValue.getValue().length);
        byteBuffer.putLong(keyValue.getTimestamp());
        byteBuffer.put(keyValue.getKey());
        byteBuffer.put(keyValue.getValue());
        byteBuffer.flip();
        fileChannel.write(byteBuffer);
    }

    public KeyValue readFrom(int postion) throws IOException {
        // deal with meta
        ByteBuffer meta = ByteBuffer.allocate(16);
        fileChannel.read(meta, postion);
        meta.flip();
        int keySize = meta.getInt();
        int valueSize = meta.getInt();
        long timestamp = meta.getLong();

        // deal with actulData
        ByteBuffer actulData = ByteBuffer.allocate(keySize + valueSize);
        fileChannel.read(actulData, 16);
        actulData.flip();
        byte[] key = new byte[keySize];
        byte[] value = new byte[valueSize];
        actulData.get(key);
        actulData.get(value);
        return new KeyValue(key, value, timestamp);
    }

    public void flush() throws IOException {
        fileChannel.force(true);
    }

    public void close() throws IOException {
        fileChannel.force(true);
        fileChannel.close();
        randomAccessFile.close();
    }
}
