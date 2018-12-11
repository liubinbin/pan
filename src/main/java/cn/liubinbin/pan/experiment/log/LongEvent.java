package main.java.cn.liubinbin.pan.experiment.log;

public class LongEvent<T> {
	private T value;

	public void set(T value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		return value + "";
	}
}