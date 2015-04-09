package ee.tkasekamp.ltlminer;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.deckfour.xes.model.XLog;
import org.junit.Before;
import org.junit.Test;
import org.processmining.plugins.ltlchecker.RuleModel;

import ee.tkasekamp.ltlminer.util.XLogReader;

/**
 * Tests that measure time. Testing against TLQC.
 * 
 * @author TKasekamp
 *
 */
public class PerformanceTest {
	LTLMiner miner;
	XLog log;
	XLog log2;
	XLog log3;

	@Before
	public void setup() throws Exception {
		miner = new LTLMiner();
		log = XLogReader
				.openLog("C:/Users/Tonis/Downloads/TLQC/TLQC/dist/repairExample.xes");
		log2 = XLogReader.openLog("src/test/resources/exercise1.xes");
		log3 = XLogReader.openLog("src/test/resources/financial_log.xes.gz");

	}

	@Test
	public void repairExampleTest() {
		long a = System.nanoTime();
		ArrayList<RuleModel> result = miner.mineAll(log, rule(), 0.0);
		long b = System.nanoTime();
		long time = b - a;

		System.out.println("Time taken: " + time);

		assertEquals(64, result.size());

	}

	@Test
	public void exercise1test() {

		long a = System.nanoTime();
		ArrayList<RuleModel> result = miner.mineAll(log2, rule(), 0.0);
		long b = System.nanoTime();
		long time = b - a;

		System.out.println("Time taken: " + time);

		assertEquals(25, result.size());

		// for (RuleModel ruleModel : result) {
		// System.out.println(ruleModel.getLtlRule());
		// }
	}

	// This takes over 3 minutes, best if commented out
	// @Test
	public void financialTest() {
		long a = System.nanoTime();
		ArrayList<RuleModel> result = miner.mineAll(log3, rule(), 0.0);
		long b = System.nanoTime();
		long time = b - a;

		System.out.println("Time taken: " + time);

		assertEquals(64, result.size());

	}

	private String rule() {
		return "formula query( A: activity , B: activity ) :=  {}\n"
				+ "    (activity == A /\\ activity == B); ";
	}

}
