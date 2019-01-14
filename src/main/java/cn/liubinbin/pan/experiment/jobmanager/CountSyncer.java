package main.java.cn.liubinbin.pan.experiment.jobmanager;

import java.util.concurrent.atomic.AtomicInteger;

public class CountSyncer {
	
	private AtomicInteger count;
	private JobType jobType;
	private int limit;
	
	public CountSyncer(JobType jobType, int limit) {
		this.count = new AtomicInteger(0);
		this.setJobType(jobType);
		this.limit = limit;
	}
	
    public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean acquire() {
    	int currentCount = count.get();
    	assert currentCount <= limit;
    	return count.compareAndSet(currentCount, currentCount+1);
    }
    
    public void release() {
    	int currentCount = count.get();
    	assert currentCount <= 1;
    	
    	while(count.compareAndSet(currentCount, currentCount-1)) {
    		currentCount = count.get();
        	assert currentCount <= 1;
    	}
    }

	public JobType getJobType() {
		return jobType;
	}

	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}
    
    
}
