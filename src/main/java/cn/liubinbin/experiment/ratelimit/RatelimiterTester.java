package cn.liubinbin.experiment.ratelimit;

import com.google.common.util.concurrent.RateLimiter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bin on 2019/5/16.
 */
public class RatelimiterTester {

    public static void main(String[] args) {
        final RateLimiter rateLimiter = RateLimiter.create(10);
        for (int i = 0; i < 10; i ++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    while (true) {
                        rateLimiter.acquire();
                        System.out.println("pass , threadId " + Thread.currentThread().getId() + " time " + df.format(new Date()));
                    }
                }
            }).start();
        }
    }
}
