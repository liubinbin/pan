package main.java.cn.liubinbin.experiment.groupbyer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author liubinbin
 *
 */
public class GroupNode implements Runnable {

	private boolean fetchIsDone;
	
	private String source;
	private int targetLevel;
	private Map<Integer, Pair> buffer; 
	private int level;
	private int hash;
	private int parallelism;
	private SocketCenter socketCenter;
	
	public GroupNode(SocketCenter socketCenter, String source, int level, int hash, int parallelism) {
		this.socketCenter = socketCenter;
		this.fetchIsDone = false;
		this.source = source;
		this.level = level;
		this.targetLevel = level + 1;
		this.hash = hash;
		this.buffer = new HashMap<Integer, Pair>();
		this.parallelism = parallelism;
	}

	private void setFetchDone() {
		this.fetchIsDone = true;
	}
	
	private void startPushDataToOut() {
		Iterator<Pair> entries = buffer.values().iterator();

		Pair pair;
		int hash = 0;
		while (entries.hasNext()) {
			pair = entries.next();
			hash = pair.getKey() % parallelism;
			socketCenter.push(targetLevel, hash, pair);
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
			if (buffer.containsKey(pair.getKey())) {
				buffer.get(pair.getKey()).increment(pair.getAggCount());
			} else {
				buffer.put(pair.getKey(), pair);
			}
		}
	}
	private void printBuffer() {
		Iterator<Pair> entries = buffer.values().iterator();

		Pair pair = null;
		while (entries.hasNext()) {
			pair = entries.next();
		}
	}
	
	@Override
	public void run() {
		fetchDataAndAgg();
		
		setFetchDone();
		
		startPushDataToOut();
	}

}
