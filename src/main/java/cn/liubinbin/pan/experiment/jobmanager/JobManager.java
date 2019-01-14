package main.java.cn.liubinbin.pan.experiment.jobmanager;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author liubinbin
 *
 */
public class JobManager {
	
	ExecutorService es;
	ArrayList<CountSyncer> allJobCounter;
	
	public JobManager() {
		int esThreadCount = 8;
		this.es = Executors.newFixedThreadPool(esThreadCount);
		this.allJobCounter = new ArrayList<CountSyncer>() ;
	}
	
	public void addJobTypeLimit(JobType jobType, int deltaLimit) {
		for (CountSyncer countSyncer : allJobCounter) {
			if (countSyncer.getJobType() == jobType) {
				countSyncer.setLimit(countSyncer.getLimit() + deltaLimit);
				return ;
			}
		}
		allJobCounter.add(new CountSyncer(jobType, deltaLimit));
	}
	
	public void run() {
		while (true) {
			for (CountSyncer countSyncer : allJobCounter) {
				if (countSyncer.acquire()) {
					es.execute(new Job(countSyncer));
				}
			}
		}
	}

	public static void main(String[] args) {
		JobManager jobManager = new JobManager();
		jobManager.addJobTypeLimit(JobType.TYPE1, 2);
		jobManager.addJobTypeLimit(JobType.TYPE2, 3);
		jobManager.addJobTypeLimit(JobType.TYPE2, 6);
		jobManager.run();
	}

}
