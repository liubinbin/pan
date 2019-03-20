package test.java.cn.liubinbin.experiment.groupbyer;

import java.io.IOException;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.cn.liubinbin.experiment.groupbyer.Pair;
import main.java.cn.liubinbin.experiment.groupbyer.SpillFile;

/**
 *
 * @author liubinbin
 *
 */
public class SpillFileTest {
	
	@Test
	public void add() throws IOException {
		SpillFile spillFile = new SpillFile("spillfile");
		Pair pair1 = new Pair(123, 2);
		Pair pair2 = new Pair(234, 6);
		spillFile.add(pair1);
		spillFile.add(pair2);
		spillFile.closeWriter();
		Pair Pair1ToComp = spillFile.next();
		Pair Pair2ToComp = spillFile.next();
		spillFile.closeReader();
		assertTrue(pair1.equals(Pair2ToComp));
		assertTrue(pair2.equals(Pair2ToComp));
		assertFalse(Pair1ToComp.equals(Pair2ToComp));
	}
}
