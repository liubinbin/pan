package cn.liubinbin.experiment.bytebufferusage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * there is pos, lim and cap in ByteBuffer. there is also mark
    0 <= postition <= limit <= capacity
 * Created by bin on 2019/4/17.
 */
public class ByteBufferUsage {

    public static void test1(){
        byte[] data = "abc".getBytes();
        System.out.println("data " + data.length +  " " + new String(data));
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        // java.nio.HeapByteBuffer[pos=0 lim=20 cap=20]
        System.out.println("bytebuffer1 " + byteBuffer.toString());
        byteBuffer.put(data);
        // java.nio.HeapByteBuffer[pos=3 lim=20 cap=20]
        System.out.println("bytebuffer2 " + byteBuffer.toString());
        byteBuffer.flip();
        // java.nio.HeapByteBuffer[pos=0 lim=3 cap=20]
        System.out.println("bytebuffer3 " + byteBuffer.toString());
        for (;byteBuffer.hasRemaining();) {
            System.out.println("byte from bytebuffer " + byteBuffer.get() + " " + byteBuffer.toString());
        }
        // java.nio.HeapByteBuffer[pos=3 lim=3 cap=20]
        System.out.println("bytebuffer4 " + byteBuffer.toString());
        byteBuffer.rewind();
        // java.nio.HeapByteBuffer[pos=0 lim=3 cap=20]
        System.out.println("bytebuffer5 " + byteBuffer.toString());
        for (;byteBuffer.hasRemaining();) {
            System.out.println("byte from bytebuffer " + byteBuffer.get() + " " + byteBuffer.toString());
        }
        // java.nio.HeapByteBuffer[pos=3 lim=3 cap=20]
        System.out.println("bytebuffer6 " + byteBuffer.toString());
        byteBuffer.rewind();
        byte[] dataTemp = new byte[3];
        byteBuffer.get(dataTemp);
        System.out.println("dataTemp: " + new String(dataTemp));
        System.out.println("bytebuffer7 " + byteBuffer.toString());
        byteBuffer.clear();
        // bytebuffer7 java.nio.HeapByteBuffer[pos=0 lim=20 cap=20]
        System.out.println("bytebuffer8 " + byteBuffer.toString());
    }

    public static void test3() throws FileNotFoundException, IOException{
        String pathName = "README.md";
        File file = new File(pathName);
        long size = file.length();
        MappedByteBuffer mappedByteBuffer = new RandomAccessFile(pathName, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        for (int offset = 0; offset < size; offset++) {
            byte b = mappedByteBuffer.get();
            System.out.println("b " + b);
        }
    }

    public static void test4() throws IOException {
        String path = "README.md";
        RandomAccessFile accessFile = new RandomAccessFile(path, "rw");
        FileChannel fileChannel = accessFile.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(256);
        int byteRead = fileChannel.read(byteBuffer);

        while (byteRead != -1){
            byteBuffer.flip();

            while(byteBuffer.hasRemaining()) {
                System.out.println("char " + (char)byteBuffer.get());
            }

            byteBuffer.clear();
            byteRead = fileChannel.read(byteBuffer);
            System.out.println("one round");
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println("hello ByteBuffer");
        test1();
//        test3();
//        test4();
    }
}
