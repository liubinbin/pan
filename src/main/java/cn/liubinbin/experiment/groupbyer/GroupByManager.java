package main.java.cn.liubinbin.experiment.groupbyer;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author liubinbin
 * 
 * impelement group by sum(), there are two versions: in-menory and on-disk
 *
 */
public class GroupByManager {
	
	protected static int FINSH_MARK = -1;
	
	public static void main(String[] args) throws InterruptedException {
		int dataCount = 10;
		Map<Integer, Queue<Pair>> shuffleBuffer = new HashMap<Integer, Queue<Pair>>();
		List<Queue<Pair>> input = new ArrayList<Queue<Pair>>();
		GroupNode[] groupNodeLevel1;
		GroupNode[] groupNodeLevel2;
		Queue<Pair> outputQueue;
		Map<Integer, Queue<Pair>> output;
		
		Random random = new Random();
		int parallelism = 4;
		
		//initial
		for(int i = 0; i < parallelism; i++) {
			input.add(new LinkedList<Pair>());
			shuffleBuffer.put(i, new LinkedList<Pair>());
		}
		groupNodeLevel1 = new GroupNode[parallelism];
		groupNodeLevel2 = new GroupNode[parallelism];
		outputQueue = new LinkedBlockingQueue<Pair>();
		output = new HashMap<Integer, Queue<Pair>>();
		output.put(0, outputQueue);
		
		//level-1
		for (int i = 0; i < parallelism; i++) {
			groupNodeLevel1[i] = new GroupNode(input.get(i), shuffleBuffer, 1, i, parallelism);
		}
		
		//shuffle
		
		//level-2
		for (int i = 0; i < parallelism; i++) {
			groupNodeLevel2[i] = new GroupNode(shuffleBuffer.get(i), output , 2, i, parallelism);
		}
		
		//start groupNode
		for (int i = 0; i < parallelism; i++) {
			new Thread(groupNodeLevel1[i]).start();
			new Thread(groupNodeLevel2[i]).start();
		}
		
		//push data
		int key = 0;
		for (int i = 0; i < dataCount; i++) {
//			key = random.nextInt(10000);
			key = i + 100;
			input.get(i % parallelism).add(new Pair(new Record(key, "hello " + key)));
		}
		
		//push mark
		for (int i = 0; i < parallelism; i++) {
			input.get(i % parallelism).add(new Pair(new Record(FINSH_MARK, "hello " + FINSH_MARK)));
		}
		
		//get data
		Pair pair;
		System.out.println("key\taggcount");
		Thread.sleep(1000 * 10);
		System.out.println("start to ouput result");
		for(int i = 0; i < 20; i++) {
			pair = output.get(0).poll();
			if (pair != null) {
				System.out.println(pair.getKey() + "\t" + pair.getAggCount());
			} else {
				System.out.println("pair null");
			}
		}
	}
}
