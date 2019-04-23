package cn.liubinbin.experiment.groupbyer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author liubinbin
 *         is InMemory
 */

public class BigMapInMemory implements BigMap {

    private int level;
    private boolean isInMemory;
    private Map<Integer, Pair> buffer;
    private Iterator<Pair> entries;

    public BigMapInMemory(int level) {
        this.level = level;
        this.isInMemory = true;
        this.buffer = new HashMap<Integer, Pair>();
    }

    public void add(Pair pair) {
        if (buffer.containsKey(pair.getKey())) {
            buffer.get(pair.getKey()).increment(pair.getAggCount());
        } else {
            buffer.put(pair.getKey(), pair);
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

}
