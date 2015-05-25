package ee.tkasekamp.ltlminer;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class RuleTemplateCreatorTest {

	@Test
	public void test() {

		String queries = "\"[]( (?x)  ->  <>(?y))\", \"<>(?x)\",  \"[]( (?x)  ->  <>(?y)\" ,  \"[]( (?x)  ->  <>(?y)\"";

		ArrayList<String> templates = RuleTemplateCreator
				.createTemplates(queries);
		// for (String string : templates) {
		// System.out.println(string);
		// }
		assertEquals(4, templates.size());
	}

}
