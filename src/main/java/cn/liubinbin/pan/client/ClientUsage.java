package cn.liubinbin.pan.client;

/**
 * Created by bin on 2019/5/21.
 */
public class ClientUsage {

    public static void main(String[] args) {
        String cacheServerHost = "localhost";
        String cacheServerPort = "50503";
        Client client = new Client(cacheServerHost, cacheServerPort);
        client.open();
        //do sth

        client.close();
    }
}
