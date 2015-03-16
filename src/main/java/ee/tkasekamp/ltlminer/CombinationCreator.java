package ee.tkasekamp.ltlminer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CombinationCreator {

	public ArrayList<String> createCombinations(String rule,
			ArrayList<String> activities) {
		// regex to find the places to work with
		// \w(:)\s(activity)
		ArrayList<String> rules = new ArrayList<>();

		ArrayList<String[]> combos = new ArrayList<>();
		int k = numberOfArguments(rule);
		String[] input = activities.toArray(new String[activities.size()]);
		System.out.println(input);
		String[] branch = new String[k];
		combine(input, k, 0, branch, 0, combos);
		System.out.println(combos.size());

		// Actually combine stuff
		for (String[] strings : combos) {
			for (int i = 0; i < strings.length; i++) {
				rules.add(rule.replaceAll("\\w(:)\\s(activity)", "$0" + " : \""
						+ strings[i] + "\""));
			}
		}

		return rules;
	}

	// http://exceptional-code.blogspot.com/2012/09/generating-all-permutations.html
	private void combine(String[] arr, int k, int startId, String[] branch,
			int numElem, ArrayList<String[]> combos) {
		if (numElem == k) {
			combos.add(branch);
			return;
		}

		for (int i = startId; i < arr.length; ++i) {
			branch[numElem++] = arr[i];
			combine(arr, k, ++startId, branch, numElem, combos);
			--numElem;
		}
	}

	private int numberOfArguments(String rule) {
		Pattern pattern = Pattern.compile("\\w(:)\\s(activity)");
		Matcher matcher = pattern.matcher(rule);

		int count = 0;
		while (matcher.find())
			count++;
		return count;
	}

}
