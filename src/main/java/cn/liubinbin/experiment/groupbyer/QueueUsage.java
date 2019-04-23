package cn.liubinbin.experiment.groupbyer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author liubinbin
 */
public class QueueUsage {

    public static void main(String[] args) {
        Queue<Integer> data = new LinkedList<Integer>();
        data.offer(1);
        data.offer(2);
        System.out.println(data.poll());
        System.out.println(data.poll());
        System.out.println(data.poll());

        //push data uses offer;
        //fetch data uses poll;
    }

}
