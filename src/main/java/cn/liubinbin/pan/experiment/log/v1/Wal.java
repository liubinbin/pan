package main.java.cn.liubinbin.pan.experiment.log.v1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Wal {
	
	private final int DATA_CHUNK = 128 * 1024 * 1024;
	private final byte[] DATA = new byte[DATA_CHUNK];
	private FileChannel fileChannel;
	private RandomAccessFile randomAccessFile;
	
	public Wal(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		
		try {
			this.randomAccessFile = new RandomAccessFile(file, "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.fileChannel = randomAccessFile.getChannel();
	}

	public void append(ByteBuffer byteBuffer){
		try {
			fileChannel.write(byteBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void flush(int sequence){
		if (sequence % 50 == 0){
			try {
				fileChannel.force(true);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void close() {
		try {
			randomAccessFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fileChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
