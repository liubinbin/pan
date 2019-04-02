package cn.liubinbin.experiment.wal.walv1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * nThreads: 1
 * sequence		%1			%2			%4			%8
 * 				696,487		1,498,163		3,090,673		4,805,681
 * @author liubinbin
 *
 */
public class FielChannelWalV1 {
	
	private final int DATA_CHUNK = 128 * 1024 * 1024;
	private final byte[] DATA = new byte[DATA_CHUNK];
	private FileChannel fileChannel;
	private RandomAccessFile randomAccessFile;
	
	public FielChannelWalV1(String filePath) {
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
//		if (sequence % 1 == 0){
			try {
				fileChannel.force(true);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
//		}
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
