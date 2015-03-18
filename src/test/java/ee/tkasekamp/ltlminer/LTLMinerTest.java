package ee.tkasekamp.ltlminer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.deckfour.xes.model.XLog;
import org.junit.Before;
import org.junit.Test;
import org.processmining.plugins.ltlchecker.CheckResultObject;
import org.processmining.plugins.ltlchecker.LTLChecker;

import ee.tkasekamp.ltlminer.util.XLogReader;

public class LTLMinerTest {
	LTLMiner miner;
	LTLChecker checker;
	XLog log;

	@Before
	public void setUp() throws Exception {
		miner = new LTLMiner();
		log = XLogReader.openLog("src/test/resources/exercise1.xes");
	}

	@Test
	public void getActivitiesFromLog() {
		ArrayList<String> shouldBe = new ArrayList<>();
		shouldBe.addAll(Arrays.asList("A", "B", "C", "D", "E"));

		assertNotNull(log);
		ArrayList<String> activites = miner.getActivities(log);

		assertEquals(5, activites.size());
		assertEquals(shouldBe, activites);
	}

	@Test
	public void analyseRule() {
		String rule = "formula always_when_A_then_eventually_E( A: activity , E: activity ) :=  {}\n"
				+ "    []( ( activity == A  -> <>( activity==E  ) ) ); ";
		Object[] objList = miner.analyseRule(log, rule);
		CheckResultObject output = (CheckResultObject) objList[0];

		assertEquals("There are a total of 25 combinations from the log", 25,
				output.getRules().length);
	}

}
