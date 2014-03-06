package com.floriparide.listings.model;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Describes {@link com.floriparide.listings.model.Rubric} attributes.
 * E.g. Restaurants rubric will have WiFi, Take Home, European cuisine, etc attributes.
 *
 * @author Mikhail Bragin
 */
public class Attribute extends MultiLangMetaModel {

	Long id;

	Long groupId;

	//list of possible values, can be null if no values are present for this attribute
	@Nullable
	List<String> possibleValues;

	//the current value set while creating/editing branch
	String currentValue;

	public Attribute(Long id, String currentValue) {
		this.id = id;
		this.currentValue = currentValue;
	}

	public Attribute(Long id, Long groupId, Map<String, String> names, List<String> possibleValues) {
		super(names);
		this.id = id;
		this.groupId = groupId;
		this.possibleValues = possibleValues;
	}

	public Attribute(Long id, Long groupId,Map<String, String> names, List<String> possibleValues, String currentValue) {
		super(names);
		this.id = id;
		this.groupId = groupId;
		this.possibleValues = possibleValues;
		this.currentValue = currentValue;
	}

	public Attribute() {
	}

	public Long getId() {
		return id;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public void setPossibleValues(@Nullable List<String> possibleValues) {
		this.possibleValues = possibleValues;
	}

	public List<String> getPossibleValues() {
		return possibleValues;
	}

	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
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
