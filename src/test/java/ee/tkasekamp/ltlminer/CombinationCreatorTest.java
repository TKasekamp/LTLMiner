package ee.tkasekamp.ltlminer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class CombinationCreatorTest {
	CombinationCreator comboCreator;
	CombinationCreator noRepetitions;

	@Before
	public void setUp() throws Exception {
		comboCreator = new CombinationCreator();
		noRepetitions = new CombinationCreator(false);
	}

	@Test
	public void replaceOneTest() {
		ArrayList<String> activities = new ArrayList<>();
		activities.add("Skyrim");
		String rule = "formula one( A: activity) = {} \n" + "<>( event == A );";
		String[] rules = comboCreator.createRules(rule, activities);
		assertEquals(1, rules.length);
		assertTrue(rules[0].contains("A: activity : \"Skyrim\""));

	}

	@Test
	public void replaceTwoTest() {
		ArrayList<String> activities = new ArrayList<>();
		activities.add("Skyrim");
		activities.add("Oblivion");
		String rule = "formula two( A: activity, B: activity) = {} \n"
				+ "( <>( event == A ) /\\ <>( event == B) );";
		String[] rules = comboCreator.createRules(rule, activities);
		assertEquals(4, rules.length);

		assertTrue(rules[0].contains("A: activity : \"Skyrim\""));
		assertTrue(rules[0].contains("B: activity : \"Skyrim\""));
		assertTrue(rules[1].contains("A: activity : \"Skyrim\""));
		assertTrue(rules[1].contains("B: activity : \"Oblivion\""));
		assertTrue(rules[2].contains("A: activity : \"Oblivion\""));
		assertTrue(rules[2].contains("B: activity : \"Skyrim\""));
		// Rule name check
		assertTrue(rules[2].contains("rule_2"));
	}

	@Test
	public void whiteSpaceTest() {

		String rule1 = "sads( A : activity ) = {} ...";
		String rule2 = "sads( A: activity ) = {} ...";
		String rule3 = "sads( A :activity ) = {} ...";
		String rule4 = "sads( A:activity ) = {} ...";
		String rule5 = "sads( A    :    activity ) = {} ...";

		assertEquals(1, comboCreator.numberOfArguments(rule1));
		assertEquals(1, comboCreator.numberOfArguments(rule2));
		assertEquals(1, comboCreator.numberOfArguments(rule3));
		assertEquals(1, comboCreator.numberOfArguments(rule4));
		assertEquals(1, comboCreator.numberOfArguments(rule5));
	}

	@Test
	public void combinerTest() {
		int k = 2;
		String[] input = new String[] { "A", "B", "C", "D", "E" };
		String[] branch = new String[k];
		ArrayList<String[]> combos = new ArrayList<>();
		comboCreator.combine(input, k, branch, 0, combos);

		assertEquals("Number of combinations", 25, combos.size());
		assertEquals(k, combos.get(0).length);
	}

	@Test
	public void ruleRenameTest() {
		ArrayList<String> input = new ArrayList<>();
		input.add("formula one() := {}");
		input.add("formula   two() := {}");
		ArrayList<String> output = comboCreator.renameRules(input);
		assertEquals("formula rule_0() := {}", output.get(0));
		assertEquals("formula rule_1() := {}", output.get(1));
	}

	@Test
	public void noRepetitionsTest() {
		int k = 2;
		String[] input = new String[] { "A", "B", "C", "D" };
		String[] branch = new String[k];
		ArrayList<String[]> combos = new ArrayList<>();
		noRepetitions.combineNoRepetitions(input, k, branch, 0, combos);

		assertEquals("Number of combinations", 12, combos.size());
		assertEquals(k, combos.get(0).length);
	}

	@Test
	public void createCombinationsTest() {
		String rule = "formula two( A: activity, B: activity) = {} \n"
				+ "( <>( event == A ) /\\ <>( event == B) );";
		String[] input = new String[] { "A", "B", "C", "D", "E" };
		ArrayList<String> activities = new ArrayList<>();
		activities.addAll(Arrays.asList(input));

		String[] withRepetition = comboCreator.createCombinations(rule,
				activities);
		assertEquals("Number of combinations", 25, withRepetition.length);

		String[] noRepetition = noRepetitions.createCombinations(rule,
				activities);
		assertEquals("Number of combinations", 20, noRepetition.length);

	}

}
