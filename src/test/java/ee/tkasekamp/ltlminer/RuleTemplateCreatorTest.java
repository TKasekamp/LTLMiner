package ee.tkasekamp.ltlminer;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class RuleTemplateCreatorTest {

	@Test
	public void test() {

		String queries = "\"[]( (?x)  ->  <>(?y))\"; \"<>(?x)\";  \"[]( (?x)  ->  <>(?y)\" ;  \"[]( (?x)  ->  <>(?y)\"";

		ArrayList<String> templates = RuleTemplateCreator.createTemplates(queries);
		// for (String string : templates) {
		// System.out.println(string);
		// }
		assertEquals(4, templates.size());
	}

	@Test
	public void testWithReplacements() {

		String queries = "\"[]( (?x{A,B})  ->  <>(?y))\"; \"<>(?x{C})\";  \"[]( (?x)  ->  <>(?y{sdasda})\" ;  \"[]( (?x)  ->  <>(?y)\"";

		ArrayList<String> templates = RuleTemplateCreator.createTemplates(queries);

		assertEquals(4, templates.size());
	}

}
