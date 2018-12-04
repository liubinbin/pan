package main.java.cn.liubinbin.pan.experiment.cache;
/**
 *
 * @author liubinbin
 *
 */
public class Addr {

	private int bucketIdx;
	private int offset;
	private int length;
	
	public Addr(int bucketIdx, int offset, int length) {
		this.setBucketIdx(bucketIdx);
		this.setOffset(offset);
		this.setLength(length);
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getBucketIdx() {
		return bucketIdx;
	}

	public void setBucketIdx(int bucketIdx) {
		this.bucketIdx = bucketIdx;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	

}
