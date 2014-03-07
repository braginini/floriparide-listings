package com.floriparide.listings.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Describes rubric attributes groups. E.g. Restaurants and cafe rubrics can have food_service group.
 *
 * @author Mikhail Bragin
 */
public class AttributesGroup extends MultiLangMetaModel {

	Long id;

	List<Attribute> attributes;

	public AttributesGroup(Long id, Map<String, String> names, List<Attribute> attributes) {
		super(names);
		this.id = id;
		this.names = names;
		this.attributes = attributes;
	}

	public AttributesGroup() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Attribute> getAttributes() {
		return (this.attributes != null) ? this.attributes : Collections.<Attribute>emptyList();
	}

	public Map<String, String> getNames() {
		return names;
	}

	public void setNames(Map<String, String> names) {
		this.names = names;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

}
