package ee.tkasekamp.ltlminer;

import static org.junit.Assert.*;

import org.deckfour.xes.model.XLog;
import org.junit.Before;
import org.junit.Test;

import ee.tkasekamp.ltlminer.util.XLogReader;

public class LTLMinerTest {
	LTLMiner miner;
	XLog log;

	@Before
	public void setUp() throws Exception {
		miner = new LTLMiner();
		log = XLogReader.openLog("src/test/resources/exercise1.xes");
	}

}
