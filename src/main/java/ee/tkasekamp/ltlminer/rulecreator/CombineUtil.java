package ee.tkasekamp.ltlminer.rulecreator;

import java.util.ArrayList;

/**
 * Creates combinations of String elements. Not very generic
 * 
 * @author Tonis
 *
 */
public class CombineUtil {
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
	public static void combine(String[] arr, int k, String[] branch,
			int numElem, ArrayList<String[]> combos) {
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
	public static void combineNoRepetitions(String[] arr, int k,
			String[] branch, int numElem, ArrayList<String[]> combos) {
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

	/**
	 * Creates a deep copy of a String array.
	 * 
	 * @param original
	 *            String []
	 * @return Copy of the original
	 */
	private static String[] createCopy(String[] original) {
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
	private static boolean arrayIncludesElement(String[] branch, String element) {
		if (branch[0] == null)
			return false;
		return branch[0].equals(element);
	}
}
