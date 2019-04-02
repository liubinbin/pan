package cn.liubinbin.experiment.groupbyer;

/**
 *
 * @author liubinbin
 * 
 */

public interface BigMap{
	
	public void add(Pair pair) throws Exception;
	
	public Pair next();
	
	public boolean hasNext();
}
