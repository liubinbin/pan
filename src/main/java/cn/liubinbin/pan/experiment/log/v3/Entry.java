package main.java.cn.liubinbin.pan.experiment.log.v3;

public class Entry<T> {
	
	public Entry() {
		// TODO Auto-generated constructor stub
	}
	
	private T value;

	public void set(T value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		return value + "";
	}
}