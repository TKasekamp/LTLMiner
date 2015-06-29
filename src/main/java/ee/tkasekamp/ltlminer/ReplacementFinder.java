package ee.tkasekamp.ltlminer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that finds the specific replacements for LTL query parameters.
 *
 */
public class ReplacementFinder {
	private static Pattern pattern = Pattern.compile("\\?(\\w+)\\{(.*?)\\}");

	public static ArrayList<HashMap<String, String[]>> find(String queries) {
		ArrayList result = new ArrayList<>();
		String[] s = queries.split(";");
		for (String string : s) {
			result.add(getReplacements(string));
		}
		return result;
	}

	public static HashMap<String, String[]> getReplacements(String query) {
		Matcher m = pattern.matcher(query);
		HashMap<String, String[]> replacements = new HashMap<>();

		while (m.find()) {
			String parameter = m.group(1);
			String[] array = m.group(2).replace(" ", "").split(",");
			replacements.put(parameter, array);
		}
		return replacements;
	}

}
