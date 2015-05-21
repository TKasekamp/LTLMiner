package ee.tkasekamp.ltlminer.rulecreator;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class ActivityCombinerTest {
	private ActivityCombiner acCombiner;
	
	@Before
	public void setup() {
		acCombiner = new ActivityCombiner();
	}
	@Test
	public void combineActivities() {
		String rule = "formula two( A: activity, B: activity) = {} \n"
				+ "( <>( event == A ) /\\ <>( event == B) );";

		ArrayList<String> parameters = new ArrayList<>(Arrays.asList("A", "B"));
		ArrayList<String> activities = new ArrayList<>(Arrays.asList("A", "B",
				"C", "D", "E"));

		ArrayList<String> rules = acCombiner.combine(rule,
				activities, parameters);

		assertEquals("Number of combinations", 20, rules.size());

	}

	@Test
	public void combineActivities2() {
		String rule = "formula two( A: activity, B: activity) = {} \n"
				+ "( <>( (event == A /\\ eventtype == \"complete\") ) /\\ <>( (event == B /\\ eventtype == \"start\")) );";
		String rule2 = "formula two( A: activity, B: activity) = {} \n"
				+ "( <>( (event == A /\\ /\\ eventtype == \"start\") ) /\\ <>( (event == B /\\ eventtype == \"complete\")) );";

		ArrayList<String> parameters = new ArrayList<>(Arrays.asList("A", "B"));
		ArrayList<String> activities = new ArrayList<>(Arrays.asList("A", "B",
				"C", "D", "E"));
		ArrayList<String> ruleTemplates = new ArrayList<>(Arrays.asList(rule,
				rule2));

		ArrayList<String> rules = acCombiner.combine(
				ruleTemplates, activities, parameters);

		assertEquals("Number of combinations", 40, rules.size());

	}

	@Test
	public void specificReplacementsTest() {
		String rule = "formula two( A: activity, B: activity) = {} \n"
				+ "( <>( event == A ) /\\ <>( event == B) );";
		ArrayList<String> parameters = new ArrayList<>(Arrays.asList("A", "B"));
		ArrayList<String> activities = new ArrayList<>(Arrays.asList("A", "B",
				"C", "D", "E"));

		HashMap<String, String[]> replacements = new HashMap<>();
		replacements.put("A", new String[] { "C", "D", "A" });
		replacements.put("B", new String[] { "A", "B" });

		ArrayList<String> rules = acCombiner.combine(rule,
				activities, parameters, replacements);
		assertEquals("Number of combinations", 5, rules.size());

	}

}
