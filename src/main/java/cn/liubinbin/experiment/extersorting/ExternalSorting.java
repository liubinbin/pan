package main.java.cn.liubinbin.experiment.extersorting;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * 
 * @author liubinbin
 *
 */
public class ExternalSorting extends RecursiveTask<String> {

	/**
	 * @param THREE
	 *            this number means 11, to help get mod
	 */
	private static final long serialVersionUID = 1L;
	private static final int THRESHOLD = 60;
	private static final int INTEGER_SIZE = 4;
	private static final int THREE = 3;

	private String filePath;
	private long startOffset;
	private long endOffset;
	private int depth;
	private String ext;

	public ExternalSorting(String filePath, long startOffset, long endOffset, int depth) {
		this(filePath, startOffset, endOffset, depth, "");
	}

	public ExternalSorting(String filePath, long startOffset, long endOffset, int depth, String ext) {
		this.filePath = filePath;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.depth = depth;
		this.ext = ext;
	}

	@Override
	protected String compute() {
		String resultFilePath = "";
		ext += ("#" + depth);
		if (endOffset - startOffset <= THRESHOLD) {
			try {
				resultFilePath = sortSmallfile(filePath, startOffset, endOffset, depth, ext);
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
		} else {
			long midOffset = (startOffset + endOffset) / 2;
			ExternalSorting frontPart = new ExternalSorting(filePath, startOffset, midOffset, depth + 1, ext + "f");
			ExternalSorting backPart = new ExternalSorting(filePath, midOffset, endOffset, depth + 1, ext + "b");

			frontPart.fork();
			backPart.fork();

			String frontPartSortedFilePath = frontPart.join();
			String backPartSortedFilePath = backPart.join();

			resultFilePath = merge(filePath, frontPartSortedFilePath, backPartSortedFilePath, ext);
		}
		return resultFilePath;
	}

	public String merge(String filePath, String frontPartSortedFilePath, String backPartSortedFilePath, String ext) {
		String newFilePath = filePath + ext + "#sorted";

		RandomAccessFile frontPartSortedFile = null;
		RandomAccessFile backPartSortedFile = null;
		RandomAccessFile newDataFile = null;

		try {
			frontPartSortedFile = new RandomAccessFile(frontPartSortedFilePath, "rw");
			backPartSortedFile = new RandomAccessFile(backPartSortedFilePath, "rw");
			newDataFile = new RandomAccessFile(newFilePath, "rw");
			int frontPartTempNum = 0;
			int backPartTempNum = 0;
			boolean frontPartFileDone = false;
			boolean backPartFileDone = false;
			frontPartTempNum = frontPartSortedFile.readInt();
			backPartTempNum = backPartSortedFile.readInt();

			while (!frontPartFileDone && !backPartFileDone) {
				if (frontPartTempNum < backPartTempNum) {
					newDataFile.writeInt(frontPartTempNum);
					try {
						frontPartTempNum = frontPartSortedFile.readInt();
					} catch (Exception e) {
						frontPartFileDone = true;
					}
				} else {
					newDataFile.writeInt(backPartTempNum);
					try {
						backPartTempNum = backPartSortedFile.readInt();
					} catch (Exception e) {
						backPartFileDone = true;
					}
				}
			}
			if (!frontPartFileDone) {
				newDataFile.writeInt(frontPartTempNum);
				while (!frontPartFileDone) {
					// read frontPartFile
					try {
						frontPartTempNum = frontPartSortedFile.readInt();
						newDataFile.writeInt(frontPartTempNum);
					} catch (Exception e) {
						frontPartFileDone = true;
					}
				}
			}
			if (!backPartFileDone) {
				newDataFile.writeInt(backPartTempNum);
				while (!backPartFileDone) {
					// read backPartFile
					try {
						backPartTempNum = backPartSortedFile.readInt();
						newDataFile.writeInt(backPartTempNum);
					} catch (Exception e) {
						backPartFileDone = true;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (frontPartSortedFile != null) {
				try {
					frontPartSortedFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (backPartSortedFile != null) {
				try {
					backPartSortedFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (newDataFile != null) {
				try {
					newDataFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return newFilePath;
	}

	public static boolean check(String filePath) {
		RandomAccessFile dataFile = null;
		int tempNum = 0;
		int lastNum = Integer.MIN_VALUE;
		try {
			dataFile = new RandomAccessFile(filePath, "rw");
			while (true) {
				tempNum = dataFile.readInt();
				if (lastNum > tempNum) {
					return false;
				}
				lastNum = tempNum;
			}
		} catch (IOException e) {
			System.out.println("check done");
		} finally {
			try {
				dataFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public String sortSmallfile(String filePath, long startOffset, long endOffset, int depth, String ext)
			throws NumberFormatException, IOException {
		RandomAccessFile raf = new RandomAccessFile(filePath, "rw");

		// if startoffset if multiple of 4. we will read this number.
		// if not, we will read the first number
		long mod = startOffset & THREE;
		if (mod != 0) {
			startOffset += (INTEGER_SIZE - mod);
		}
		raf.seek(startOffset);

		List<Integer> tempNumSet = new ArrayList<Integer>();
		int number;
		while (startOffset < endOffset) {
			number = raf.readInt();
			tempNumSet.add(number);
			startOffset += INTEGER_SIZE;
		}
		raf.close();

		String newFilePath = filePath + ext + "#sorted";
		RandomAccessFile newdataFile = new RandomAccessFile(newFilePath, "rw");
		Collections.sort(tempNumSet);
		for (Integer item : tempNumSet) {
			newdataFile.writeInt(item);
			// System.out.println("newFilePath " + newFilePath + " " + item);
		}
		newdataFile.close();

		return newFilePath;
	}

	public static void print(String filePath) throws IOException {
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(filePath, "rw");
			long length = raf.length();
			long idx = 0;
			int number;
			while (idx < length) {
				number = raf.readInt();
				System.out.println(number);
			}
		} catch (Exception e) {
			System.out.println("print done");
		} finally {
			if (raf != null) {
				raf.close();
			}
		}

	}

	@Override
	public String toString() {
		return String.format("ExternalSorting {filePath: %s, startOffset: %d, endOffset: %d, depth: %d, ext: %s}",
				this.filePath, this.startOffset, this.endOffset, this.depth, this.ext);
	}

	public static void main(String[] args)
			throws InterruptedException, ExecutionException, NumberFormatException, IOException {
		String filePath = "D:\\externalsort\\tempunsorted.txt";
		File file = new File(filePath);
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		ExternalSorting externalSorting = new ExternalSorting(filePath, 0, file.length(), 0);
		Future<String> result = forkJoinPool.submit(externalSorting);
		System.out.println("sortFilePath: " + result.get());

		String sortedFilePath = "D:\\externalsort\\tempunsorted.txt#0#sorted";
		print(sortedFilePath);
		boolean checkResult = check(sortedFilePath);
		System.out.println("check result: " + checkResult);
	}
}
