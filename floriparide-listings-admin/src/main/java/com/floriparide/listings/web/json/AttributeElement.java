package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.Attribute;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class AttributeElement implements Element<Attribute> {

	@JsonProperty("")
	Long id;

	@JsonProperty("")
	Long groupId;

	@JsonProperty("")
	String name;

	@JsonProperty("possible_values")
	List<Object> possibleValues;

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

	public static List<Attribute> attributesElementsToAttribute(List<AttributeElement> attributeElements) {
		List<Attribute> attributes = new ArrayList<Attribute>(attributeElements.size());

		for (AttributeElement a : attributeElements)
			attributes.add(a.getModel());

		return attributes;
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

	public List<Object> getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(List<Object> possibleValues) {
		this.possibleValues = possibleValues;
	}

	@Override
	public Attribute getModel() {
		return new Attribute(id, groupId, name, possibleValues);
	}
}
