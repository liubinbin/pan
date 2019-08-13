package cn.liubinbin.pan.client;

import java.io.File;
import java.io.IOException;

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
//        client.putOBject("key6" , new File("C:\\Users\\viruser.v-desktop\\Desktop\\161709788f82c2ec"));
//        client.putOBject("key5" , "abc");

        client.getObject("key6", new File("dest"));

//        client.deleteObject("default","key4");


        client.close();
    }
}
