package main.java.cn.liubinbin.experiment.groupbyer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author liubinbin
 *
 */
public class GroupNode implements Runnable {

	private boolean fetchIsDone;
	
	private String source;
	private int targetLevel;
	private int level;
	private int hash;
	private int parallelism;
	private SocketCenter socketCenter;
	private BigMapInMemory bigMapInMemory;
	private int fetchCount;
	private int pushCount;
	
	public GroupNode(SocketCenter socketCenter, String source, int level, int hash, int parallelism) {
		this.socketCenter = socketCenter;
		this.fetchIsDone = false;
		this.source = source;
		this.level = level;
		this.targetLevel = level + 1;
		this.hash = hash;
		this.parallelism = parallelism;
		this.bigMapInMemory = new BigMapInMemory(level);
		this.fetchCount = 0;
		this.pushCount = 0;
	}

	private void setFetchDone() {
		this.fetchIsDone = true;
	}
	
	private void startPushDataToOut() {
		Pair pair;
		int hash = 0;
		while (bigMapInMemory.hasNext()) {
			pair = bigMapInMemory.next();
			hash = pair.getKey() % parallelism;
			socketCenter.push(targetLevel, hash, pair);
			pushCount++;
		}
		
		// add mark
		if (level == 1) {
			for (int i = 0; i < parallelism; i++) {
				socketCenter.push( targetLevel, (i % parallelism), new Pair(new Record(GroupByManager.FINSH_MARK, "hello " + GroupByManager.FINSH_MARK)));
			}
		} else {
			socketCenter.push( targetLevel, 0, new Pair(new Record(GroupByManager.FINSH_MARK, "hello " + GroupByManager.FINSH_MARK)));
		}
	}
	
	private void fetchDataAndAgg() {
		Pair pair;
		while(true) {
			pair = socketCenter.fetch(source);
			if (pair == null) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			if (pair.getKey() == GroupByManager.FINSH_MARK ) {
				break;
			}
			bigMapInMemory.add(pair);
			fetchCount++;
		}
	}
	
	@Override
	public void run() {
		fetchDataAndAgg();
		
		setFetchDone();
		
		startPushDataToOut();
		
		System.out.println("this " + toString() + " status " + getTaskStatus() );
	}

	
	public String toString() {
		return "GroupNode[ " + level + "," + hash + " ]";
	}
	
	public String getTaskStatus() {
		return "[ " + fetchCount + "," + pushCount + " ]";
	}
}
