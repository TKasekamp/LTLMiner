package ee.tkasekamp.ltlminer.rulecreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;

public abstract class AbstractCombiner {

	public abstract ArrayList<String> combine(String ruleTemplate,
			ArrayList<String> replacements, ArrayList<String> ruleParameters,
			HashMap<String, String[]> suitableReplacements);

	public abstract ArrayList<String> combine(String ruleTemplate,
			ArrayList<String> replacements, ArrayList<String> ruleParameters);

	protected abstract String replaceInRule(String ruleTemplate,
			HashMap<String, String> whatToReplace, Matcher m);

	protected ArrayList<HashMap<String, String>> whatToReplaceWithWhat(
			ArrayList<String[]> combinations, ArrayList<String> ruleParameters) {
		ArrayList<HashMap<String, String>> whatToReplaceList = new ArrayList<HashMap<String, String>>();

		for (String[] combo : combinations) {
			HashMap<String, String> a = new HashMap<String, String>();
			for (int i = 0; i < combo.length; i++) {
				a.put(ruleParameters.get(i), combo[i]);
			}
			whatToReplaceList.add(a);
		}
		return whatToReplaceList;
	}

	protected ArrayList<String[]> createCombinations(
			ArrayList<String> replacements, int k) {
		ArrayList<String[]> combinations = new ArrayList<>();

		String[] input = replacements.toArray(new String[replacements.size()]);
		String[] branch = new String[k];

		CombineUtil.combineNoRepetitions(input, k, branch, 0, combinations);
		return combinations;
	}

	/**
	 * Magic happens here.
	 */
	@SuppressWarnings("rawtypes")
	protected ArrayList<String[]> filterCombinations(
			ArrayList<String[]> combinations, ArrayList<String> ruleParameters,
			HashMap<String, String[]> suitableReplacements) {
		int position;
		String[] suitable;
		Iterator it = suitableReplacements.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();

			try {
				position = ruleParameters.indexOf(pair.getKey());
			} catch (NullPointerException e) {
				continue;
			}
			suitable = (String[]) pair.getValue();

			Iterator<String[]> it2 = combinations.iterator();
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
		return combinations;
	}
}
