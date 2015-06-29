package ee.tkasekamp.ltlminer.rulecreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleCreator {
	private Pattern argumentPattern;
	private Pattern ruleNamePattern;
	private ActivityCombiner acCombiner;
	private EventTypeCombiner evCombiner;

	public RuleCreator() {
		argumentPattern = Pattern.compile("(\\w+)(\\s)*(:)(\\s)*(activity)");
		ruleNamePattern = Pattern.compile("(formula)(\\s)*(\\w)+(\\()");
		acCombiner = new ActivityCombiner();
		evCombiner = new EventTypeCombiner();

	}

	public ArrayList<String> generateRules(ArrayList<String> ruleTemplates,
			ArrayList<String> activities, ArrayList<String> eventTypes) {
		ArrayList<String> finishedRules = new ArrayList<>();

		for (String ruleTemplate : ruleTemplates) {
			finishedRules.addAll(createRule(ruleTemplate, activities,
					eventTypes, null));
		}

		finishedRules = renameRules(finishedRules);

		return finishedRules;
	}

	public ArrayList<String> generateRules(ArrayList<String> ruleTemplates,
			ArrayList<String> activities) {
		return generateRules(ruleTemplates, activities, null);
	}

	public ArrayList<String> generateRules(String ruleTemplate,
			ArrayList<String> activities) {
		return generateRules(ruleTemplate, activities, null, null);
	}

	public ArrayList<String> generateRules(String ruleTemplate,
			ArrayList<String> activities, ArrayList<String> eventTypes) {
		ArrayList<String> ruleTemplates = new ArrayList<>();
		ruleTemplates.add(ruleTemplate);
		return generateRules(ruleTemplates, activities, eventTypes);
	}

	public ArrayList<String> generateRules(String ruleTemplate,
			ArrayList<String> activities, ArrayList<String> eventTypes,
			HashMap<String, String[]> suitableActivityReplacements) {
		ArrayList<String> finishedRules = new ArrayList<>();

		finishedRules.addAll(createRule(ruleTemplate, activities, eventTypes,
				suitableActivityReplacements));

		finishedRules = renameRules(finishedRules);

		return finishedRules;
	}


	/**
	 * If eventTypes is not used, just put null. The number of items in ruleTemplates and suitableActivityReplacements must be equal. 
	 * @param ruleTemplates
	 * @param activities
	 * @param eventTypes
	 * @param suitableActivityReplacements ArrayList of HashMaps. Yeah.
	 * @return
	 */
	public ArrayList<String> generateRules(ArrayList<String> ruleTemplates,
			ArrayList<String> activities, ArrayList<String> eventTypes,
			ArrayList<HashMap<String, String[]>> suitableActivityReplacements) {
		ArrayList<String> finishedRules = new ArrayList<>();

		for (int i = 0; i < ruleTemplates.size(); i++) {
			finishedRules.addAll(createRule(ruleTemplates.get(i), activities,
					eventTypes, suitableActivityReplacements.get(i)));
		}

		finishedRules = renameRules(finishedRules);

		return finishedRules;
	}

	public ArrayList<String> generateRules(String ruleTemplate,
			ArrayList<String> activities,
			HashMap<String, String[]> suitableActivityReplacements) {

		return generateRules(ruleTemplate, activities, null,
				suitableActivityReplacements);

	}

	private ArrayList<String> createRule(String ruleTemplate,
			ArrayList<String> activities, ArrayList<String> eventTypes,
			HashMap<String, String[]> suitableActivityReplacements) {
		ArrayList<String> finishedRules = new ArrayList<>();

		ArrayList<String> ruleParameters = getParameters(ruleTemplate);

		if (eventTypes != null) {
			finishedRules = evCombiner.combine(ruleTemplate, eventTypes,
					ruleParameters);
			finishedRules = acCombiner.combine(finishedRules, activities,
					ruleParameters, suitableActivityReplacements);
		} else {
			finishedRules = acCombiner.combine(ruleTemplate, activities,
					ruleParameters, suitableActivityReplacements);
		}

		return finishedRules;
	}

	/**
	 * Returns the parameters of the ruleTemplate. For example in "activity : A"
	 * it will return a list containing A.
	 * 
	 * @param ruleTemplate
	 * @return
	 */
	private ArrayList<String> getParameters(String ruleTemplate) {
		Matcher matcher = argumentPattern.matcher(ruleTemplate);
		ArrayList<String> parameters = new ArrayList<>();

		while (matcher.find()) {
			String text = matcher.group(1);
			parameters.add(text);
		}
		return parameters;
	}

	private ArrayList<String> renameRules(ArrayList<String> rules) {
		Matcher matcher;

		for (int i = 0; i < rules.size(); i++) {
			matcher = ruleNamePattern.matcher(rules.get(i));
			rules.set(i, matcher.replaceAll("formula " + ruleName(i) + "("));
		}
		return rules;
	}

	public String ruleName(int i) {
		return "rule_" + i;
	}

}
