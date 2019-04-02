package cn.liubinbin.experiment.filecache;

import java.util.concurrent.atomic.AtomicInteger;

public class Handler {
	private AtomicInteger num;
	private long time;
	private String tblPart;
	
	public Handler(String tblPart) {
		this.num = new AtomicInteger(0);
		this.time = System.currentTimeMillis();
		this.tblPart = tblPart;
	}
	
	public void deal(){
		System.out.println(tblPart);
	}

	public long getLiveTime(){
		return System.currentTimeMillis() - time;
	}
	
	public void close(){
		//do nothing
	}
	
	public boolean addNum(int expect, int delta){
		return this.num.compareAndSet(expect, expect + delta);
	}
	
	public int getNum() {
		return num.get();
	}

	public void setNum(int num) {
		this.num.set(num);
	}

	public long getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}
