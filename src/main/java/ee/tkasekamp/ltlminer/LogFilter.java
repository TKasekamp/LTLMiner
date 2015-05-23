package ee.tkasekamp.ltlminer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
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

	public static final int DEFAULT_EVENTS = 10;

	/**
	 * Orders all events in the log by frequency and returns
	 * {@link LogFilter#DEFAULT_EVENTS} most frequent.
	 * 
	 * @param log
	 *            {@link XLog}
	 * @return {@link ArrayList} with {@link XEvent} names.
	 */
	public ArrayList<String> getFrequent(XLog log) {
		return getFrequent(log, DEFAULT_EVENTS);
	}

	/**
	 * Orders all events in the log by frequency and returns the most frequent.
	 * 
	 * @param log
	 *            {@link XLog}
	 * @param howMany
	 *            How many event names to return.
	 * @return {@link ArrayList} with {@link XEvent} names.
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
	 * Get all events from this log.
	 * 
	 * @param log
	 *            {@link XLog}
	 * @return {@link ArrayList} with {@link XEvent} names.
	 */
	public ArrayList<String> getAllEvents(XLog log) {
		HashSet<String> answ = new HashSet<String>();
		for (XTrace trace : log) {
			for (XEvent event : trace) {
				String eventName = XConceptExtension.instance().extractName(
						event);
				answ.add(eventName);
			}
		}

		ArrayList<String> a = new ArrayList<>();
		a.addAll(answ);
		return a;
	}

	public ArrayList<String> getEventTypes(XLog log) {
		HashSet<String> answ = new HashSet<String>();
		for (XTrace trace : log) {
			for (XEvent event : trace) {
				String eventType = XLifecycleExtension.instance()
						.extractTransition(event);
				answ.add(eventType);
			}
		}

		ArrayList<String> a = new ArrayList<>();
		a.addAll(answ);
		return a;
	}

	/**
	 * Get only events from the log that are present in the input array.
	 * 
	 * @param log
	 *            {@link XLog}
	 * @param activities
	 * @return {@link ArrayList} with {@link XEvent} names.
	 */
	public ArrayList<String> getActivities(XLog log, String[] activities) {
		HashSet<String> answ = new HashSet<String>();
		for (XTrace trace : log) {
			for (XEvent event : trace) {
				String eventName = XConceptExtension.instance().extractName(
						event);
				answ.add(eventName);
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
	 * Counts the number of times an event appears in a log. Returns a HashMap,
	 * which is basically a dictionary.
	 * 
	 * @param log
	 *            The log to be analysed
	 * @return HashMap
	 */
	private HashMap<String, Integer> getFrequencySet(XLog log) {
		HashMap<String, Integer> activities = new HashMap<String, Integer>();
		for (XTrace trace : log) {
			for (XEvent event : trace) {
				String eventName = XConceptExtension.instance().extractName(
						event);
				if (activities.containsKey(eventName)) {
					int current = activities.get(eventName);
					activities.put(eventName, current + 1);
				} else {
					activities.put(eventName, 1);
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
