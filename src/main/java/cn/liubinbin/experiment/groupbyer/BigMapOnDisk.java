package main.java.cn.liubinbin.experiment.groupbyer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author liubinbin
 * is OnDisk
 * 
 * 1. on buffer: 
 * 2. on disk: 
 * 
 * fileName: level1.hash-level2.hash-level3.hash-level4.hash
 * this file stands for source
 * 
 * 				   1
 * 			1-0 1-0 1-0 1-0
 * 		1-0-0 1-0-1 1-0-2 1-0-3
 * 
 * 
 * 
 */

public class BigMapOnDisk implements BigMap{
	
	private final int LEVEL_THROTTLE = 8;
	private final int SPILL_THROTTLE = 100;
	private final int SECTOR_COUNT_IN_A_LEVEL = 4;
	private int level;
	private int hash;
	private boolean isInMemory;
	private Map<Integer, Pair> buffer; 
	private Iterator<Pair> entries;
	private boolean shouldSpill;
	private int currentCount;
	private BigMapOnDisk[] bigMapOnDisksNextLevel;
	private SpillFile spillFile;
	private SpillFile[] spillFiles;
	private String filePrefix;
	private int sectorNextIdx;
	
	public BigMapOnDisk(SpillFile spillFile) throws LevelTooDeepException, IOException {
		this();
		this.spillFile = spillFile;
		loadFromSpillFile();
	}
	
	public BigMapOnDisk() throws LevelTooDeepException {
		this(1, 0);
	}
	
	private BigMapOnDisk(int level, int hash) throws LevelTooDeepException {
		if(level > LEVEL_THROTTLE) {
			throw new LevelTooDeepException("level is too deep, LEVEL_THROTTLE is 8. At this case, turn up this parameter or data just is too skew");
		}
		this.level = level;
		this.hash = hash;
		this.isInMemory = false;
		this.buffer = new HashMap<Integer, Pair>();
		this.shouldSpill = false;
		this.currentCount = 0;
		this.sectorNextIdx = 0;
	}
	
	public void add(Pair pair) throws IOException {
		if (currentCount > SPILL_THROTTLE) {
			shouldSpill = true;
			startToSpill();
		}
		if (shouldSpill) {
			spillToFile(pair);
		} else {
			if (buffer.containsKey(pair.getKey())) {
				buffer.get(pair.getKey()).increment(pair.getAggCount());
			} else {
				buffer.put(pair.getKey(), pair);
			}
			currentCount++;
		}
	}
	
	public Pair next() {
		if (shouldSpill == false) {
			if (entries == null) {
				entries = buffer.values().iterator();
			}
			return entries.next();
		} else {
			if (sectorNextIdx <= SECTOR_COUNT_IN_A_LEVEL || bigMapOnDisksNextLevel[sectorNextIdx].hasNext()) {
				if (bigMapOnDisksNextLevel[sectorNextIdx].hasNext()) {
					return bigMapOnDisksNextLevel[sectorNextIdx].next();
				} else {
					sectorNextIdx++;
					if (sectorNextIdx <= SECTOR_COUNT_IN_A_LEVEL) {
						if (bigMapOnDisksNextLevel[sectorNextIdx].hasNext()) {
							return bigMapOnDisksNextLevel[sectorNextIdx].next();
						}
					}
				}
			}
		}
		return null;
	}
	
	public boolean hasNext() {
		if (entries == null) {
			 entries = buffer.values().iterator();
		}
		return entries.hasNext();
	}
	
	public void startToSpill() throws IOException {
		if (entries == null) {
			 entries = buffer.values().iterator();
		}
		spillFiles = new SpillFile[SECTOR_COUNT_IN_A_LEVEL];
		for (int i = 0; i < SECTOR_COUNT_IN_A_LEVEL; i++) {
			spillFiles[i] = new SpillFile(filePrefix, i);
		}
		while(entries.hasNext()) {
			Pair pair = entries.next();
			spillFiles[pair.getKey() >> level * 4 / SECTOR_COUNT_IN_A_LEVEL].add(entries.next()); 
		}
	}
	
	public void spillToFile(Pair pair) throws IOException {
		spillFiles[pair.getKey() >> level * 4 / SECTOR_COUNT_IN_A_LEVEL].add(entries.next()); 
	}
	
	private void loadFromSpillFile() throws IOException {
		while(true) {
			Pair pair = spillFile.next();
			if (pair == null) {
				break;
			}
			add(pair);
		}
	}
	
	private int getHash(int key, int level) {
		return key >> (level * 4);
	}
}
