package ee.tkasekamp.ltlminer;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Properties;

import org.junit.Test;

public class StarterTest {

	@Test
	public void test() throws Exception {
		LTLMinerStarter starter = new LTLMinerStarter(getProps());
		starter.mine();
	}

	@Test
	public void test2() throws Exception {
		Properties props = getProps();
		props.setProperty("outputFormat", "text");
		props.setProperty("outputPath", "rules.txt");
		LTLMinerStarter starter = new LTLMinerStarter(props);
		starter.mine();
		assertTrue(new File("rules.txt").exists());
	}

	@Test
	public void test3() throws Exception {
		Properties props = getProps();
		String queries = "\"[](( (?x{A,B})  ->  <>(?y{E,A})))\"; \"<>(?x{C,sad})\"";
		props.setProperty("queries", queries);
		props.setProperty("considerEventTypes", "false");
		props.setProperty("minSupport", "0.0");
		LTLMinerStarter starter = new LTLMinerStarter(props);
		starter.mine();

	}

	private Properties getProps() {
		Properties props = new Properties();
		props.setProperty("logPath", "src/test/resources/exercise1.xes");
		props.setProperty("considerEventTypes", "true");
		props.setProperty("minSupport", "0.5");
		props.setProperty("outputFormat", "console");
		String queries = "\"[](( (?x)  ->  <>(?y)))\"; \"<>(?x)\"";
		props.setProperty("queries", queries);

		return props;
	}

}
