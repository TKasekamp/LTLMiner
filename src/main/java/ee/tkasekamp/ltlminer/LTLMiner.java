package ee.tkasekamp.ltlminer;

import java.util.ArrayList;
import java.util.Vector;

import org.deckfour.xes.model.XLog;
import org.processmining.plugins.ltlchecker.CheckResultObject;
import org.processmining.plugins.ltlchecker.LTLChecker;
import org.processmining.plugins.ltlchecker.RuleModel;
import org.processmining.plugins.ltlchecker.model.LTLModel;

public class LTLMiner {
	CombinationCreator creator;
	LTLChecker checker;
	LogFilter logFilter;

	public LTLMiner() {
		creator = new CombinationCreator();
		checker = new LTLChecker();
		logFilter = new LogFilter();
	}

	/**
	 * The main method for LTLMiner.
	 * 
	 * @param log
	 * @param rule
	 * @param threshold
	 *            0 to 1
	 * @return
	 */
	public ArrayList<RuleModel> mine(XLog log, String rule, double threshold) {
		// get list of activities from log
		// create list of rules with createCombinations
		// preProcess rule list to create a LTL model
		// analyse LTL model with LTLChecker
		// take the output and add rules above the threshold to ArrayList
		// do something with this ArrayList

		Object[] objList = analyseRule(log, rule);
		CheckResultObject output = (CheckResultObject) objList[0];
		ArrayList<RuleModel> result = new ArrayList<>();

		for (RuleModel r : output.getRules()) {
			if (r.getCoverage() >= threshold) {
				result.add(r);
			}

		}
		return result;
	}

	/**
	 * This is the first part of the miner. It finds all activities from the log
	 * and creates all possible combinations of rules using the input rule as a
	 * template. It then uses the {@link LTLChecker} to create an output.
	 * 
	 * @param log
	 *            {@link XLog}
	 * @param rule
	 *            Valid LTL formula with no default arguments.
	 * @return Object [] form {@link LTLChecker}
	 */
	public Object[] analyseRule(XLog log, String rule) {
		ArrayList<String> activities = logFilter.getAllActivities(log);
		String[] rules = creator.createCombinations(rule, activities);
		String modelString = createLTLModel(rules);

		addRulesToChecker(rules);
		LTLModel model = new LTLModel();
		model.setFile(modelString);
		return checker.analyse(log, model);
	}

	// public

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

}
