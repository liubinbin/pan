package main.java.cn.liubinbin.experiment.filecache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Processor {
	
	private AtomicLong callCount = new AtomicLong(0); 
	private ConcurrentHashMap<String, Handler> cache = new ConcurrentHashMap<String, Handler>();
	private int sizeInFile;
	
	public Processor() {
		sizeInFile = 5000000;
	}
	
	public Handler getHandler(String tblPart, int num){
		callCount.addAndGet(1);
		while(true){
			if (!cache.containsKey(tblPart)){
				cache.putIfAbsent(tblPart, new Handler(tblPart));
			} 
			Handler candidate = cache.get(tblPart);
			if (candidate!= null){
				int temp = candidate.getNum();
				if (candidate.addNum(temp, num)){
					return candidate;
				}
			}
		}
	}
	
	public long cal(){
		long result = 0;
		for (String key : cache.keySet()){
			result += cache.get(key).getNum();
		}
		return result;
	}
	
	public long callCount(){
		return callCount.get();
	}
}
