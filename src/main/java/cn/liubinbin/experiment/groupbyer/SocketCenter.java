package cn.liubinbin.experiment.groupbyer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * @author liubinbin
 * <p>
 * 3-0
 * <p>
 * 2-0 2-1 2-2 2-3
 * <p>
 * 1-0 1-1 1-2 1-3
 * <p>
 * input
 */
public class SocketCenter {

    Map<String, Queue<Pair>> shuffleBuffer = new HashMap<String, Queue<Pair>>();
    Map<String, Integer> finishRule = new HashMap<String, Integer>();
    Map<String, Integer> currentFinishMarkCount = new HashMap<String, Integer>();
    private int paralleism;

    public SocketCenter(int parallelism) {
        this.paralleism = parallelism;
        for (int i = 0; i < parallelism; i++) {
            //level 1
            shuffleBuffer.put("1-" + i, new LinkedList<Pair>());
            finishRule.put("1-" + i, 1);
            currentFinishMarkCount.put("1-" + i, 0);
            //level 2
            shuffleBuffer.put("2-" + i, new LinkedList<Pair>());
            finishRule.put("2-" + i, parallelism);
            currentFinishMarkCount.put("2-" + i, 0);
        }
        //sink node
        shuffleBuffer.put("3-0", new LinkedList<Pair>());
        finishRule.put("3-0", parallelism);
        currentFinishMarkCount.put("3-0", 0);
    }

    public void push(int level, int hash, Pair pair) {
        hash = (level == 3 ? 0 : hash);
        String source = level + "-" + hash;
        push(source, pair);
    }

    public synchronized void push(String source, Pair pair) {
        shuffleBuffer.get(source).add(pair);
    }

    // pair.getKey() == GroupByManager.FINSH_MARK means fetching can be over
    public synchronized Pair fetch(String source) {
        Pair pair = shuffleBuffer.get(source).poll();
        if (pair == null) {
            return null;
        }
        if (pair.getKey() != GroupByManager.FINSH_MARK) {
            return pair;
        } else {
            currentFinishMarkCount.put(source, currentFinishMarkCount.get(source) + 1);
            if (currentFinishMarkCount.get(source) >= finishRule.get(source)) {
                return pair;
            } else {
                return null;
            }
        }
    }

    public int getParalleism() {
        return paralleism;
    }
}
