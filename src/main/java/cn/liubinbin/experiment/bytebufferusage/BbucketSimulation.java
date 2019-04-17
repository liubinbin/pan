package cn.liubinbin.experiment.bytebufferusage;

import cn.liubinbin.pan.manager.bcache.BItem;

import java.nio.ByteBuffer;

/**
 * Created by bin on 2019/4/17.
 */
public class BbucketSimulation {

    public static void main(String[] args) {
        System.out.println("hello BbucketSimulation");
        ByteBuffer bBucket = ByteBuffer.allocate(1024 * 1024 * 2);
        System.out.println("bBucket " + bBucket.toString());
        int bucketOffset = 0;

        int slotSize = 10240;
        BItem first = new BItem();
        BItem second = new BItem();

        // write first KeyValue
        int offsetFirst = 0;
        bucketOffset = offsetFirst;


        // write second KeyValue
        int offsetSecond = 10240;

        // get first KeyValue
        // offsetFirst
        // print key, value
        //get second KeyValue
        // offsetSecond

    }
}
