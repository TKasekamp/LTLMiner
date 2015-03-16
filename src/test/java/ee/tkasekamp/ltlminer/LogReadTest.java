package ee.tkasekamp.ltlminer;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.junit.Before;
import org.junit.Test;
import org.processmining.plugins.ltlchecker.CheckResultObject;
import org.processmining.plugins.ltlchecker.InstanceModel;
import org.processmining.plugins.ltlchecker.LTLChecker;
import org.processmining.plugins.ltlchecker.model.LTLModel;

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

	@Test
	public void matchEverythingTest() {
		LTLChecker checker = new LTLChecker();
		Vector<String> rules = new Vector<>();
		rules.add("always_when_A_then_eventually_B");
		checker.setSelectedRules(rules);

		LTLModel model = new LTLModel();
		model.setFile(matchEverythingSample());
		Object[] objList = checker.analyse(log, model);
		CheckResultObject output = (CheckResultObject) objList[0];

		System.out.println(output.getRules()[0].getLtlRule());
		XLog comLog = (XLog) objList[1];
		XLog ncomLog = (XLog) objList[2];

		assertEquals("Matches everything", 3, comLog.size());
		assertEquals(0, ncomLog.size());

	}

	@Test
	public void anotherTest() {
		LTLChecker checker = new LTLChecker();
		Vector<String> rules = new Vector<>();
		// rules.add("ABC");
		rules.add("always_when_A_then_eventually_B");
		rules.add("always_when_B_then_eventually_C");
		checker.setSelectedRules(rules);

		LTLModel model = new LTLModel();
		model.setFile(matchEverythingSample());
		Object[] objList = checker.analyse(log, model);
		CheckResultObject output = (CheckResultObject) objList[0];

		System.out.println(output.getRules()[0].getCoverage());
		for (InstanceModel mod : output.getInstances()) {
			System.out.println(mod.getHealthDegree());
			System.out.println(mod.isCorrect());
			for (XEvent trace : mod.getInstance()) {

				String traceName = XConceptExtension.instance().extractName(
						trace);
				System.out.println("Eventname: " + traceName);
				XAttributeMap caseAttributes = trace.getAttributes();
				XAttributeMap eventAttributes = trace.getAttributes();
				for (String key : eventAttributes.keySet()) {
					String value = eventAttributes.get(key).toString();
					System.out.println("key: " + key + "  value: " + value);
				}
				// for(String key :caseAttributes.keySet()){
				// String value = caseAttributes.get(key).toString();
				// System.out.println("key: "+key+"  value: "+value);
				// }

			}
		}
		System.out.println(output.getInstances());
		// System.out.println(output.getRules()[0].toString());

		// System.out.println(output.getInstances()[0].getInstance());
		XLog comLog = (XLog) objList[1];
		XLog ncomLog = (XLog) objList[2];

		for (XTrace trace : ncomLog) {
			String traceName = XConceptExtension.instance().extractName(trace);
			System.out.println("TraceName: " + traceName);
			XAttributeMap caseAttributes = trace.getAttributes();
			for (XEvent event : trace) {
				String activityName = XConceptExtension.instance().extractName(
						event);
				System.out.println("ActivityName: " + activityName);
				Date timestamp = XTimeExtension.instance().extractTimestamp(
						event);
				System.out.println("Timestamp: " + timestamp);
				String eventType = XLifecycleExtension.instance()
						.extractTransition(event);
				XAttributeMap eventAttributes = event.getAttributes();
				for (String key : eventAttributes.keySet()) {
					String value = eventAttributes.get(key).toString();
					System.out.println("key: " + key + "  value: " + value);
				}
				for (String key : caseAttributes.keySet()) {
					String value = caseAttributes.get(key).toString();
					System.out.println("key: " + key + "  value: " + value);
				}

			}
		}

	}

	private String matchEverythingSample() {
		return "set ate.EventType;"
				+ "set ate.Originator;"
				+ "date ate.Timestamp := \"yyyy-MM-dd\"; "
				+ "set ate.WorkflowModelElement;"
				+ "rename ate.WorkflowModelElement as activity; \n"
				+ "formula always_when_A_then_eventually_B( A: activity : \"A\", E: activity : \"E\") :=  {}\n"
				+ "    []( ( activity == A  -> <>( activity==E  ) ) ); "
				+ "formula always_when_B_then_eventually_C( A: activity : \"B\", E: activity : \"C\") :=  {}\n"
				+ "    []( ( activity == A  -> <>( activity==E  ) ) ); ";
	}

	private String anotherQuery() {
		return "set ate.EventType;" + "set ate.WorkflowModelElement;"
				+ "rename ate.WorkflowModelElement as activity; \n"
				+ "rename ate.EventType as eventtype;"
				+ "rename ate.EventType as event;"
				+ "formula query(A :activity, E: activity) := {} \n"
				+ "(<>(activity == A) /\\  !(<>(activity == E)));";
	}

}
