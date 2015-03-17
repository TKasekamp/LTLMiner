package ee.tkasekamp.ltlminer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CombinationCreator {

	public ArrayList<String> createCombinations(String rule,
			ArrayList<String> activities) {

		ArrayList<String> rules = new ArrayList<>();

		ArrayList<String[]> combos = new ArrayList<>();
		int k = numberOfArguments(rule);
		String[] input = activities.toArray(new String[activities.size()]);
		String[] branch = new String[k];
		combine(input, k, branch, 0, combos);

		// Actually combine stuff
		for (String[] strings : combos) {
			rules.add(replaceText(rule, strings));
		}

		return rules;
	}

	/**
	 * Generates all possible combinations from arr with lenght k. Allows
	 * repetiton in the finshed arrays.
	 * <code>http://exceptional-code.blogspot.com/2012/09/generating-all-permutations.html</code>
	 * 
	 * @param arr
	 *            Where to take arguments from
	 * @param k
	 *            How deep to go
	 * @param branch
	 *            Current working array
	 * @param numElem
	 *            Current depth
	 * @param combos
	 *            Holds finished arrays
	 */
	public void combine(String[] arr, int k, String[] branch, int numElem,
			ArrayList<String[]> combos) {
		if (numElem == k) {
			combos.add(createCopy(branch));
			return;
		}

		for (int i = 0; i < arr.length; ++i) {
			branch[numElem++] = arr[i];
			combine(arr, k, branch, numElem, combos);
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

	/**
	 * Adds a default activity for every rule parameter. Finds places like
	 * <code>A : activity</code> and replaces it with something like
	 * <code>A : activity : "A"</code>.
	 * 
	 * @param rule
	 * @param combination
	 * @return
	 */
	private String replaceText(String rule, String[] combination) {
		Pattern patt = Pattern.compile("\\w(:)\\s(activity)");
		Matcher m = patt.matcher(rule);
		StringBuffer sb = new StringBuffer(rule.length());

		int counter = 0;
		while (m.find()) {
			String text = m.group(0);

			text += " : \"" + combination[counter] + "\"";
			m.appendReplacement(sb, Matcher.quoteReplacement(text));
			counter++;
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * Creates a deep copy of a String array.
	 * 
	 * @param original
	 *            String []
	 * @return Copy of the original
	 */
	private String[] createCopy(String[] original) {
		String[] copy = new String[original.length];
		for (int i = 0; i < original.length; i++) {
			copy[i] = original[i];
		}

		return copy;
	}

}
