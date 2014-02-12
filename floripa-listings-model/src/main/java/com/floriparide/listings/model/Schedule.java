package com.floriparide.listings.model;

import java.util.Map;

/**
 * Describes {@link com.floriparide.listings.model.Branch} working time
 *
 * Each field represents a week day with working time intervals, e.g. {@link this#monday} can have keys
 * working_hours-0, working_hours-1, working_hours-2, ... with corresponding intervals of type {@link com.floriparide.listings.model.Schedule.Interval}
 *
 * If the company does not work during the week day corresponding field can be {@code null}
 *
 * @author Mikhail Bragin
 */
public class Schedule {

	Map<String, Interval> monday;
	Map<String, Interval> tuesday;
	Map<String, Interval> wednesday;
	Map<String, Interval> thursday;
	Map<String, Interval> friday;
	Map<String, Interval> saturday;
	Map<String, Interval> sunday;

	//working hour comment
	String comment;

	/**
	 * Describes working hour interval with fields {@code from} and {@code to} in {@code hh:mm} format
	 *
	 */
	public class Interval {

		String from;

		String to;

		public Interval(String from, String to) {
			this.from = from;
			this.to = to;
		}

		public String getFrom() {
			return from;
		}

		public String getTo() {
			return to;
		}
	}
}
