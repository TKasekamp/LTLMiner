package ee.tkasekamp.ltlminer;

import java.util.ArrayList;
import java.util.HashSet;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

public class LTLMiner {

	public void mine(XLog log, String rule, double threshold) {
		// get list of activities from log
		// create list of rules with createCombinations
		// preProcess rule list to create a LTL model
		// analyse LTL model with LTLChecker
		// take the output and add rules above the threshold to ArrayList
		// do something with this ArrayList
	}

	/**
	 * Finds all activities in the input log.
	 * 
	 * @param log
	 * @return
	 */
	public static HashSet<String> findAllActivities(XLog log) {
		HashSet<String> answ = new HashSet<String>();
		for (XTrace trace : log) {
			for (XEvent event : trace) {
				String activityName = XConceptExtension.instance().extractName(
						event);
				answ.add(activityName);
			}
		}

		return answ;
	}

	public static ArrayList<String> getActivities(XLog log) {
		HashSet<String> answ = findAllActivities(log);
		ArrayList<String> a = new ArrayList<>();
		a.addAll(answ);
		return a;

	}

	/**
	 * Creates a proper LTL model from all the input rules. Adds necessary stuff
	 * to the front.
	 * 
	 * @param rules
	 * @return LTL model ready to be analysed
	 */
	public static String createLTLModel(ArrayList<String> rules) {
		return null;
	}

}
