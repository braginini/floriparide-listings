package com.floriparide.listings.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Describes working hour interval with fields {@code from} and {@code to} in {@code hh:mm} format
 */
public class Interval implements Comparable<Interval> {

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

	@Override
	public int compareTo(Interval o) {
		if (o == null)
			return 1;

		SimpleDateFormat formatter = new SimpleDateFormat("HH:MM");
		try {
			Date theirs = formatter.parse(o.from);
			Date ours = formatter.parse(this.from);

			if (theirs.getTime() > ours.getTime())
				return -1;
			else if (theirs.getTime() < ours.getTime())
				return 1;

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}
}
