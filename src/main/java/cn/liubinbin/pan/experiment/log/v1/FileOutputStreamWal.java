package main.java.cn.liubinbin.pan.experiment.log.v1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOutputStreamWal {
	
	private FileOutputStream fileOutputStream;
	
	public FileOutputStreamWal(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		
		try {
			this.fileOutputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public synchronized void append(byte[] date){
		try {
			fileOutputStream.write(date);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void flush(int sequence){
//		if (sequence % 50 == 0){
			try {
				fileOutputStream.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
//		}
	}

	public synchronized void close() {
		try {
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
