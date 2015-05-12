package ee.tkasekamp.ltlminer;

import java.util.ArrayList;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.plugins.ltlchecker.RuleModel;

import ee.tkasekamp.ltlminer.util.XLogReader;

/**
 * LTLMiner usage example.
 * 
 * @author TKasekamp
 *
 */
public class UsageTest {

	@Test
	public void notCoExistence() throws Exception {
		String formula = "formula notCoExistence( A: activity , B: activity ) :=  { }"
				+ "!( (<>(activity == A) /\\ <>(activity == B) ));";
		LTLMiner miner = new LTLMiner(); 
		XLog log = XLogReader.openLog("src/test/resources/orderGoodsLog.xes");
		ArrayList<RuleModel> result = miner.mine(log, formula, 0.0);

		for (RuleModel rule : result) {
			System.out.println(rule.getCoverage() + " " + rule.getLtlRule());
		}
	}
}
