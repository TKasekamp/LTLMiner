package ee.tkasekamp.ltlminer;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
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
		String asd = config.getProperty("outputFormat");
		switch (asd) {
		case "console":
			printToConsole(output);
			break;
		case "text":
			saveToFile(output, config.getProperty("outputPath"));
			break;
		default:
			break;
		}
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

	private void printToConsole(ArrayList<RuleModel> output) {
		System.out.println("coverage\tLTLRule");
		for (RuleModel ruleModel : output) {
			System.out.println(ruleModel.getCoverage() + "\t"
					+ ruleModel.getLtlRule());
		}
	}

	public void saveToFile(ArrayList<RuleModel> output, String outputPath) {
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputPath), "UTF-8"));
			for (RuleModel ruleModel : output) {
				out.write(ruleModel.getCoverage() + "\t"
						+ ruleModel.getLtlRule() + "\n");
			}
			out.close();
		} catch (IOException e) {
			System.err.println("Couldn't save to file: " + outputPath);
		}

	}
}
