package ee.tkasekamp.ltlminer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.deckfour.xes.model.XLog;
import org.junit.Before;
import org.junit.Test;

import ee.tkasekamp.ltlminer.util.XLogReader;

public class LogFilterTest {
	LogFilter logFilter;
	XLog log;

	@Before
	public void setUp() throws Exception {
		logFilter = new LogFilter();
		log = XLogReader.openLog("src/test/resources/exercise1.xes");
	}

	@Test
	public void getActivitiesFromLog() {
		ArrayList<String> shouldBe = new ArrayList<>();
		shouldBe.addAll(Arrays.asList("A", "B", "C", "D", "E"));

		assertNotNull(log);
		ArrayList<String> activites = logFilter.getAllEvents(log);

		assertEquals(5, activites.size());
		assertEquals(shouldBe, activites);
	}

	@Test
	public void getTop2() {
		ArrayList<String> shouldBe = new ArrayList<>();
		shouldBe.addAll(Arrays.asList("A", "D"));

		assertNotNull(log);
		ArrayList<String> activites = logFilter.getFrequent(log, 2);

		assertEquals(2, activites.size());
		assertEquals(shouldBe, activites);
	}

	@Test
	public void getPrefferdActivities() {
		ArrayList<String> shouldBe = new ArrayList<>();
		shouldBe.addAll(Arrays.asList("A", "B"));

		assertNotNull(log);
		ArrayList<String> activites = logFilter.getActivities(log,
				new String[] { "A", "B", "M" });

		assertEquals(2, activites.size());
		assertEquals(shouldBe, activites);
	}

}
