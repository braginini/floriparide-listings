package com.floriparide.listings.model;

import java.util.Collections;
import java.util.List;

/**
 * Describes rubric attributes groups. E.g. Restaurants and cafe rubrics can have food_service group.
 *
 * @author Mikhail Bragin
 */
public class AttributesGroup {

	Long id;

	//todo make names for each language? Esp, En, pt?
	String name;

	AttributesType type;

	List<Attribute> attributes;

	public AttributesGroup(Long id, String name, AttributesType type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public AttributesType getType() {
		return type;
	}

	public List<Attribute> getAttributes() {
		return (this.attributes != null) ? this.attributes : Collections.<Attribute>emptyList();
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Describes {@link com.floriparide.listings.model.AttributesGroup} type
	 *
	 */
	public enum AttributesType {
		BOOLEAN, NUMBER, RANGE
	}
}
