package test.java.cn.liubinbin.experiment.groupbyer;

import cn.liubinbin.experiment.groupbyer.Pair;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author liubinbin
 */
public class PairTest {

    @Test
    public void testEqual() {
        Pair pair1 = new Pair(1, 2);
        Pair pair2 = new Pair(1, 3);
        Pair pair3 = new Pair(2, 2);
        Pair pair4 = new Pair(1, 2);
        assertFalse(pair1.equals(pair2));
        assertFalse(pair1.equals(pair3));
        assertTrue(pair1.equals(pair4));
    }
}
