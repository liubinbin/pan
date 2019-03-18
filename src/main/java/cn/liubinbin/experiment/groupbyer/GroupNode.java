package main.java.cn.liubinbin.experiment.groupbyer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;import javax.swing.text.html.HTMLDocument.HTMLReader.ParagraphAction;

/**
 *
 * @author liubinbin
 *
 */
public class GroupNode implements Runnable {

	private boolean fetchIsDone;
	
	private Queue<Pair> inQueue;
	private Map<Integer, Queue<Pair>> outQueue;
	private Map<Integer, Pair> buffer; 
	private int level;
	private int hash;
	private int parallelism;
	
	public GroupNode(Queue<Pair> inQueue, Map<Integer, Queue<Pair>> outQueue, int level, int hash, int parallelism) {
		this.fetchIsDone = false;
		this.inQueue = inQueue;
		this.outQueue = outQueue;
		this.level = level;
		this.hash = hash;
		this.buffer = new HashMap<Integer, Pair>();
		this.parallelism = parallelism;
	}

	private void setFetchDone() {
		this.fetchIsDone = true;
	}
	
	private void startPushDataToOut() {
		System.out.println("level " + level + " hash " + hash + " size " + buffer.keySet().size());
		Iterator<Pair> entries = buffer.values().iterator();

		Pair pair;
		while (entries.hasNext()) {
			pair = entries.next();
			System.out.println("push data key: " + "level " + level + " pair.key " + pair.getKey() + " aggcount: " + pair.getAggCount());
			outQueue.get(0).add(pair);
		}
		
		for (int i = 0; i < parallelism; i++) {
			outQueue.get(0).add(new Pair(new Record(GroupByManager.FINSH_MARK, "hello " + GroupByManager.FINSH_MARK)));
		}
	}
	
	@Override
	public void run() {
		Pair pair;
//		System.out.println("level " + level + " start");
		while(true) {
			pair = inQueue.poll();
			if (pair == null) {
				continue;
			}
//			System.out.println("level " + level + " pair.key " + pair.getKey());
			if (pair.getKey() == GroupByManager.FINSH_MARK ) {
//				System.out.println("meet finsh_mark " + " level " + level);
				break;
			}
			if (buffer.containsKey(pair.getKey())) {
				buffer.get(pair.getKey()).increment(pair.getAggCount());
			} else {
				buffer.put(pair.getKey(), pair);
			}
			
		}
		
		setFetchDone();
		
		startPushDataToOut();
	}

}
