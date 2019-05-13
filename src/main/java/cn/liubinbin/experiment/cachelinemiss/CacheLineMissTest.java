package cn.liubinbin.experiment.cachelinemiss;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bin on 2019/5/13.
 */
public class CacheLineMissTest {

    public void test1() {
        int threadCount = 10;
        int[] data = new int[threadCount];
        for (int i = 0; i < threadCount; i++){
            data[i] = 0;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

    }

    public void test2() {

    }

    public static void main(String[] args) {

    }
}
