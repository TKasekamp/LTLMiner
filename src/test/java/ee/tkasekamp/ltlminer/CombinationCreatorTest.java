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
		String rule = "formula one( A: activity) = {} \n"
				+ "<>( event == E );";
		ArrayList<String> combos = comboCreator.createCombinations(rule, activities);
		assertEquals(1, combos.size());
		System.out.println(combos.toString());
		assertTrue(combos.get(0).contains("A: activity : \"Skyrim\""));
	}
	
	@Test
	public void replaceTwoTest() {
		fail("Not yet implemented");
	}

}
