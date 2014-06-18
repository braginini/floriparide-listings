package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.Interval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mike on 6/14/14.
 */
public class IntervalElement extends Element<Interval> {

    @JsonProperty("")
    String from;

    @JsonProperty("")
    String to;

    public IntervalElement() {
    }

    @Override
    public Interval getModel() {
        return new Interval(from, to);
    }

    public IntervalElement(Interval model) {
        if (model != null) {
            this.from = model.getFrom();
            this.to = model.getTo();
        }
    }

    public static List<Interval> elementListToModelList(List<IntervalElement> elements) {
        if (elements == null || elements.isEmpty())
            return Collections.emptyList();
        ArrayList<Interval> models = new ArrayList<>(elements.size());
        for (IntervalElement e : elements) {
            models.add(e.getModel());
        }

        return models;
    }

    public static List<IntervalElement> modelListToElementList(List<Interval> models) {
        if (models == null || models.isEmpty())
            return Collections.emptyList();

        ArrayList<IntervalElement> elements = new ArrayList<>(models.size());
        for (Interval m : models) {
            elements.add(new IntervalElement(m));
        }

        return elements;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
