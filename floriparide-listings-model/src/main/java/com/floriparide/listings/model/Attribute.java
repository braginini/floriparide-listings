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
	List<Object> possibleValues;

	public Attribute(Long id, Long groupId, String name, List<Object> possibleValues) {
		this.id = id;
		this.groupId = groupId;
		this.name = name;
		this.possibleValues = possibleValues;
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

	public List<Object> getPossibleValues() {
		return possibleValues;
	}
}
