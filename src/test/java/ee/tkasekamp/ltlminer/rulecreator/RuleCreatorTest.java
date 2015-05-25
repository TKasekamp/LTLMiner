package ee.tkasekamp.ltlminer.rulecreator;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class RuleCreatorTest {
	private RuleCreator ruleCreator;
	ArrayList<String> activities;

	@Before
	public void setUp() throws Exception {
		ruleCreator = new RuleCreator();
		activities = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
	}

	@Test
	public void generateRule1() {

		ArrayList<String> rules = ruleCreator
				.generateRules(rule1(), activities);
		assertEquals("Number of combinations", 20, rules.size());

		ArrayList<String> eventtypes = new ArrayList<>(
				Arrays.asList("complete"));
		rules = ruleCreator.generateRules(rule1(), activities, eventtypes);
		assertEquals("Number of combinations", 0, rules.size());

		eventtypes = new ArrayList<>(Arrays.asList("complete", "started"));
		rules = ruleCreator.generateRules(rule1(), activities, eventtypes);
		assertEquals("Number of combinations", 40, rules.size());

	}

	@Test
	public void generateRule2() {
		ArrayList<String> rules = ruleCreator
				.generateRules(rule2(), activities);
		assertEquals("Number of combinations", 5, rules.size());

		ArrayList<String> eventtypes = new ArrayList<>(
				Arrays.asList("complete"));
		rules = ruleCreator.generateRules(rule2(), activities, eventtypes);
		assertEquals("Number of combinations", 5, rules.size());

		eventtypes = new ArrayList<>(Arrays.asList("complete", "started"));
		rules = ruleCreator.generateRules(rule2(), activities, eventtypes);
		assertEquals("Number of combinations", 10, rules.size());

	}

	@Test
	public void generateRules1And2() {
		ArrayList<String> ruleTemplates = new ArrayList<>(Arrays.asList(
				rule1(), rule2()));

		ArrayList<String> rules = ruleCreator.generateRules(ruleTemplates,
				activities);
		assertEquals("Number of combinations", 25, rules.size());

		ArrayList<String> eventtypes = new ArrayList<>(
				Arrays.asList("complete"));
		rules = ruleCreator
				.generateRules(ruleTemplates, activities, eventtypes);
		assertEquals("Number of combinations", 5, rules.size());

		eventtypes = new ArrayList<>(Arrays.asList("complete", "started"));
		rules = ruleCreator
				.generateRules(ruleTemplates, activities, eventtypes);
		assertEquals("Number of combinations", 50, rules.size());

	}

	@Test
	public void generateRule3() {

		ArrayList<String> rules = ruleCreator
				.generateRules(rule3(), activities);
		assertEquals("Number of combinations", 20, rules.size());

		ArrayList<String> eventtypes = new ArrayList<>(
				Arrays.asList("complete"));
		rules = ruleCreator.generateRules(rule3(), activities, eventtypes);
		assertEquals("Number of combinations", 0, rules.size());

		eventtypes = new ArrayList<>(Arrays.asList("complete", "started"));
		rules = ruleCreator.generateRules(rule3(), activities, eventtypes);
		assertEquals("Number of combinations", 40, rules.size());
	}

	@Test
	public void generateRule3RealLog() {
		ArrayList<String> ac = new ArrayList<>(Arrays.asList("Unmatched",
				"Completed", "Queued", "Accepted"));
		ArrayList<String> ev = new ArrayList<>(Arrays.asList(
				"Awaiting Assignment", "Assigned", "Wait - Implementation",
				"In Call", "In Progress", "Wait - User", "Cancelled",
				"Unmatched", "Closed", "Resolved", "Wait", "Wait - Customer",
				"Wait - Vendor"));
		ArrayList<String> rules = ruleCreator.generateRules(rule3(), ac, ev);
		assertEquals("Number of combinations", 1872, rules.size());
	}

	@Test
	public void generateWithReplacementRule1() {
		HashMap<String, String[]> replacements = new HashMap<>();
		replacements.put("A", new String[] { "C", "D", "A" });
		replacements.put("BSDFAD", new String[] { "B" });

		ArrayList<String> rules = ruleCreator.generateRules(rule1(),
				activities, replacements);
		assertEquals("Number of combinations", 3, rules.size());

		ArrayList<String> eventtypes = new ArrayList<>(
				Arrays.asList("complete"));
		rules = ruleCreator.generateRules(rule1(), activities, eventtypes,
				replacements);
		assertEquals("Number of combinations", 0, rules.size());

		eventtypes = new ArrayList<>(Arrays.asList("complete", "started"));
		rules = ruleCreator.generateRules(rule1(), activities, eventtypes,
				replacements);
		assertEquals("Number of combinations", 6, rules.size());

	}

	@Test
	public void generateWithBadReplacementRule1() {
		HashMap<String, String[]> replacements = new HashMap<>();
		replacements.put("A", new String[] { "C", "D", "A", "stupidThing" });
		replacements.put("B", new String[] { "B" });

		ArrayList<String> rules = ruleCreator.generateRules(rule1(),
				activities, replacements);
		assertEquals("Number of combinations", 12, rules.size());

		ArrayList<String> eventtypes = new ArrayList<>(
				Arrays.asList("complete"));
		rules = ruleCreator.generateRules(rule1(), activities, eventtypes,
				replacements);
		assertEquals("Number of combinations", 0, rules.size());

		eventtypes = new ArrayList<>(Arrays.asList("complete", "started"));
		rules = ruleCreator.generateRules(rule1(), activities, eventtypes,
				replacements);
		assertEquals("Number of combinations", 24, rules.size());

	}

	private String rule1() {
		return "formula one( A: activity, BSDFAD: activity) = {} \n"
				+ "( <>( activity == A ) /\\ <>( activity == BSDFAD) );";
	}

	private String rule2() {
		return "formula one( A: activity) = {} \n" + "( <>( activity == A ));";
	}

	private String rule3() {
		return "formula rule7( A: activity , B: activity ) :=  { }"
				+ "( (activity != B _U activity == A) \\/ [](activity != B) );";
	}

}
