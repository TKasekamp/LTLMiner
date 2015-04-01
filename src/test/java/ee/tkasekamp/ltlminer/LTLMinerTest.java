package ee.tkasekamp.ltlminer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.deckfour.xes.model.XLog;
import org.junit.Before;
import org.junit.Test;
import org.processmining.plugins.ltlchecker.CheckResultObject;
import org.processmining.plugins.ltlchecker.LTLChecker;
import org.processmining.plugins.ltlchecker.RuleModel;

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

	@Test
	public void mineTest() {
		String rule = "formula always_when_A_then_eventually_E( A: activity , E: activity ) :=  {}\n"
				+ "    []( ( activity == A  -> <>( activity==E  ) ) ); ";
		ArrayList<RuleModel> result = miner.mine(log, rule, 0.5);
		assertEquals("Counted them. So should be 16", 16, result.size());

		String rule2 = "formula is_activity_of_first_state_A( A: activity ) :="
				+ "{"
				+ "<h2>Is the activity of the first state equal to <b>A</b>?</h2>"
				+ "<p> Compare the activity of the first state with <b>A</b> </p>"
				+ "<p> Arguments:<br>"
				+ "<ul>"
				+ "<li><b>A</b> of type set (<i>ate.WorkflowModelElement</i>)</li>"
				+ "</ul>" + "</p>}" + "activity == A;";
		ArrayList<RuleModel> result2 = miner.mine(log, rule2, 0.2);
		assertEquals(1, result2.size());

		String rule3 = "formula query(A :activity, E: activity) := {} \n"
				+ "(<>(activity == A) /\\  !(<>(activity == E)));";
		ArrayList<RuleModel> result3 = miner.mine(log, rule3, 0.5);
		assertEquals(4, result3.size());

	}

}
