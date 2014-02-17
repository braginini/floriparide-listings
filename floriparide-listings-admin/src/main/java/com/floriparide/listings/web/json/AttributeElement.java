package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.Attribute;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class AttributeElement {

	@JsonProperty("")
	Long id;

	@JsonProperty("")
	Long groupId;

	@JsonProperty("")
	String name;

	public AttributeElement() {
	}

	public AttributeElement(@NotNull Attribute attribute) {
		this.id = attribute.getId();
		this.groupId = attribute.getGroupId();
		this.name = attribute.getName();
	}

	public static List<AttributeElement> attributesToAttributeElements(List<Attribute> attributes) {
		List<AttributeElement> attributeElements = new ArrayList<AttributeElement>(attributes.size());

		for (Attribute a : attributes)
			attributeElements.add(new AttributeElement(a));

		return attributeElements;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
