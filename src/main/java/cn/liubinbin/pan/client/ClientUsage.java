package cn.liubinbin.pan.client;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bin on 2019/5/21.
 */
public class ClientUsage {

    public static void main(String[] args) throws IOException {
        String cacheServerHost = "localhost";
        String cacheServerPort = "50503";
        Client client = new Client(cacheServerHost, cacheServerPort);
        client.open();
        //do sth
//        long startTime = System.currentTimeMillis();
//        for (int i = 0; i < 100000; i++) {
//            InputStream inputStream = client.getObject("key");
//        }
//        System.out.println("time used " + (System.currentTimeMillis() - startTime));
//        client.putOBject("default","key4", null);
        client.deleteObject("default","key4");
        client.close();
    }
}
