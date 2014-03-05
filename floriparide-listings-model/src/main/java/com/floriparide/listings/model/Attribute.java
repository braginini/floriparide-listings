package com.floriparide.listings.model;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Describes {@link com.floriparide.listings.model.Rubric} attributes.
 * E.g. Restaurants rubric will have WiFi, Take Home, European cuisine, etc attributes.
 *
 * @author Mikhail Bragin
 */
public class Attribute {

	Long id;

	Long groupId;

	//todo make names for each language? Esp, En, pt?
	String name;

	//list of possible values, can be null if no values are present for this attribute
	@Nullable
	List<String> possibleValues;

	//the current value set while creating/editing branch
	Object currentValue;

	public Attribute(Long id, Long groupId, String name, List<String> possibleValues) {
		this.id = id;
		this.groupId = groupId;
		this.name = name;
		this.possibleValues = possibleValues;
	}

    public static String asCommaSeparated(List<String> list) {
        if (list == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); ++i) {
            if (i != 0)
                sb.append(",");
            sb.append(list.get(i));
        }
        return sb.toString();
    }

	public Long getId() {
		return id;
	}

	public Long getGroupId() {
		return groupId;
	}

	public String getName() {
		return name;
	}

	public List<String> getPossibleValues() {
		return possibleValues;
	}

	public Object getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Object currentValue) {
		this.currentValue = currentValue;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Attribute attribute = (Attribute) o;

		if (id != null ? !id.equals(attribute.id) : attribute.id != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
