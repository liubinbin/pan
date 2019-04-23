package test.java.cn.liubinbin.experiment.jobmanager;
/**
 * @author liubinbin
 */

import cn.liubinbin.experiment.jobmanager.JobType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JobTypeTest {

    @Test
    public void testTYPE() {
        JobType jobType = JobType.TYPE2;
        assertEquals(jobType, JobType.TYPE2);
    }

}
