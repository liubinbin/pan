package cn.liubinbin.experiment.extersorting;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

/**
 * 
 * @author liubinbin
 *
 */
public class GeneraterData {

	public static void main(String[] args) throws IOException {
		String dataPath = "D:\\externalsort\\tempunsorted.txt";
		int numCount = 100;
		Random random = new Random();
		RandomAccessFile dataFile = null;
		try {
			dataFile = new RandomAccessFile(dataPath, "rw");
			// FileWriter fileWriter = new FileWriter(dataFile);
			for (int i = 0; i < numCount; i++) {
				int number = random.nextInt();
				dataFile.writeInt(number);
				System.out.println(number);
			}
		} finally {
			dataFile.close();
		}

	}

}
