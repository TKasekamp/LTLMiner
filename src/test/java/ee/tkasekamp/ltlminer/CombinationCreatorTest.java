package ee.tkasekamp.ltlminer;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class CombinationCreatorTest {
	CombinationCreator comboCreator;

	@Before
	public void setUp() throws Exception {
		comboCreator = new CombinationCreator();
	}

	@Test
	public void replaceOneTest() {
		ArrayList<String> activities = new ArrayList<>();
		activities.add("Skyrim");
		String rule = "formula one( A: activity) = {} \n" + "<>( event == A );";
		ArrayList<String> combos = comboCreator.createCombinations(rule,
				activities);
		assertEquals(1, combos.size());
		assertTrue(combos.get(0).contains("A: activity : \"Skyrim\""));
	}

	@Test
	public void replaceTwoTest() {
		ArrayList<String> activities = new ArrayList<>();
		activities.add("Skyrim");
		activities.add("Oblivion");
		String rule = "formula two( A: activity, B: activity) = {} \n"
				+ "( <>( event == A ) /\\ <>( event == B) );";
		ArrayList<String> combos = comboCreator.createCombinations(rule,
				activities);
		assertEquals(4, combos.size());

		assertTrue(combos.get(0).contains("A: activity : \"Skyrim\""));
		assertTrue(combos.get(0).contains("B: activity : \"Skyrim\""));
		assertTrue(combos.get(1).contains("A: activity : \"Skyrim\""));
		assertTrue(combos.get(1).contains("B: activity : \"Oblivion\""));
		assertTrue(combos.get(2).contains("A: activity : \"Oblivion\""));
		assertTrue(combos.get(2).contains("B: activity : \"Skyrim\""));
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
}
