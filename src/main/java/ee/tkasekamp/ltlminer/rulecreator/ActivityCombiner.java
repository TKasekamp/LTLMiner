package ee.tkasekamp.ltlminer.rulecreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityCombiner extends AbstractCombiner {
	private Pattern argumentPattern = Pattern
			.compile("(\\w+)(\\s)*(:)(\\s)*(activity)");

	public ArrayList<String> combine(ArrayList<String> ruleTemplates,
			ArrayList<String> replacements, ArrayList<String> ruleParameters,
			HashMap<String, String[]> suitableReplacements) {
		ArrayList<HashMap<String, String>> whatToReplaceList = createReplacementList(
				replacements, ruleParameters, suitableReplacements);

		// Now the combining
		ArrayList<String> finishedRules = new ArrayList<>();
		Matcher m;
		for (String ruleTemplate : ruleTemplates) {
			m = argumentPattern.matcher(ruleTemplate);
			for (HashMap<String, String> whatToReplace : whatToReplaceList) {
				finishedRules
						.add(replaceInRule(ruleTemplate, whatToReplace, m));
			}
		}

		return finishedRules;
	}

	public ArrayList<String> combine(ArrayList<String> ruleTemplates,
			ArrayList<String> replacements, ArrayList<String> ruleParameters) {
		return combine(ruleTemplates, replacements, ruleParameters, null);
	}

	@Override
	public ArrayList<String> combine(String ruleTemplate,
			ArrayList<String> replacements, ArrayList<String> ruleParameters,
			HashMap<String, String[]> suitableReplacements) {
		ArrayList<String> ruleTemplates = new ArrayList<>();
		ruleTemplates.add(ruleTemplate);
		return combine(ruleTemplates, replacements, ruleParameters,
				suitableReplacements);
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
			String text = m.group(0);
			String parameter = m.group(1);
			text += " : \"" + whatToReplace.get(parameter) + "\"";
			m.appendReplacement(sb, Matcher.quoteReplacement(text));

		}
		m.appendTail(sb);
		return sb.toString();
	}
}
