package cn.liubinbin.experiment.groupbyer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * @author liubinbin
 * is OnDisk
 * <p>
 * 1. on buffer:
 * 2. on disk:
 * <p>
 * fileName: level1.hash-level2.hash-level3.hash-level4.hash
 * this file stands for source
 * <p>
 * 1
 * 1-0 			1-0 1-0 1-0
 * 1-0-0 1-0-1 1-0-2 1-0-3
 */

public class BigMapOnDisk implements BigMap {

    private final int LEVEL_THROTTLE = 8;
    private final int SPILL_THROTTLE = 10000;
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
    private boolean spillFileClosed;

    public BigMapOnDisk(SpillFile spillFile, String filePrefix, int level, int hash) throws LevelTooDeepException, IOException {
        this(level, hash);
        this.spillFile = spillFile;
        this.filePrefix = filePrefix + "-" + hash;
        loadFromSpillFile();
    }

    public BigMapOnDisk() throws LevelTooDeepException {
        this(1, 0);
        this.filePrefix = "0";
    }

    private BigMapOnDisk(int level, int hash) throws LevelTooDeepException {
        if (level > LEVEL_THROTTLE) {
            throw new LevelTooDeepException("level is too deep, LEVEL_THROTTLE is 8. At this case, turn up this parameter or data just is too skew");
        }
        this.level = level;
        this.hash = hash;
        this.isInMemory = false;
        this.buffer = new HashMap<Integer, Pair>();
        this.shouldSpill = false;
        this.currentCount = 0;
        this.sectorNextIdx = 0;
        this.spillFileClosed = false;
    }

    public static void main(String[] args) throws LevelTooDeepException, IOException, InterruptedException {
        int dataCount = 123456;
        int key = 0;
        Random random = new Random();

        BigMapOnDisk bigMapOnDisk = new BigMapOnDisk();

        for (int i = 0; i < dataCount; i++) {
            key = random.nextInt(100) + 100000;
//			key = i % 4;
            bigMapOnDisk.add(new Pair(new Record(key, "hello " + key)));
        }
        int aggTotalCount = 0;

        Pair pair;
        System.out.println("key\taggcount");
        Thread.sleep(1000 * 2);
        while (true) {
            pair = bigMapOnDisk.next();
            if (pair == null) {
                break;
            }
            aggTotalCount += pair.getAggCount();
            System.out.println(pair.getKey() + "\t" + pair.getAggCount());
        }
        System.out.println("aggTotalCount: " + aggTotalCount);
    }

    public void add(Pair pair) throws IOException, LevelTooDeepException {
        if (currentCount >= SPILL_THROTTLE) {
            if (shouldSpill == false) {
                startToSpill();
            }
            shouldSpill = true;
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
            return entries.hasNext() ? entries.next() : null;
        } else {
            if (spillFileClosed == false) {
                for (int i = 0; i < SECTOR_COUNT_IN_A_LEVEL; i++) {
                    try {
                        spillFiles[i].closeWriter();
                        ;
                    } catch (Exception e) {
                        System.out.println("spillFile " + spillFiles[i].getFilePath() + " close got exception");
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < SECTOR_COUNT_IN_A_LEVEL; i++) {
                    try {
                        bigMapOnDisksNextLevel[i] = new BigMapOnDisk(spillFiles[i], this.filePrefix, this.level + 1, i);
                    } catch (LevelTooDeepException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                spillFileClosed = true;
            }

            while (sectorNextIdx < SECTOR_COUNT_IN_A_LEVEL) {
                //TODO
                Pair pair = bigMapOnDisksNextLevel[sectorNextIdx].next();
                if (pair != null) {
                    return pair;
                } else {
                    sectorNextIdx++;
                }
//				if (bigMapOnDisksNextLevel[sectorNextIdx].hasNext()) {
//					return bigMapOnDisksNextLevel[sectorNextIdx].next();
//				} else {
//					sectorNextIdx++;
//					if (sectorNextIdx <= SECTOR_COUNT_IN_A_LEVEL) {
//						if (bigMapOnDisksNextLevel[sectorNextIdx].hasNext()) {
//							return bigMapOnDisksNextLevel[sectorNextIdx].next();
//						}
//					}
//				}
            }
        }
        return null;
    }

    public boolean hasNext() {
        return true;
//		if (entries == null) {
//			 entries = buffer.values().iterator();
//		}
//		return entries.hasNext();
    }

    public void startToSpill() throws IOException, LevelTooDeepException {
        if (entries == null) {
            entries = buffer.values().iterator();
        }
        spillFiles = new SpillFile[SECTOR_COUNT_IN_A_LEVEL];
        bigMapOnDisksNextLevel = new BigMapOnDisk[SECTOR_COUNT_IN_A_LEVEL];
        for (int i = 0; i < SECTOR_COUNT_IN_A_LEVEL; i++) {
            spillFiles[i] = new SpillFile(filePrefix, i);
//			bigMapOnDisksNextLevel[i] = new BigMapOnDisk(spillFiles[i]);
        }
        int hash;
        while (entries.hasNext()) {
            Pair pair = entries.next();
            hash = getHash(pair.getKey(), level);
            spillFiles[hash].add(pair);
        }
    }

    public void spillToFile(Pair pair) throws IOException {
        spillFiles[getHash(pair.getKey(), level)].add(pair);
    }

    private void loadFromSpillFile() throws IOException, LevelTooDeepException {
        while (true) {
            Pair pair = spillFile.next();
            if (pair == null) {
                break;
            }
            add(pair);
        }
    }

    //TODO find a better hash function
    private int getHash(int key, int level) {
        return (key >> ((level - 1) * 4)) % SECTOR_COUNT_IN_A_LEVEL;
    }

    public String toString() {
        return "BigMapOnDisk [ " + level + "," + hash + " ]";
    }
}
