package main.java.cn.liubinbin.experiment.groupbyer;

/**
 *
 * @author liubinbin
 * 
 */

public interface BigMap{
	
	public void add(Pair pair);
	
	public Pair next();
	
	public boolean hasNext();
}
