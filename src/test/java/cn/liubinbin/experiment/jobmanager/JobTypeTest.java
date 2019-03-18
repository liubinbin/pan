package test.java.cn.liubinbin.experiment.jobmanager;
/**
 *
 * @author liubinbin
 *
 */

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.java.cn.liubinbin.experiment.jobmanager.JobType;

public class JobTypeTest {

	@Test
	public void testTYPE(){
		JobType jobType = JobType.TYPE2;
		assertEquals(jobType, JobType.TYPE2);
	}

}
