package main.java.cn.liubinbin.experiment.groupbyer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author liubinbin
 * is OnDisk
 */

public class BigMapOnDisk implements BigMap{
	
	private final int LEVEL_THROTTLE = 8;
	private final int SPILL_THROTTLE = 100;
	private final int SECTOR_COUNT_IN_A_LEVEL = 4;
	private int level;
	private boolean isInMemory;
	private int sectorsCountInALevel;
	private Map<Integer, Pair> buffer; 
	private Iterator<Pair> entries;
	private boolean shouldSpill;
	private String tempSpillDir;
	private int currentCount;
	private BigMapOnDisk[] bigMapOnDisksNextLevel;
	
	public BigMapOnDisk() throws LevelTooDeepException {
		this(1);
	}
	
	public BigMapOnDisk(int level) throws LevelTooDeepException {
		if(level >= LEVEL_THROTTLE) {
			throw new LevelTooDeepException("level is too deep, LEVEL_THROTTLE is 8. At this case, turn up this parameter or data just is too skew");
		}
		this.level = level;
		this.isInMemory = false;
		this.sectorsCountInALevel = 16;
		this.buffer = new HashMap<Integer, Pair>();
		this.shouldSpill = false;
		this.currentCount = 0;
	}
	
	public void add(Pair pair) {
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
		if (entries == null) {
			 entries = buffer.values().iterator();
		}
		return entries.next();
	}
	
	public boolean hasNext() {
		if (entries == null) {
			 entries = buffer.values().iterator();
		}
		return entries.hasNext();
	}
	
	public void startToSpill() {
		
	}
	
	public void spillToFile(Pair pair) {
		
	}
	
}
