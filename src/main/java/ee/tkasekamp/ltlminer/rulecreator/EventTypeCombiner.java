package ee.tkasekamp.ltlminer.rulecreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventTypeCombiner extends AbstractCombiner {
	private Pattern leftActivity = Pattern
			.compile("(activity)(\\s)*(==|!=)(\\s)*(\\w+)");

	@Override
	public ArrayList<String> combine(String ruleTemplate,
			ArrayList<String> replacements, ArrayList<String> ruleParameters,
			HashMap<String, String[]> suitableReplacements) {
		// Finding out what to replace with what
		int k = ruleParameters.size();
		// Combinations with repetitions
		ArrayList<String[]> eventCombinations = createCombinations(
				replacements, k);
		
		if(suitableReplacements != null) 
			eventCombinations =filterCombinations(eventCombinations, ruleParameters, suitableReplacements);
		
		ArrayList<HashMap<String, String>> whatToReplaceList = whatToReplaceWithWhat(
				eventCombinations, ruleParameters);

		// Now the combining
		ArrayList<String> finishedRules = new ArrayList<>();
		Matcher m = leftActivity.matcher(ruleTemplate);
		for (HashMap<String, String> whatToReplace : whatToReplaceList) {
			finishedRules.add(replaceInRule(ruleTemplate, whatToReplace, m));
		}

		return finishedRules;
	}

	@Override
	public ArrayList<String> combine(String ruleTemplate,
			ArrayList<String> replacements, ArrayList<String> ruleParameters) {
		return combine(ruleTemplate, replacements, ruleParameters, null);
	}

	@Override
	protected String replaceInRule(String ruleTemplate,
			HashMap<String, String> whatToReplace, Matcher m) {
		m.reset(ruleTemplate);
		StringBuffer sb = new StringBuffer(ruleTemplate.length());

		while (m.find()) {
			String parameter = m.group(5);
			String operator = m.group(3);
			String text = "(activity " + operator + " " + parameter
					+ " /\\ eventtype == \"" + whatToReplace.get(parameter)
					+ "\")";
			m.appendReplacement(sb, Matcher.quoteReplacement(text));

		}
		m.appendTail(sb);
		return sb.toString();
	}
}
