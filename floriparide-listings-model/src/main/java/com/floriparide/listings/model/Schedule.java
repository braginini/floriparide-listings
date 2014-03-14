package com.floriparide.listings.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Describes {@link com.floriparide.listings.model.Branch} working time
 * <p/>
 * Each field represents a week day with working time intervals, e.g. {@link this#monday} can have keys
 * working_hours-0, working_hours-1, working_hours-2, ... with corresponding intervals of type {@link com.floriparide.listings.model.Interval}
 * <p/>
 * If the company does not work during the week day corresponding field can be {@code null}
 *
 * @author Mikhail Bragin
 */
//todo add sorting by time in each field
public class Schedule {

	List<Interval> monday = new ArrayList<>();
	List<Interval> tuesday = new ArrayList<>();
	List<Interval> wednesday = new ArrayList<>();
	List<Interval> thursday = new ArrayList<>();
	List<Interval> friday = new ArrayList<>();
	List<Interval> saturday = new ArrayList<>();
	List<Interval> sunday = new ArrayList<>();

	//working hour comment
	String comment;

	public Schedule() {
	}

	public void addToMonday(Interval interval) {
		monday.add(interval);
	}

	public void addToThursday(Interval interval) {
		thursday.add(interval);
	}
	public void addToTuesday(Interval interval) {
		tuesday.add(interval);
	}

	public void addToFriday(Interval interval) {
		friday.add(interval);
	}

	public void addToSaturday(Interval interval) {
		saturday.add(interval);
	}

	public void addToSunday(Interval interval) {
		saturday.add(interval);
	}

	public List<Interval> getMonday() {
		return monday;
	}

	public void setMonday(List<Interval> monday) {
		this.monday = monday;
	}

	public List<Interval> getTuesday() {
		return tuesday;
	}

	public void setTuesday(List<Interval> tuesday) {
		this.tuesday = tuesday;
	}

	public List<Interval> getWednesday() {
		return wednesday;
	}

	public void setWednesday(List<Interval> wednesday) {
		this.wednesday = wednesday;
	}

	public List<Interval> getThursday() {
		return thursday;
	}

	public void setThursday(List<Interval> thursday) {
		this.thursday = thursday;
	}

	public List<Interval> getFriday() {
		return friday;
	}

	public void setFriday(List<Interval> friday) {
		this.friday = friday;
	}

	public List<Interval> getSaturday() {
		return saturday;
	}

	public void setSaturday(List<Interval> saturday) {
		this.saturday = saturday;
	}

	public List<Interval> getSunday() {
		return sunday;
	}

	public void setSunday(List<Interval> sunday) {
		this.sunday = sunday;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Schedule sorted() {

		Collections.sort(monday);
		Collections.sort(tuesday);
		Collections.sort(wednesday);
		Collections.sort(thursday);
		Collections.sort(friday);
		Collections.sort(saturday);
		Collections.sort(sunday);

		return this;
	}
}
