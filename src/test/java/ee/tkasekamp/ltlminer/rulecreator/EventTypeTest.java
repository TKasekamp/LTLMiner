package ee.tkasekamp.ltlminer.rulecreator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class EventTypeTest {
	private EventTypeCombiner evCombiner;

	@Before
	public void setup() {
		evCombiner = new EventTypeCombiner();
	}

	@Test
	public void testRule1() {
		ArrayList<String> parameters = new ArrayList<>();
		parameters.add("A");
		parameters.add("B");
		ArrayList<String> eventTypes = new ArrayList<>();
		eventTypes.add("complete");

		ArrayList<String> rules = evCombiner.combine(rule1(), eventTypes,
				parameters);

		assertEquals(0, rules.size());

		// Add another eventType
		eventTypes.add("started");
		ArrayList<String> rules2 = evCombiner.combine(rule1(), eventTypes,
				parameters);
		assertEquals(2, rules2.size());
		assertTrue(rules2.get(0).contains(
				"activity == A /\\ eventtype == \"complete\""));
		assertTrue(rules2.get(0).contains(
				"activity == B /\\ eventtype == \"started\""));
		assertTrue(rules2.get(1).contains(
				"activity == A /\\ eventtype == \"started\""));
		assertTrue(rules2.get(1).contains(
				"activity == B /\\ eventtype == \"complete\""));
		// And the other two are fine
	}

	@Test
	public void testRule2() {
		ArrayList<String> parameters = new ArrayList<>();
		parameters.add("A");
		parameters.add("B");
		ArrayList<String> eventTypes = new ArrayList<>();
		eventTypes.add("complete");

		ArrayList<String> rules = evCombiner.combine(rule2(), eventTypes,
				parameters);

		assertEquals(0, rules.size());

		// Add another eventType
		eventTypes.add("started");
		ArrayList<String> rules2 = evCombiner.combine(rule2(), eventTypes,
				parameters);
		assertEquals(2, rules2.size());
		assertTrue(rules2.get(0).contains(
				"activity == A /\\ eventtype == \"complete\""));
		assertTrue(rules2.get(0).contains(
				"activity != B /\\ eventtype == \"started\""));
		assertTrue(rules2.get(1).contains(
				"activity == A /\\ eventtype == \"started\""));
		assertTrue(rules2.get(1).contains(
				"activity != B /\\ eventtype == \"complete\""));
		// And the other two are fine

		// Add another eventType
		eventTypes.add("waiting");
		eventTypes.add("assigned");
		ArrayList<String> rules3 = evCombiner.combine(rule2(), eventTypes,
				parameters);
		assertEquals(12, rules3.size());
	}
	
	@Test
	public void testRule2RealLog() {
		ArrayList<String> parameters = new ArrayList<>();
		parameters.add("A");
		parameters.add("B");
		ArrayList<String> ev = new ArrayList<>(Arrays.asList("Awaiting Assignment","Assigned","Wait - Implementation","In Call","In Progress","Wait - User","Cancelled","Unmatched","Closed","Resolved","Wait","Wait - Customer","Wait - Vendor"));

		ArrayList<String> rules = evCombiner.combine(rule2(), ev,
				parameters);
		assertEquals(156, rules.size());

	}

	@Test
	public void testRule3() {
		ArrayList<String> parameters = new ArrayList<>();
		parameters.add("A");
		parameters.add("B");
		parameters.add("C");
		ArrayList<String> eventTypes = new ArrayList<>();
		eventTypes.add("complete");

		ArrayList<String> rules = evCombiner.combine(rule3(), eventTypes,
				parameters);

		assertEquals(0, rules.size());

		// Add another eventType
		eventTypes.add("started");
		ArrayList<String> rules2 = evCombiner.combine(rule3(), eventTypes,
				parameters);
		assertEquals(2, rules2.size());
		assertTrue(rules2.get(0).contains(
				"activity == A /\\ eventtype == \"complete\""));
		assertTrue(rules2.get(0).contains(
				"activity != B /\\ eventtype == \"started\""));
		assertTrue(rules2.get(1).contains(
				"activity == A /\\ eventtype == \"started\""));
		assertTrue(rules2.get(1).contains(
				"activity != B /\\ eventtype == \"complete\""));
		// And the other two are fine

		// Add another eventType
		eventTypes.add("waiting");
		eventTypes.add("assigned");
		ArrayList<String> rules3 = evCombiner.combine(rule3(), eventTypes,
				parameters);
		assertEquals(36, rules3.size());
	}

	@Test
	public void testRule4() {
		ArrayList<String> parameters = new ArrayList<>();
		parameters.add("A");
		ArrayList<String> eventTypes = new ArrayList<>();
		eventTypes.add("complete");

		ArrayList<String> rules = evCombiner.combine(rule4(), eventTypes,
				parameters);

		assertEquals(1, rules.size());

		// Add another eventType
		eventTypes.add("started");
		ArrayList<String> rules2 = evCombiner.combine(rule4(), eventTypes,
				parameters);
		assertEquals(2, rules2.size());
		assertTrue(rules2.get(0).contains(
				"activity == A /\\ eventtype == \"complete\""));
		assertTrue(rules2.get(1).contains(
				"activity == A /\\ eventtype == \"started\""));
		// And the other two are fine

		// Add another eventType
		eventTypes.add("waiting");
		eventTypes.add("assigned");
		ArrayList<String> rules3 = evCombiner.combine(rule4(), eventTypes,
				parameters);

		assertEquals(4, rules3.size());
	}

	private String rule1() {
		return "formula query(A :activity, B: activity) := {} \n"
				+ "(<>(activity == A) /\\  !(<>(activity == B)));";
	}

	private String rule2() {
		return "formula rule7( A: activity , B: activity ) :=  { precedence or ([!(?y) U ?x]) | !(G?y)}"
				+ "( (activity != B _U activity == A) \\/ [](activity != B) );";
	}

	private String rule3() {
		return "formula rule7( A: activity , B: activity , C:activity) :=  { "
				+ "( (activity != B _U activity == A) \\/ [](activity != C) );";
	}

	private String rule4() {
		return "formula rule1( A: activity ) :=  { init or ?x}"
				+ "activity == A;";
	}

}
