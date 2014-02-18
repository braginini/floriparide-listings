package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.AttributesGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class AttributesGroupElement {

	@JsonProperty("")
	Long id;

	@JsonProperty("")
	String name;

	@JsonProperty("")
	String type;

	@JsonProperty("")
	List<AttributeElement> attributes;

	public AttributesGroupElement() {
	}

	public AttributesGroupElement(@NotNull AttributesGroup attributesGroup) {
		this.id = attributesGroup.getId();
		this.name = attributesGroup.getName();
		this.type = attributesGroup.getType().name();
		this.attributes = AttributeElement.attributesToAttributeElements(attributesGroup.getAttributes());
	}

	public static List<AttributesGroupElement> attributesGroupsToElements(@NotNull List<AttributesGroup> attributesGroups) {
		List<AttributesGroupElement> attributesGroupElements = new ArrayList<AttributesGroupElement>(attributesGroups.size());

		for (AttributesGroup ag : attributesGroups)
			attributesGroupElements.add(new AttributesGroupElement(ag));

		return attributesGroupElements;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<AttributeElement> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeElement> attributes) {
		this.attributes = attributes;
	}
}


