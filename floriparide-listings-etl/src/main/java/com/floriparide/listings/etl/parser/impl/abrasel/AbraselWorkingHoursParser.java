package com.floriparide.listings.etl.parser.impl.abrasel;

import com.floriparide.listings.etl.parser.Parser;
import com.floriparide.listings.model.Interval;
import com.floriparide.listings.model.Schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses working hours from abrasel profile website according to
 * abrasel_working_hours_patterns.md
 *
 * @author Mikhail Bragin
 */
public class AbraselWorkingHoursParser implements Parser<Schedule> {

	private static final Logger log = LoggerFactory.getLogger(AbraselWorkingHoursParser.class);

	@Override
	public Schedule parse(String resource) throws IOException {

		if (resource == null || resource.isEmpty())
			return null;

		List<String> firstSplit = checkSplit(Arrays.asList(resource.split("\\.")));

		Schedule schedule = new Schedule();

		for (String s : firstSplit) {
			List<String> secondSplit = checkSplit(Arrays.asList(s.split(",")));
			//each second split should be size of 2

			if (secondSplit.size() < 2) {
				log.error("Strange second split detected in resource", resource);
				continue;
			}

			String weekDaysPart = secondSplit.get(0);
			String hoursPart = secondSplit.get(1);

			//get weekdays and corresponding hours
			List<Integer> weekdays = getWeekdays(weekDaysPart);
			List<Interval> hours = getHours(hoursPart);

			for (int i : weekdays) {
				if (i == Calendar.MONDAY)
					schedule.getMonday().addAll(hours);
				else if (i == Calendar.TUESDAY)
					schedule.getTuesday().addAll(hours);
				else if (i == Calendar.WEDNESDAY)
					schedule.getWednesday().addAll(hours);
				else if (i == Calendar.THURSDAY)
					schedule.getThursday().addAll(hours);
				else if (i == Calendar.FRIDAY)
					schedule.getFriday().addAll(hours);
				else if (i == Calendar.SATURDAY)
					schedule.getSaturday().addAll(hours);
				else if (i == Calendar.SUNDAY)
					schedule.getSunday().addAll(hours);
			}
		}

		return schedule.sorted();
	}

	private List<Interval> getHours(String hoursPart) {

		List<Interval> result = new ArrayList<>();

		if (hoursPart.contains(" e ")) {
			List<String> split = checkSplit(Arrays.asList(hoursPart.split(" e ")));
			//should be split size of 2
			if (!checkSplitSizeTwo(hoursPart, split))
				return Collections.emptyList();

			for (String s : split) {
				Interval interval = getInterval(s);

				if (interval != null)
					result.add(interval);
			}
		} else {
			Interval interval = getInterval(hoursPart);
			if (interval != null)
				result.add(interval);
		}

		return result;
	}

	private Interval getInterval(String s) {
		Interval interval = null;
		String[] fromTo = s.replace("de", "").split("\\sate\\s");
		List<String> fromToSplit = checkSplit(Arrays.asList(fromTo));
		if (checkSplitSizeTwo(s, fromToSplit)) {
			interval = new Interval(fromToSplit.get(0), fromToSplit.get(1));

		}
		return interval;
	}

	private boolean checkSplitSizeTwo(String resource, List<String> split) {
		if (split.size() < 2) {
			log.error("Strange hours pattern detected in resource", resource);
			return false;
		}

		return true;
	}

	/**
	 * Checks for spaces on the borders of strings and also for empty strings which are removed
	 *
	 * @param uncheckedSplit
	 * @return
	 */
	private List<String> checkSplit(List<String> uncheckedSplit) {
		List<String> checkedSplit = new ArrayList<>();

		for (String s : uncheckedSplit) {

			StringBuilder sb = new StringBuilder(s);
			//clear up borders from spaces
			if (!s.isEmpty()) {
				if (sb.charAt(0) == ' ')
					sb.deleteCharAt(0);

				if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ' ')
					sb.deleteCharAt(sb.length() - 1);

				//if after cleaning the string is empty, we don't need it
				if (sb.length() > 0)
					checkedSplit.add(sb.toString());
			}
		}
		return checkedSplit;

	}

	/**
	 * Returns days of week from weekdays part of second split
	 *
	 * @param s
	 * @return list of integers according to {@link java.util.Calendar} weekdays codes
	 */
	public List<Integer> getWeekdays(String s) {

		ArrayList<Integer> result = new ArrayList<>();

		if (s.equals(WeekdaysPattern.ALL_DAYS.getPattern())) {
			for (int i = 1; i < 8; i++)
				result.add(i);

		} else if (s.contains(WeekdaysPattern.PERIOD_PATTERN.getPattern())) {
			List<String> split = checkSplit(Arrays.asList(s.split(WeekdaysPattern.PERIOD_PATTERN.getPattern())));
			if (!checkSplitSizeTwo(s, split))
				return Collections.emptyList();

			WeekdaysPattern from = WeekdaysPattern.lookup(split.get(0));
			WeekdaysPattern to = WeekdaysPattern.lookup(split.get(1));

			int start = getDay(from);
			int end = getDay(to);

			if (start > 0 && end > 0) {
				result.addAll(getWeekDaysInRange(start, end));
			}

		} else {
			WeekdaysPattern p = WeekdaysPattern.lookup(s);
			int day = getDay(p);
			if (day > 0)
				result.add(day);
		}

		return result;

	}

	private int getDay(WeekdaysPattern p) {
		switch (p) {
			case MONDAY:
				return Calendar.MONDAY;
			case TUESDAY:
				return Calendar.TUESDAY;
			case WEDNESDAY:
				return Calendar.WEDNESDAY;
			case THURSDAY:
				return Calendar.THURSDAY;
			case FRIDAY:
				return Calendar.FRIDAY;
			case SATURDAY:
				return Calendar.SATURDAY;
			case SUNDAY:
				return Calendar.SUNDAY;
		}

		return -1;
	}

	/**
	 * Handles Sunday which is int = 1
	 *
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	private List<Integer> getWeekDaysInRange(int startDay, int endDay) {
		ArrayList<Integer> result = new ArrayList<>();
		if (endDay != Calendar.SUNDAY) {
			for (int i = startDay; i <= endDay; i++)
				result.add(i);
		} else {
			for (int i = startDay; i <= Calendar.SATURDAY; i++) {
				result.add(i);
			}
			result.add(endDay);
		}

		return result;
	}

	public enum WeekdaysPattern {

		ALL_DAYS("Todos dias da semana"),
		PERIOD_PATTERN(" à "),
		MONDAY("Segunda"),
		TUESDAY("Terça"),
		WEDNESDAY("Quarta"),
		THURSDAY("Quinta"),
		FRIDAY("Sexta"),
		SATURDAY("Sabado"),
		SUNDAY("Domingo");

		public static final Map<String, WeekdaysPattern> map = new HashMap<>();

		static {
			for (WeekdaysPattern p : WeekdaysPattern.values())
				map.put(p.getPattern(), p);
		}

		String pattern;

		WeekdaysPattern(String pattern) {
			this.pattern = pattern;
		}

		public String getPattern() {
			return pattern;
		}

		public static WeekdaysPattern lookup(String pattern) {
			return (pattern != null) ? map.get(pattern) : null;
		}


	}
}
