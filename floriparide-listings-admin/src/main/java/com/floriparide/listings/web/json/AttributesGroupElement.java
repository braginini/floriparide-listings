package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.AttributesGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class AttributesGroupElement implements Element<AttributesGroup> {

	@JsonProperty("")
	Long id;

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

	public Map<String, String> getNames() {
		return names;
	}

	public void setNames(Map<String, String> names) {
		this.names = names;
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


