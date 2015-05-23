package ee.tkasekamp.ltlminer;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

import org.deckfour.xes.model.XLog;
import org.processmining.plugins.ltlchecker.CheckResultObject;
import org.processmining.plugins.ltlchecker.LTLChecker;
import org.processmining.plugins.ltlchecker.RuleModel;
import org.processmining.plugins.ltlchecker.model.LTLModel;

import ee.tkasekamp.ltlminer.rulecreator.RuleCreator;

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
	private RuleCreator ruleCreator;
	private LTLChecker checker;
	private LogFilter logFilter;

	public LTLMiner() {
		ruleCreator = new RuleCreator();
		checker = new LTLChecker();
		logFilter = new LogFilter();
	}

	public ArrayList<RuleModel> mine(XLog log, ArrayList<String> ruleTemplates,
			double threshold) {
		ArrayList<String> events = logFilter.getAllEvents(log);
		ArrayList<String> rules = ruleCreator.generateRules(ruleTemplates,
				events);

		Object[] objList = checkRules(log, rules);
		return filter(objList, threshold);
	}

	public ArrayList<RuleModel> mineWithEventTypes(XLog log,
			ArrayList<String> ruleTemplates, double threshold) {
		ArrayList<String> events = logFilter.getAllEvents(log);
		ArrayList<String> eventTypes = logFilter.getEventTypes(log);
		ArrayList<String> rules = ruleCreator.generateRules(ruleTemplates,
				events, eventTypes);

		Object[] objList = checkRules(log, rules);
		return filter(objList, threshold);
	}
	
	public ArrayList<RuleModel> mine(XLog log, Properties properties) {
		// TODO
		return null;
	}

	private Object[] checkRules(XLog log, ArrayList<String> rules) {
		String modelString = LTLFileCreator.createLTLModel(rules);
		addRulesToChecker(rules);
		LTLModel model = new LTLModel();
		model.setFile(modelString);
		return checker.analyse(log, model);
	}

	private void addRulesToChecker(ArrayList<String> rules) {
		Vector<String> selectedRules = new Vector<>();
		// Can do this because rules were just renamed and there are exactly
		// this many
		for (int i = 0; i < rules.size(); i++) {
			selectedRules.add(ruleCreator.ruleName(i));
		}
		checker.setSelectedRules(selectedRules);

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
	private static ArrayList<RuleModel> filter(Object[] objList,
			double threshold) {
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
