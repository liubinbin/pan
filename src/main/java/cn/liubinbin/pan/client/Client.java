package cn.liubinbin.pan.client;

import java.util.ArrayList;

/**
 * @author liubinbin
 */
public class Client {

    private String cacheServerHost;
    private String cacheServerPort;

    public Client(String cacheServerHost, String cacheServerPort) {
        this.cacheServerHost = cacheServerHost;
        this.cacheServerPort = cacheServerPort;
    }

    public void open() {
        // get a address to connect
    }

    public void close() {

    }

    public ArrayList<String> listObject() {
        return null;
    }

    public void getObject(String bucketName, String key) {

    }

    public void putOBject(String bucketName, String key, String filePath) {

    }

    public void deleteObject(String bucketName, String key) {

    }
}
