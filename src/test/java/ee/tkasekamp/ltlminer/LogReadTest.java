package ee.tkasekamp.ltlminer;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import org.deckfour.xes.model.XLog;
import org.junit.Before;
import org.junit.Test;

/**
 * Read in log and try to find all the names of the rules. Just experimentation.
 * 
 * @author TKasekamp
 *
 */
public class LogReadTest {
	XLog log;

	@Before
	public void setUp() throws Exception {
		log = XLogReader.openLog("src/test/resources/exercise1.xes");
	}

	@Test
	public void findActivities() {
		HashSet<String> shouldBe = new HashSet<>();
		shouldBe.addAll(Arrays.asList("A", "B", "C", "D", "E"));

		assertNotNull(log);
		HashSet<String> activites = LTLMiner.findAllActivities(log);

		assertEquals(5, activites.size());
		assertEquals(shouldBe, activites);
	}

}
