package ee.tkasekamp.ltlminer.rulecreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityCombiner {
	private static Pattern argumentPattern = Pattern
			.compile("(\\w+)(\\s)*(:)(\\s)*(activity)");

	public static ArrayList<String> combineActivities(
			ArrayList<String> ruleTemplates, ArrayList<String> activities,
			ArrayList<String> parameters, HashMap<String, String[]> replacements) {
		// Finding out what to replace with what
		int k = parameters.size();
		// Combinations with repetitions
		ArrayList<String[]> activityCombinations = createCombinations(
				activities, k);

		// Filter the combinations with replacements if necessary
		if (replacements != null)
			activityCombinations = filterCombinations(activityCombinations,
					parameters, replacements);

		ArrayList<HashMap<String, String>> whatToReplaceList = whatToReplaceWithWhat(
				activityCombinations, parameters);

		// Now the combining
		ArrayList<String> finishedRules = new ArrayList<>();
		Matcher m;
		for (String ruleTemplate : ruleTemplates) {
			m = argumentPattern.matcher(ruleTemplate);
			for (HashMap<String, String> whatToReplace : whatToReplaceList) {
				finishedRules.add(replaceEventTypes(ruleTemplate,
						whatToReplace, m));
			}
		}

		return finishedRules;
	}

	public static ArrayList<String> combineActivity(String ruleTemplate,
			ArrayList<String> activities, ArrayList<String> parameters,
			HashMap<String, String[]> replacements) {
		ArrayList<String> ruleTemplates = new ArrayList<>();
		ruleTemplates.add(ruleTemplate);
		return combineActivities(ruleTemplates, activities, parameters,
				replacements);
	}

	public static ArrayList<String> combineActivities(
			ArrayList<String> ruleTemplates, ArrayList<String> activities,
			ArrayList<String> parameters) {
		return combineActivities(ruleTemplates, activities, parameters, null);
	}

	public static ArrayList<String> combineActivity(String ruleTemplate,
			ArrayList<String> activities, ArrayList<String> parameters) {
		return combineActivity(ruleTemplate, activities, parameters, null);

	}

	/**
	 * Magic happens here.
	 */
	@SuppressWarnings("rawtypes")
	private static ArrayList<String[]> filterCombinations(
			ArrayList<String[]> combos, ArrayList<String> parameters,
			HashMap<String, String[]> replacements) {
		int position;
		String[] suitable;
		Iterator it = replacements.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();

			try {
				position = parameters.indexOf(pair.getKey());
			} catch (NullPointerException e) {
				continue;
			}
			suitable = (String[]) pair.getValue();

			Iterator<String[]> it2 = combos.iterator();
			while (it2.hasNext()) {
				String[] currentCombo = it2.next();
				boolean ok = true;

				for (String replacement : suitable) {
					if (currentCombo[position].equals(replacement)) {
						ok = false;
						break;
					}
				}

				if (ok) {
					it2.remove();
				}
			}

		}
		return combos;
	}

	private static ArrayList<HashMap<String, String>> whatToReplaceWithWhat(
			ArrayList<String[]> activityCombinations,
			ArrayList<String> parameters) {
		ArrayList<HashMap<String, String>> whatToReplaceList = new ArrayList<HashMap<String, String>>();

		for (String[] combo : activityCombinations) {
			HashMap<String, String> a = new HashMap<String, String>();
			for (int i = 0; i < combo.length; i++) {
				a.put(parameters.get(i), combo[i]);
			}
			whatToReplaceList.add(a);
		}
		return whatToReplaceList;
	}

	private static ArrayList<String[]> createCombinations(
			ArrayList<String> activities, int k) {
		ArrayList<String[]> activityCombinations = new ArrayList<>();

		String[] input = activities.toArray(new String[activities.size()]);
		String[] branch = new String[k];

		CombineUtil.combineNoRepetitions(input, k, branch, 0,
				activityCombinations);
		return activityCombinations;
	}

	private static String replaceEventTypes(String ruleTemplate,
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
