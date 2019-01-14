package main.java.cn.liubinbin.pan.experiment.jobmanager;

/**
 *
 * @author liubinbin
 *
 */
public enum JobType {

	TYPE1(1), TYPE2(2), TYPE3(3);

	JobType(int value) {

	}

	String getJobType(String jobType) {
		return this.toString() + "-" + jobType.replace("%", "%%");
	}
}
