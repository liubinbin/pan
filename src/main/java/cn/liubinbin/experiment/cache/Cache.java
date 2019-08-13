package cn.liubinbin.experiment.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by bin on 2019/5/15.
 */
public class Cache extends LinkedHashMap {
    private int size;

    public Cache(int size) {
        // Third parameter is accessOrder
        // If accessOrder is true, we will put node to last after being got to make deleting use accessOrder
        // If accessOrder is true, we will use insert order to delete object
        super(size, 0.75f, true);
        this.size = size;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry entry) {
        return super.size() > this.size;
    }

    public static void main(String[] args) {
        Cache cache = new Cache(3);
        cache.put("1", "hello world");
        cache.put("2", "hello world");
//        cache.get("1");
        cache.put("3", "hello world");
        cache.put("4", "hello world");
        System.out.println("keySet: " + cache.keySet());
        // if we comment out "cache.get("1");", keySet is [2, 3, 4]
        // if not, keySet is [1, 3, 4]
    }
}
