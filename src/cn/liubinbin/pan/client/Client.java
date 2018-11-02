package cn.liubinbin.pan.client;

import java.awt.List;
import java.util.ArrayList;

/**
 *
 * @author liubinbin
 *
 */
public class Client {

	private String feHost;
	private String fePort;
	
	public Client(String feHost, String fePort) {
		this.feHost = feHost;
		this.fePort = fePort;
	}
	
	public void open() {
		// get a address to connect
	}
	
	public void close() {
		
	}
	
	public void createBucket(String bucketName) {
		
	}
	
	public ArrayList<String> listBucket() {
		return null;
	}
	
	public ArrayList<String> listObject() {
		return null;
	}
	
	public void deleteBucket(String bucketName) {
		
	}
	
	public void getObject(String bucketName, String key) {
		
	}
	
	public void putOBject(String bucketName, String key, String filePath) {
		
	}
	
	public void deleteObject(String bucketName, String key) {
		
	}
}
