package main.java.cn.liubinbin.pan.experiment.log.v3;

import java.nio.ByteBuffer;

public class Entry<T> {
	
	private ByteBuffer byteBuffer;
	private int sequence;
	
	public Entry() {
		this.sequence = 0;
	}
	
	public Entry(ByteBuffer byteBuffer, int sequence) {
		this.setByteBuffer(byteBuffer);
		this.setSequence(sequence);
	}
	
	public ByteBuffer getByteBuffer() {
		return byteBuffer;
	}

	public void setByteBuffer(ByteBuffer byteBuffer) {
		this.byteBuffer = byteBuffer;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString(){
		return sequence + "";
	}
}