package cn.liubinbin.experiment.rbtree;

import java.util.Random;

/**
 * @author liubinbin
 */
public class TreeMapUsage {

    public static void main(String[] args) {
        BinTreeMap<Integer, String> treeMap = new BinTreeMap<Integer, String>();
        Random random = new Random();
        int tempNum = 0;
        for (int i = 0; i < 10; i++) {
            tempNum = random.nextInt(100);
            treeMap.put(tempNum, "value" + tempNum);
        }
        System.out.println(treeMap.size());
        System.out.println(treeMap.containsKey(1));
        System.out.println(treeMap.get(1));
        treeMap.remove(2);
        System.out.println(treeMap.toString());
        System.out.println(treeMap.getFirstEntry());
        System.out.println(treeMap.getLastEntry());
    }

}
