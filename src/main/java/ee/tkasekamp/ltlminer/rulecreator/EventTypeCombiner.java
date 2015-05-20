package ee.tkasekamp.ltlminer.rulecreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventTypeCombiner {
	private static Pattern leftActivity = Pattern
			.compile("(activity)(\\s)*(==|!=)(\\s)*(\\w+)");

	public static ArrayList<String> combineEventTypes(String ruleTemplate,
			ArrayList<String> eventTypes, ArrayList<String> parameters) {

		// Finding out what to replace with what
		int k = parameters.size();
		// Combinations with repetitions
		ArrayList<String[]> eventCombinations = createCombinations(eventTypes,
				k);

		ArrayList<HashMap<String, String>> whatToReplaceList = whatToReplaceWithWhat(
				eventCombinations, parameters);

		// Now the combining
		ArrayList<String> finishedRules = new ArrayList<>();
		Matcher m = leftActivity.matcher(ruleTemplate);
		for (HashMap<String, String> whatToReplace : whatToReplaceList) {
			finishedRules
					.add(replaceEventTypes(ruleTemplate, whatToReplace, m));
		}

		return finishedRules;
	}

	private static ArrayList<HashMap<String, String>> whatToReplaceWithWhat(
			ArrayList<String[]> eventCombinations, ArrayList<String> parameters) {
		ArrayList<HashMap<String, String>> whatToReplaceList = new ArrayList<HashMap<String, String>>();

		for (String[] combo : eventCombinations) {
			HashMap<String, String> a = new HashMap<String, String>();
			for (int i = 0; i < combo.length; i++) {
				a.put(parameters.get(i), combo[i]);
			}
			whatToReplaceList.add(a);
		}
		return whatToReplaceList;
	}

	private static ArrayList<String[]> createCombinations(
			ArrayList<String> eventTypes, int k) {
		ArrayList<String[]> eventCombinations = new ArrayList<>();

		String[] input = eventTypes.toArray(new String[eventTypes.size()]);
		String[] branch = new String[k];

		GenericCombiner.combineNoRepetitions(input, k, branch, 0,
				eventCombinations);
		return eventCombinations;
	}

	private static String replaceEventTypes(String ruleTemplate,
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
