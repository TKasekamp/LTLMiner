package ee.tkasekamp.ltlminer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.deckfour.xes.model.XLog;
import org.processmining.plugins.ltlchecker.RuleModel;

import ee.tkasekamp.ltlminer.util.XLogReader;

public class LTLMinerStarter {
	// Here the properties file reading and starting LTLMiner
	// Read in XLog and after handle the output
	Properties config;
	LTLMiner miner;

	public LTLMinerStarter() throws IOException {
		config = loadProperties("config.properties");
		miner = new LTLMiner();
	}

	public LTLMinerStarter(Properties properties) {
		config = properties;
		miner = new LTLMiner();
	}

	public void mine() throws Exception {
		XLog log = readLogFile(config.getProperty("logPath"));
		ArrayList<RuleModel> output = miner.mine(log, config);
	}

	private Properties loadProperties(String propFileName) throws IOException {
		Properties prop = new Properties();
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(propFileName);

		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName
					+ "' not found in the classpath");
		}
		return prop;
	}

	private XLog readLogFile(String logPath) throws Exception {
		return XLogReader.openLog(logPath);
	}
}
