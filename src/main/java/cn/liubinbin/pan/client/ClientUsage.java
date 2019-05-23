package cn.liubinbin.pan.client;

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
        client.getObject("key2");
        client.close();
    }
}
