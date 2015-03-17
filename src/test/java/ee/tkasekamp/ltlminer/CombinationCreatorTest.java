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
		// tests for all combinations of whitespace. the regex must handle that.
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
