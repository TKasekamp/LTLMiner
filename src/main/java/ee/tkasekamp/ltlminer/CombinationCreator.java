package ee.tkasekamp.ltlminer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CombinationCreator {
	private Pattern argumentPattern;
	private Pattern ruleNamePattern;

	/** false by default */
	private boolean repetitions;

	private HashMap<String, Integer> positions;
	private HashMap<String, String[]> eventReplacement;

	/**
	 * Default constructor. Do not Create combinations with repetitions.
	 */
	public CombinationCreator() {
		this(false);
	}

	public CombinationCreator(boolean repetitions) {
		argumentPattern = Pattern.compile("(\\w+)(\\s)*(:)(\\s)*(\\w+)");
		ruleNamePattern = Pattern.compile("(formula)(\\s)*(\\w)+(\\()");
		this.repetitions = repetitions;
	}

	public String[] createRule(String rule, ArrayList<String> activities) {
		ArrayList<String> finishedRules = createCombinations(rule, activities);

		// Rename rules
		finishedRules = renameRules(finishedRules);

		return finishedRules.toArray(new String[finishedRules.size()]);
	}

	public String[] createRule(String rule, ArrayList<String> activities,
			HashMap<String, String[]> eventReplacement) {
		positions = new HashMap<String, Integer>();
		createPositions(rule);
		// System.out.println(positions);
		this.eventReplacement = eventReplacement;

		ArrayList<String> finishedRules = createCombinations(rule, activities);

		// Rename rules
		finishedRules = renameRules(finishedRules);

		return finishedRules.toArray(new String[finishedRules.size()]);
	}

	public String[] createRules(String[] ltlFormulas,
			ArrayList<String> activities,
			HashMap<String, String[]> eventReplacement) {
		ArrayList<String> finishedRules = new ArrayList<>();

		for (String rule : ltlFormulas) {
			// System.out.println("Process rule");
			String[] bla = createRule(rule, activities, eventReplacement);

			for (String string : bla) {
				finishedRules.add(string);
			}
			// System.out.println("Main size: " + finishedRules.size());

		}

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

	private void createPositions(String rule) {
		Matcher m = argumentPattern.matcher(rule);

		int counter = 0;
		while (m.find()) {
			String text = m.group(1);
			positions.put(text, counter);
			counter++;
		}

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

		// System.out.println(combos.size());
		if (positions != null)
			combos = filterCombinations(combos);

		ArrayList<String> rules = new ArrayList<>();
		// Actually combine stuff

		for (int i = 0; i < combos.size(); i++) {
			rules.add(replaceText(rule, combos.get(i)));
		}
		return rules;
	}

	private ArrayList<String[]> filterCombinations(ArrayList<String[]> combos) {
		// ArrayList<String[]> newCombos = new ArrayList<>();

		int position;
		String[] suitable;
		Iterator it = eventReplacement.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();

			try {
				position = positions.get(pair.getKey());
			} catch (NullPointerException e) {
				continue;
			}
			suitable = (String[]) pair.getValue();

			Iterator<String[]> it2 = combos.iterator();
			// System.out.println("Combos size " + combos.size());
			while (it2.hasNext()) {
				// System.out.println(it2.next());
				String[] currentCombo = it2.next();
				boolean ok = true;

				for (String fuckThisThing : suitable) {
					// System.out.println("Checking pos " + position + ","
					// + currentCombo[position] + " and " + fuckThisThing);
					if (currentCombo[position].equals(fuckThisThing)) {
						ok = false;
						break;
					}
				}

				if (ok) {
					// System.out.println("Not ok " + currentCombo[0] + " "
					// + currentCombo[1]);
					it2.remove();
				}
			}

			// System.out.println("Size " + combos.size());
		}
		return combos;
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
