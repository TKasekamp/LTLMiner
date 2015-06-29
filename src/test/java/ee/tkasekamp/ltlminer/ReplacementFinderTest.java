package ee.tkasekamp.ltlminer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class ReplacementFinderTest {

	@Test
	public void noReplacements() {
		String query = "[]( (?x)  ->  <>(?y)";
		HashMap<String, String[]> result = ReplacementFinder.getReplacements(query);
		assertEquals(0, result.size());
	}

	@Test
	public void oneReplacement() {
		String query = "[]( (?x{A})  ->  <>(?y)";
		HashMap<String, String[]> result = ReplacementFinder.getReplacements(query);
		assertEquals(1, result.size());
		String[] array = result.get("x");
		assertEquals(1, array.length);
		assertEquals("A", array[0]);
	}

	@Test
	public void multipleReplacements() {
		String query = "[]( (?x{A,B,C})  ->  <>(?y{   B   })";
		HashMap<String, String[]> result = ReplacementFinder.getReplacements(query);
		assertEquals(2, result.size());
		String[] array = result.get("x");
		assertEquals(3, array.length);

		assertEquals(1, result.get("y").length);
	}

	@Test
	public void multipleQueries() {
		String queries = "\"[]( (?x{A,B})  ->  <>(?y))\"; \"<>(?x)\";  \"[]( (?x{A,B})  ->  <>(?y{C,F})\" ;  \"[]( (?x)  ->  <>(?y{C,F})\"";
		ArrayList<HashMap<String, String[]>> result = ReplacementFinder.find(queries);

		assertEquals(4, result.size());

		assertEquals(1, result.get(0).size());
		String[] array = result.get(0).get("x");
		assertEquals(2, array.length);

		assertEquals(0, result.get(1).size());

		assertEquals(2, result.get(2).size());

		assertEquals(1, result.get(3).size());
		array = result.get(3).get("y");
		assertEquals(2, array.length);

	}

}
