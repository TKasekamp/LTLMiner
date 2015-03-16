package ee.tkasekamp.ltlminer;

import java.util.HashSet;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

public class LTLMiner {

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
}
