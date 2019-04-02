package cn.liubinbin.experiment.wal.filewriterperf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.ReadableByteChannel;
import java.util.RandomAccess;

import javax.xml.crypto.Data;

/**
 *
 * @author liubinbin
 *
 */
public class WriteBigFileComparision {

	private final long LEN = 2L * 1024 * 1024 * 1024L;
//	private final int DATA_CHUNK = 128 * 1024 * 1024;
//	private final byte[] DATA = new byte[DATA_CHUNK];
	private final int DATA_CHUNK = 10;
	private final byte[] DATA = {'h', 'e', 'l', 'l', 'o', 'w', 'o', 'r', 'l', 'd'};
	
	public void writeWithFileChannel(String filePath) throws IOException {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
		FileChannel fileChannel = randomAccessFile.getChannel();
		
		long len = LEN;
		ByteBuffer byteBuffer = ByteBuffer.allocate(DATA_CHUNK);
		while (len > 0) {
			byteBuffer.clear();
			byteBuffer.put(DATA);
			
			byteBuffer.flip();
			fileChannel.write(byteBuffer);
			
			len -= DATA_CHUNK;
		}
		
		fileChannel.force(true);
		fileChannel.close();
		randomAccessFile.close();
	}
	

	public void writeWithMappedByteBuffer(String filePath) throws IOException {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
		FileChannel fileChannel = randomAccessFile.getChannel();
		int pos = 0;
		MappedByteBuffer mappedByteBuffer = null;
		long len = LEN;
		while (len > 0) {
			mappedByteBuffer = fileChannel.map(MapMode.READ_WRITE, pos, DATA_CHUNK);
			mappedByteBuffer.put(DATA);
			len -= DATA_CHUNK;
			pos += DATA_CHUNK;
		}
		
		fileChannel.force(true);
		fileChannel.close();
		randomAccessFile.close();
	}
	
	
	public void writeWithTransferTo(String filePath) throws IOException {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
		FileChannel fileChannel = randomAccessFile.getChannel();
		
		long len = LEN;
		ByteArrayInputStream byteArrayInputStream = null;
		ReadableByteChannel fromByteChannel = null;
		int pos = 0;
		while(len > 0) {
			byteArrayInputStream = new ByteArrayInputStream(DATA);
			fromByteChannel = Channels.newChannel(byteArrayInputStream);
			
			fileChannel.transferFrom(fromByteChannel, pos, DATA_CHUNK);
			pos += DATA_CHUNK;
			len -= DATA_CHUNK;
		}
		
		fileChannel.force(true);
		fileChannel.close();
		fromByteChannel.close();
		randomAccessFile.close();
	}
	
	public void doMain(String[] args) throws IOException {
		long startTime = 0;
		startTime = System.currentTimeMillis();
		writeWithFileChannel("file1");
		System.out.println("writeWithFileChannel use " + (System.currentTimeMillis() - startTime));
		
		startTime = System.currentTimeMillis();
		writeWithMappedByteBuffer("file2");
		System.out.println("writeWithMappedByteBuffer use " + (System.currentTimeMillis() - startTime));
		
		startTime = System.currentTimeMillis();
		writeWithTransferTo("file3");
		System.out.println("writeWithTransferTo use " + (System.currentTimeMillis() - startTime));
	}
	
	public static void main(String[] args) throws IOException {
		new WriteBigFileComparision().doMain(args);
	}

}
