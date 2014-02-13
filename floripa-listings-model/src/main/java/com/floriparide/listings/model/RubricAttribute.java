package com.floriparide.listings.model;

/**
 * Describes {@link com.floriparide.listings.model.Rubric} attributes.
 * E.g. Restaurants rubric will have WiFi, Take Home, European cuisine, etc attributes.
 *
 * @author Mikhail Bragin
 */
public class RubricAttribute {

	Long id;

	Long rubricId;

	//todo make names for each language? Esp, En, pt?
	String name;

	RubricAttributeType type;

	public RubricAttribute(Long id, Long rubricId, String name, RubricAttributeType type) {
		this.id = id;
		this.rubricId = rubricId;
		this.name = name;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public Long getRubricId() {
		return rubricId;
	}

	public String getName() {
		return name;
	}

	public RubricAttributeType getType() {
		return type;
	}


	/**
	 * Describes {@link com.floriparide.listings.model.RubricAttribute} type
	 *
	 */
	public enum RubricAttributeType {
		BOOLEAN, NUMBER, RANGE
	}
}
