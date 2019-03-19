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
//		System.out.println("startPushDataToOut level " + level + " hash " + hash + " size " + buffer.keySet().size());
		Iterator<Pair> entries = buffer.values().iterator();

		Pair pair;
		int counter = 0;
		int hash = 0;
		while (entries.hasNext()) {
			pair = entries.next();
//			System.out.println("push data key: " + "level " + level + " pair.key " + pair.getKey() + " aggcount: " + pair.getAggCount());
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
	
	private void printBuffer() {
		Iterator<Pair> entries = buffer.values().iterator();

		Pair pair = null;
		while (entries.hasNext()) {
			pair = entries.next();
//			System.out.println("buffer data: level " + level + " pair.key " + pair.getKey() + " aggcount: " + pair.getAggCount());
		}
	}
	
	@Override
	public void run() {
		Pair pair;
//		System.out.println("level " + level + " start");
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
//				System.out.println("level " + level + " meet finsh_mark ");
				break;
			}
			if (buffer.containsKey(pair.getKey())) {
				buffer.get(pair.getKey()).increment(pair.getAggCount());
			} else {
				buffer.put(pair.getKey(), pair);
			}
//			printBuffer();
		}
		
		setFetchDone();
		
		startPushDataToOut();
		
//		System.out.println("level " + level + " GroupNode ends");
	}

}
