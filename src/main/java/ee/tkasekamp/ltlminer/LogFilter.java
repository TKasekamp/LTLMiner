package ee.tkasekamp.ltlminer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

/**
 * This class is given an XLog and some parameters. It then returns a list of
 * Activities based on those parameters.
 * 
 * @author TKasekamp
 *
 */
public class LogFilter {

	public static final int DEFAULT_ACTIVITIES = 10;

	/**
	 * Orders all activities in the log by frequency and returns
	 * {@link LogFilter#DEFAULT_ACTIVITIES} most frequent.
	 * 
	 * @param log
	 *            {@link XLog}
	 * @return
	 */
	public ArrayList<String> getFrequent(XLog log) {
		return getFrequent(log, DEFAULT_ACTIVITIES);
	}

	/**
	 * Orders all activities in the log by frequency and returns the most
	 * frequent.
	 * 
	 * @param log
	 *            {@link XLog}
	 * @param howMany
	 *            How many activities to return.
	 * @return
	 */
	public ArrayList<String> getFrequent(XLog log, int howMany) {
		Map<String, Integer> freqMap = getFrequencySet(log);
		Map.Entry<String, Integer>[] entries = sortActivitiesByFrequency(freqMap);
		ArrayList<String> result = new ArrayList<>();
		int count = 0;
		for (Map.Entry<String, Integer> entry : entries) {
			if (count == howMany)
				break;
			result.add(entry.getKey());
			count++;
		}
		return result;

	}

	/**
	 * Get all activities from this log.
	 * 
	 * @param log
	 *            {@link XLog}
	 * @return
	 */
	public ArrayList<String> getAllActivities(XLog log) {
		HashSet<String> answ = new HashSet<String>();
		for (XTrace trace : log) {
			for (XEvent event : trace) {
				String activityName = XConceptExtension.instance().extractName(
						event);
				answ.add(activityName);
			}
		}

		ArrayList<String> a = new ArrayList<>();
		a.addAll(answ);
		return a;
	}

	/**
	 * Get only activities from the log that are present in the input array.
	 * 
	 * @param log
	 *            {@link XLog}
	 * @param activities
	 * @return
	 */
	public ArrayList<String> getActivities(XLog log, String[] activities) {
		HashSet<String> answ = new HashSet<String>();
		for (XTrace trace : log) {
			for (XEvent event : trace) {
				String activityName = XConceptExtension.instance().extractName(
						event);
				answ.add(activityName);
			}
		}

		ArrayList<String> a = new ArrayList<>();
		for (String string : activities) {
			if (answ.contains(string))
				a.add(string);

		}

		return a;
	}

	/**
	 * Counts the number of times an activity appears in a log. Returns a
	 * HashMap, which is basically a dictionary.
	 * 
	 * @param log
	 *            The log to be analysed
	 * @return HashMap
	 */
	private HashMap<String, Integer> getFrequencySet(XLog log) {
		HashMap<String, Integer> activities = new HashMap<String, Integer>();
		for (XTrace trace : log) {
			for (XEvent event : trace) {
				String activityName = XConceptExtension.instance().extractName(
						event);
				if (activities.containsKey(activityName)) {
					int current = activities.get(activityName);
					activities.put(activityName, current + 1);
				} else {
					activities.put(activityName, 1);
				}
			}
		}

		return activities;
	}

	private Map.Entry<String, Integer>[] sortActivitiesByFrequency(
			Map<String, Integer> activities) {

		Map.Entry<String, Integer>[] entries = activities.entrySet().toArray(
				new Map.Entry[0]);
		Arrays.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Map.Entry<String, Integer> o1,
					Map.Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		return entries;
	}
}
