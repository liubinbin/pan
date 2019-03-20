package main.java.cn.liubinbin.experiment.binfile.filev1;

import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author liubinbin
 *
 */
public class BFile {
	
	private String filePath;
	private int postion;
	
	public BFile(String filePath) throws FileNotFoundException {
		this.filePath = filePath;
		this.postion = 0;
		File file = new File(filePath);
		
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
		FileChannel fileChannel = randomAccessFile.getChannel();
		
	}
	
	public void writeTo(KeyValue keyValue) {
		// key size
		// value size
		// ts (long)
		// key
		// value
		
	}
	
	public void readFrom(int postion) {
		
	}
	

	public static void main(String[] args) throws IOException {
		String filePath = "testfile";
		File file = new File(filePath);
//		if (file.exists()) {
//			file.delete();
//		}
		
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
		FileChannel fileChannel = randomAccessFile.getChannel();
		fileChannel.position(5);
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(5);
		for(int i = 0; i < 10; i++) {
			System.out.println("pos: " + fileChannel.position());
			byteBuffer.clear();
			byteBuffer.put("12345".getBytes());
			
			byteBuffer.flip();
			fileChannel.write(byteBuffer);
		}
		
		fileChannel.force(true);
		fileChannel.close();
		randomAccessFile.close();
	}
}
