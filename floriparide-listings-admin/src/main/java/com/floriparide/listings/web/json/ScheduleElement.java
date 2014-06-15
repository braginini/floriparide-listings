package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.Interval;
import com.floriparide.listings.model.Schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 6/14/14.
 */
public class ScheduleElement extends Element<Schedule> {

    @JsonProperty("")
    List<IntervalElement> monday = new ArrayList<>();

    @JsonProperty("")
    List<IntervalElement> tuesday = new ArrayList<>();

    @JsonProperty("")
    List<IntervalElement> wednesday = new ArrayList<>();

    @JsonProperty("")
    List<IntervalElement> thursday = new ArrayList<>();

    @JsonProperty("")
    List<IntervalElement> friday = new ArrayList<>();

    @JsonProperty("")
    List<IntervalElement> saturday = new ArrayList<>();

    @JsonProperty("")
    List<IntervalElement> sunday = new ArrayList<>();

    public ScheduleElement(Schedule schedule) {
        if (schedule != null) {
            this.monday = IntervalElement.modelListToElementList(schedule.getMonday());
            this.tuesday = IntervalElement.modelListToElementList(schedule.getTuesday());
            this.wednesday = IntervalElement.modelListToElementList(schedule.getWednesday());
            this.thursday = IntervalElement.modelListToElementList(schedule.getThursday());
            this.friday = IntervalElement.modelListToElementList(schedule.getFriday());
            this.saturday = IntervalElement.modelListToElementList(schedule.getSaturday());
            this.sunday = IntervalElement.modelListToElementList(schedule.getSunday());
        }

    }


    @Override
    public Schedule getModel() {
        Schedule schedule = new Schedule();
        schedule.setMonday(IntervalElement.elementListToModelList(monday));
        schedule.setTuesday(IntervalElement.elementListToModelList(tuesday));
        schedule.setWednesday(IntervalElement.elementListToModelList(wednesday));
        schedule.setThursday(IntervalElement.elementListToModelList(thursday));
        schedule.setFriday(IntervalElement.elementListToModelList(friday));
        schedule.setSaturday(IntervalElement.elementListToModelList(saturday));
        schedule.setSunday(IntervalElement.elementListToModelList(sunday));
        return schedule;
    }

    public List<IntervalElement> getMonday() {
        return monday;
    }

    public void setMonday(List<IntervalElement> monday) {
        this.monday = monday;
    }

    public List<IntervalElement> getTuesday() {
        return tuesday;
    }

    public void setTuesday(List<IntervalElement> tuesday) {
        this.tuesday = tuesday;
    }

    public List<IntervalElement> getWednesday() {
        return wednesday;
    }

    public void setWednesday(List<IntervalElement> wednesday) {
        this.wednesday = wednesday;
    }

    public List<IntervalElement> getThursday() {
        return thursday;
    }

    public void setThursday(List<IntervalElement> thursday) {
        this.thursday = thursday;
    }

    public List<IntervalElement> getFriday() {
        return friday;
    }

    public void setFriday(List<IntervalElement> friday) {
        this.friday = friday;
    }

    public List<IntervalElement> getSaturday() {
        return saturday;
    }

    public void setSaturday(List<IntervalElement> saturday) {
        this.saturday = saturday;
    }

    public List<IntervalElement> getSunday() {
        return sunday;
    }

    public void setSunday(List<IntervalElement> sunday) {
        this.sunday = sunday;
    }
}
