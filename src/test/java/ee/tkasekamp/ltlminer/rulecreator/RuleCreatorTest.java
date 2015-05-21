package ee.tkasekamp.ltlminer.rulecreator;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class RuleCreatorTest {
	private RuleCreator ruleCreator;

	@Before
	public void setUp() throws Exception {
		ruleCreator = new RuleCreator();
	}

	@Test
	public void generateRule1() {
		ArrayList<String> activities = new ArrayList<>(Arrays.asList("A", "B",
				"C", "D", "E"));

		ArrayList<String> rules = ruleCreator
				.generateRules(rule1(), activities);

		for (String string : rules) {
			System.out.println(string);
		}
		assertEquals("Number of combinations", 20, rules.size());

	}

	private String rule1() {
		return "formula one( A: activity, BSDFAD: activity) = {} \n"
				+ "( <>( event == A ) /\\ <>( event == BSDFAD) );";
	}

}
