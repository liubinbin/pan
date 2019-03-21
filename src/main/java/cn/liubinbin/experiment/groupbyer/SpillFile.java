package main.java.cn.liubinbin.experiment.groupbyer;

import java.io.BufferedReader;
import java.io.File;
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
	private final static String SPILL_FILE_ROOT = "spill";
	
	public SpillFile(String filePrefix, int hash) throws IOException {
		this(SPILL_FILE_ROOT + File.separator + filePrefix + "-" + hash);
	}
	
	public SpillFile(String filePath) throws IOException {
		this.setFilePath(filePath);
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

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
