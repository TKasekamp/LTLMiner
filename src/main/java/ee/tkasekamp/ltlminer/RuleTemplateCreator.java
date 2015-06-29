package ee.tkasekamp.ltlminer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleTemplateCreator {
	private static Pattern queryParameter = Pattern.compile("\\?(\\w+)");

	public static ArrayList<String> createTemplates(String queries) {
		ArrayList<String> templates = new ArrayList<>();

		String[] s = queries.split(";");
		for (String string : s) {
			templates.add(bla(string));
		}
		return templates;
	}

	private static String bla(String thing) {
		thing = thing.replaceAll("\"", "");
		thing = removeSpecificParameters(thing);
		HashSet<String> a = getParameters(thing);

		thing = doSomething(thing);
		return makeProper(thing, a);

	}

	private static String makeProper(String thing, HashSet<String> parameters) {
		StringBuilder s = new StringBuilder();

		s.append("formula rule(");
		for (String string : parameters) {
			s.append(string + ": activity,");
		}
		if (parameters.size() != 0) {
			s.deleteCharAt(s.lastIndexOf(","));
		}
		s.append(") := {} \n");
		s.append(thing);
		s.append(";");

		return s.toString();
	}

	private static HashSet<String> getParameters(String query) {
		Matcher m = queryParameter.matcher(query);
		HashSet<String> asasdf = new HashSet<>();

		while (m.find()) {
			String parameter = m.group(1);
			asasdf.add(parameter);

		}

		return asasdf;
	}

	private static String doSomething(String query) {
		Matcher m = queryParameter.matcher(query);

		StringBuffer sb = new StringBuffer(query.length());

		while (m.find()) {
			String parameter = m.group(1);
			String text = "activity == " + parameter;
			m.appendReplacement(sb, Matcher.quoteReplacement(text));

		}
		m.appendTail(sb);
		return sb.toString();
	}

	private static String removeSpecificParameters(String query) {
		return query.replaceAll("\\{(.*?)\\}", "");
	}
}
