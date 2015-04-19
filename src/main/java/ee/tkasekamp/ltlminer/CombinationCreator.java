package ee.tkasekamp.ltlminer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CombinationCreator {
	private Pattern argumentPattern;
	private Pattern ruleNamePattern;

	/** true by default */
	private boolean repetitions;

	/**
	 * Default constructor. Create combinations with repetitions.
	 */
	public CombinationCreator() {
		this(true);
	}

	public CombinationCreator(boolean repetitions) {
		argumentPattern = Pattern.compile("\\w+(\\s)*(:)(\\s)*\\w+");
		ruleNamePattern = Pattern.compile("(formula)(\\s)*(\\w)+(\\()");
		this.repetitions = repetitions;
	}

	public String[] createRule(String rule, ArrayList<String> activities) {
		ArrayList<String> finishedRules = createCombinations(rule, activities);

		// Rename rules
		finishedRules = renameRules(finishedRules);

		return finishedRules.toArray(new String[finishedRules.size()]);
	}

	public String[] createRules(String[] ltlformulas,
			ArrayList<String> activities) {
		ArrayList<String> finishedRules = new ArrayList<>();

		for (int i = 0; i < ltlformulas.length; i++) {
			finishedRules
					.addAll(createCombinations(ltlformulas[i], activities));
		}

		finishedRules = renameRules(finishedRules);
		return finishedRules.toArray(new String[finishedRules.size()]);
	}

	private ArrayList<String> createCombinations(String rule,
			ArrayList<String> activities) {
		ArrayList<String[]> combos = new ArrayList<>();
		int k = numberOfArguments(rule);
		String[] input = activities.toArray(new String[activities.size()]);
		String[] branch = new String[k];

		if (repetitions)
			combine(input, k, branch, 0, combos);
		else
			combineNoRepetitions(input, k, branch, 0, combos);

		ArrayList<String> rules = new ArrayList<>();
		// Actually combine stuff

		for (int i = 0; i < combos.size(); i++) {
			rules.add(replaceText(rule, combos.get(i)));
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

	/**
	 * Generates all possible combinations from arr with lenght k. Does not
	 * allow repetiton in the finshed arrays.
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
	public void combineNoRepetitions(String[] arr, int k, String[] branch,
			int numElem, ArrayList<String[]> combos) {
		if (numElem == k) {
			combos.add(createCopy(branch));
			return;
		}

		for (int i = 0; i < arr.length; ++i) {
			if (!arrayIncludesElement(branch, arr[i])) {
				branch[numElem++] = arr[i];
				combineNoRepetitions(arr, k, branch, numElem, combos);
				--numElem;
			}
		}
	}

	public int numberOfArguments(String rule) {
		Matcher matcher = argumentPattern.matcher(rule);

		int count = 0;
		while (matcher.find())
			count++;
		return count;
	}

	public ArrayList<String> renameRules(ArrayList<String> rules) {
		return renameRules(rules, 0);
	}

	public ArrayList<String> renameRules(ArrayList<String> rules, int startIndex) {
		Matcher matcher;

		for (int i = startIndex; i < rules.size(); i++) {
			matcher = ruleNamePattern.matcher(rules.get(i));
			rules.set(i, matcher.replaceAll("formula " + ruleName(i) + "("));
		}
		return rules;
	}

	public static String ruleName(int i) {
		return "rule_" + i;
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
		Matcher m = argumentPattern.matcher(rule);
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

	/**
	 * Checks if the branch includes the element. Returns false if the first
	 * element is null. Otherwise checks if first element equals to input
	 * element.
	 * 
	 * @param branch
	 * @param element
	 * @return
	 */
	private boolean arrayIncludesElement(String[] branch, String element) {
		if (branch[0] == null)
			return false;
		return branch[0].equals(element);
	}

	public boolean isRepetitions() {
		return repetitions;
	}

	public void setRepetitions(boolean repetitions) {
		this.repetitions = repetitions;
	}

}
