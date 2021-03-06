package cn.liubinbin.experiment.groupbyer;

import java.util.Random;

/**
 * @author liubinbin
 * <p>
 * impelement group by sum(), there are two versions: in-menory and on-disk
 */
public class GroupByManager {

    protected static int FINSH_MARK = -1;

    public static void main(String[] args) throws InterruptedException {
        int parallelism = 4;
        SocketCenter socketCenter = new SocketCenter(parallelism);
        int dataCount = 123456789;
        GroupNode[] groupNodeLevel1;
        GroupNode[] groupNodeLevel2;

        Random random = new Random();

        //initial
        groupNodeLevel1 = new GroupNode[parallelism];
        groupNodeLevel2 = new GroupNode[parallelism];

        //level-1
        for (int i = 0; i < parallelism; i++) {
            groupNodeLevel1[i] = new GroupNode(socketCenter, "1" + "-" + i, 1, i, parallelism);
        }

        //level-2
        for (int i = 0; i < parallelism; i++) {
            groupNodeLevel2[i] = new GroupNode(socketCenter, "2" + "-" + i, 2, i, parallelism);
        }

        //start groupNode
        for (int i = 0; i < parallelism; i++) {
            new Thread(groupNodeLevel1[i]).start();
            new Thread(groupNodeLevel2[i]).start();
        }

        //push data
        int key = 0;
        for (int i = 0; i < dataCount; i++) {
            key = random.nextInt(123);
//			key = i % 4;
            socketCenter.push(1, (i % parallelism), new Pair(new Record(key, "hello " + key)));
        }

        //push mark
        for (int i = 0; i < parallelism; i++) {
            socketCenter.push(1, (i % parallelism), new Pair(new Record(FINSH_MARK, "hello " + FINSH_MARK)));
        }

        //get data
        Pair pair;
        System.out.println("key\taggcount");
        Thread.sleep(1000 * 2);
        int aggTotalCount = 0;
        while (true) {
            pair = socketCenter.fetch("3-0");
            if (pair != null) {
                if (pair.getKey() == GroupByManager.FINSH_MARK) {
                    break;
                } else {
                    aggTotalCount += pair.getAggCount();
                    System.out.println(pair.getKey() + "\t" + pair.getAggCount());
                }
            } else {
                Thread.sleep(1);
            }
        }
        System.out.println("aggTotalCount: " + aggTotalCount);
        System.out.println("done");
    }
}
