package main.java.cn.liubinbin.experiment.groupbyer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author liubinbin
 *
 */
public class SpillFile {
	
	private String filePath;
	private FileWriter fileWriter;
	private BufferedReader bufferedReader;
	
	public SpillFile(String filePath) throws IOException {
		this.filePath = filePath;
		this.fileWriter = new FileWriter(filePath);
		this.bufferedReader = new BufferedReader(new FileReader(filePath));
	}

	public void add(Pair pair) throws IOException {
		fileWriter.write(pair.getKey() + " " + pair.getAggCount()+"\n");
	}
	
	public Pair next() throws IOException {
		String pairStr = bufferedReader.readLine();
		if (pairStr == null) {
			return null;
		}
		try {
			String[] pairStrSplit = pairStr.split(" ");
			return new Pair(Integer.parseInt(pairStrSplit[0]), Integer.parseInt(pairStrSplit[1]));
		} catch (Exception e) {
			return null;
		}
	}
	
	public void closeWriter() throws IOException {
		fileWriter.close();
	}
	
	public void closeReader() throws IOException {
		bufferedReader.close();
	}
}
