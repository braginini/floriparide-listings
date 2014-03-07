package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.AttributesGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class AttributesGroupElement extends MultiLangElement<AttributesGroup> {

	@JsonProperty("")
	Map<String, String> names;

	@JsonProperty("")
	List<AttributeElement> attributes;

	public AttributesGroupElement() {
	}

	public AttributesGroupElement(@NotNull AttributesGroup attributesGroup) {
		this.id = attributesGroup.getId();
		this.names = attributesGroup.getNames();
		this.attributes = AttributeElement.attributesToAttributeElements(attributesGroup.getAttributes());
	}

	public static List<AttributesGroupElement> attributesGroupsToElements(
			@NotNull List<AttributesGroup> attributesGroups) {

		if (attributesGroups == null) return Collections.emptyList();

		List<AttributesGroupElement> attributesGroupElements = new ArrayList<>(attributesGroups.size());

		for (AttributesGroup ag : attributesGroups)
			attributesGroupElements.add(new AttributesGroupElement(ag));

		return attributesGroupElements;
	}

	public static List<AttributesGroup> attributesGroupsElementToAttributeGroups(
			@NotNull List<AttributesGroupElement> attributesGroupElements) {

		if (attributesGroupElements == null) return Collections.emptyList();

		List<AttributesGroup> attributesGroups = new ArrayList<>(attributesGroupElements.size());

		for (AttributesGroupElement ag : attributesGroupElements)
			attributesGroups.add(ag.getModel());

		return attributesGroups;
	}

	public List<AttributeElement> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeElement> attributes) {
		this.attributes = attributes;
	}

	@Override
	@JsonIgnore
	public AttributesGroup getModel() {
		return new AttributesGroup(id, names, AttributeElement.attributesElementsToAttribute(attributes));
	}
}


