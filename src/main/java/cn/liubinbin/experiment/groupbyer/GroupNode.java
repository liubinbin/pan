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
	private Queue<Pair> outQueue;
	private Map<Integer, Pair> buffer; 
	private int level;
	private int hash;
	
	public GroupNode(Queue<Pair> inQueue, Queue<Pair> outQueue, int level, int hash) {
		this.fetchIsDone = false;
		this.inQueue = inQueue;
		this.outQueue = outQueue;
		this.level = level;
		this.hash = hash;
		this.buffer = new HashMap<Integer, Pair>();
	}

	private void setFetchDone() {
		this.fetchIsDone = true;
	}
	
	private void startPushDataToOut() {
		Iterator<Pair> entries = buffer.values().iterator();

		Pair pair;
		while (entries.hasNext()) {
			pair = entries.next();
//			System.out.println("push data key: " + pair.getKey() + " aggcount: " + pair.getAggCount());
			outQueue.add(pair);
		}
	}
	
	@Override
	public void run() {
		Pair pair;
		while(true) {
			pair = inQueue.poll();
			if (pair == null) {
				break;
			}
			if (buffer.containsKey(pair.getKey())) {
				buffer.get(pair.getKey()).increment(pair.getAggCount());
			} else {
				buffer.put(pair.getKey(), pair);
			}
			if (pair.getKey() == GroupByManager.FINSH_MARK ) {
				break;
			}
		}
		
		setFetchDone();
		
		startPushDataToOut();
	}

}
