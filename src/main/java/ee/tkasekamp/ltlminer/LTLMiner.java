package ee.tkasekamp.ltlminer;

import java.util.ArrayList;
import java.util.Vector;

import org.deckfour.xes.model.XLog;
import org.processmining.plugins.ltlchecker.CheckResultObject;
import org.processmining.plugins.ltlchecker.LTLChecker;
import org.processmining.plugins.ltlchecker.RuleModel;
import org.processmining.plugins.ltlchecker.model.LTLModel;

/**
 * {@link LTLMiner} main class. The basic algorithm is:<br>
 * <ul>
 * <li>get list of activities from log</li>
 * <li>generates all possible combinations of the rule with the those activities
 * </li>
 * <li>make all those rules into a single valid String so it is an LTLModel</li>
 * <li>set the LTLChecker to check those rule names</li>
 * <li>give the LTLChecker both the {@link XLog} and {@link LTLModel} as inputs</li>
 * <li>return only {@link RuleModel}'s with {@link RuleModel#getCoverage()}
 * greater or equal to the threshold</li>
 * </ul>
 * 
 * @author TKasekamp
 *
 */
public class LTLMiner {
	private CombinationCreator creator;
	private LTLChecker checker;
	private LogFilter logFilter;

	public LTLMiner() {
		creator = new CombinationCreator();
		checker = new LTLChecker();
		logFilter = new LogFilter();
	}

	/**
	 * @param repetitions
	 *            True to create repetitions, false otherwise
	 */
	public LTLMiner(boolean repetitions) {
		creator = new CombinationCreator(repetitions);
		checker = new LTLChecker();
		logFilter = new LogFilter();
	}

	/**
	 * The main method for LTLMiner. Tests the rule with the
	 * {@link LogFilter#DEFAULT_EVENTS} most frequent events in the log.
	 * Generates all possible combinations of the rule with those events. Uses
	 * LTLChecker to test all those rules. Returns {@link RuleModel}'s with
	 * coverage above or equal to the threshold.
	 * 
	 * @param log
	 *            {@link XLog}
	 * @param rule
	 *            Valid LTL rule like
	 *            <code>formula doThis(A:activity)= {...}</code>
	 * @param threshold
	 *            0 to 1
	 * @return
	 */
	public ArrayList<RuleModel> mine(XLog log, String rule, double threshold) {
		ArrayList<String> ac = logFilter.getFrequent(log);
		Object[] objList = analyseRule(log, rule, ac);
		return filter(objList, threshold);
	}

	/**
	 * Tests the rule with the input howManyEvents most frequent events in the
	 * log. Generates all possible combinations of the rule with those events.
	 * Uses LTLChecker to test all those rules. Returns {@link RuleModel}'s with
	 * coverage above or equal to the threshold.<br>
	 * 
	 * <strong>Setting the number of events too high will decrease
	 * performance</strong>
	 * 
	 * @param log
	 *            {@link XLog}
	 * @param rule
	 *            Valid LTL rule like
	 *            <code>formula doThis(A:activity)= {...}</code>
	 * @param threshold
	 *            0 to 1
	 * @param howManyEvents
	 *            How many different events to generate rules with. Note that
	 *            for a rule with 2 inputs the number will be howManyEvents
	 *            squared. For 3 inputs it will be
	 *            howManyEvents*howManyEvents*howManyEvents and so on.
	 * @return
	 */
	public ArrayList<RuleModel> mine(XLog log, String rule, double threshold,
			int howManyEvents) {
		ArrayList<String> ac = logFilter.getFrequent(log, howManyEvents);
		Object[] objList = analyseRule(log, rule, ac);
		return filter(objList, threshold);
	}

	/**
	 * Gets all events from from the log and generates all possible combinations
	 * with rule. Uses LTLChecker to test all those rules. Returns
	 * {@link RuleModel}'s with coverage above or equal to the threshold.<br>
	 * 
	 * <strong>A log file with too many events will decrease
	 * performance!</strong>
	 * 
	 * @param log
	 *            {@link XLog}
	 * @param rule
	 *            Valid LTL rule like
	 *            <code>formula doThis(A:activity)= {...}</code>
	 * @param threshold
	 *            0 to 1
	 * @return
	 */
	public ArrayList<RuleModel> mineAll(XLog log, String rule, double threshold) {

		ArrayList<String> ac = logFilter.getAllEvents(log);
		Object[] objList = analyseRule(log, rule, ac);
		return filter(objList, threshold);
	}

	/**
	 * Generates all possible combinations of the rule with the activities
	 * provided. Uses LTLChecker to test all those rules. Returns
	 * {@link RuleModel}'s with coverage above or equal to the threshold.
	 * 
	 * @param log
	 *            {@link XLog}
	 * @param rule
	 *            Valid LTL rule like
	 *            <code>formula doThis(A:activity)= {...}</code>
	 * @param threshold
	 *            0 to 1
	 * @param eventNames
	 *            Creates rule combinations with all these {@link String}'s.
	 * @return
	 */
	public ArrayList<RuleModel> mine(XLog log, String rule, double threshold,
			String[] eventNames) {

		ArrayList<String> ac = logFilter.getActivities(log, eventNames);
		Object[] objList = analyseRule(log, rule, ac);
		return filter(objList, threshold);
	}

	/**
	 * This is the first part of the miner. It creates all possible combinations
	 * of rules using the input rule as a template. It then uses the
	 * {@link LTLChecker} to create an output.
	 * 
	 * @param log
	 *            {@link XLog}
	 * @param rule
	 *            Valid LTL formula with no default arguments.
	 * @return Object [] form {@link LTLChecker}
	 */
	public Object[] analyseRule(XLog log, String rule,
			ArrayList<String> activities) {
		String[] rules = creator.createCombinations(rule, activities);
		String modelString = createLTLModel(rules);

		addRulesToChecker(rules);
		LTLModel model = new LTLModel();
		model.setFile(modelString);
		return checker.analyse(log, model);
	}

	private void addRulesToChecker(String[] rules) {
		Vector<String> selectedRules = new Vector<>();
		// Can do this because rules were just renamed and there are exactly
		// this many
		for (int i = 0; i < rules.length; i++) {
			selectedRules.add(CombinationCreator.ruleName(i));
		}
		checker.setSelectedRules(selectedRules);

	}

	/**
	 * Creates a proper LTL model from all the input rules. Adds necessary stuff
	 * to the front.
	 * 
	 * @param rules
	 * @return LTL model ready to be analysed
	 */
	private String createLTLModel(String[] rules) {
		StringBuilder model = new StringBuilder();
		// Add more if necessary
		model.append("set ate.WorkflowModelElement;");
		model.append("rename ate.WorkflowModelElement as activity; \n");
		for (String string : rules) {
			model.append(string);
			model.append("\n");
		}
		return model.toString();
	}

	/**
	 * Gets {@link RuleModel} from {@link CheckResultObject} and returns the
	 * ones with coverage greater or equal to the threshold.
	 * 
	 * @param objList
	 *            {@link Object} array containing {@link CheckResultObject}
	 * @param threshold
	 *            double
	 * @return
	 */
	private ArrayList<RuleModel> filter(Object[] objList, double threshold) {
		CheckResultObject output = (CheckResultObject) objList[0];
		ArrayList<RuleModel> result = new ArrayList<>();

		for (RuleModel r : output.getRules()) {
			// TODO double comparison isn't good. Do with delta
			if (r.getCoverage() >= threshold) {
				result.add(r);
			}

		}
		return result;
	}

}
