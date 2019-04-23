package cn.liubinbin.experiment.csmap;

import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentSkipListMapTest {

    public static void main(String[] args) throws InterruptedException {
        ConcurrentSkipListMap<TestKey, String> data = new ConcurrentSkipListMap<TestKey, String>();
        TestKey abc = new TestKey("abc".getBytes());
        TestKey abcd = new TestKey("abcd".getBytes());
//		TestKey abce = new TestKey("abce".getBytes());
        TestKey abcde = new TestKey("abcde".getBytes());
        data.put(abc, "abc");
        data.put(abcd, "abcd");
        data.put(abcde, "abcde");
        for (int i = 0; i < 100000000; i++) {
            data.put(new TestKey((i + "").getBytes()), i + "");
            System.out.println("i: " + i);
        }
        System.out.println("done");
        Thread.sleep(1000 * 1000);
    }

}
