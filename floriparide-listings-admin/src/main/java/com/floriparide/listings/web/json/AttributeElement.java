package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.Attribute;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class AttributeElement implements Element<Attribute> {

	@JsonProperty("")
	Long id;

	@JsonProperty("")
	Long groupId;

	@JsonProperty("")
	Map<String, String> names;

    @JsonProperty("possible_values")
	List<String> possibleValues;

	@JsonProperty("value")
	String currentValue;

	public AttributeElement() {
	}

	public AttributeElement(@NotNull Attribute attribute) {
		this.id = attribute.getId();
		this.groupId = attribute.getGroupId();
		this.names = attribute.getNames();
        this.possibleValues = attribute.getPossibleValues();
		this.currentValue = attribute.getCurrentValue();
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

    @NotNull
    @JsonIgnore
    public Attribute getModel() {
        return new Attribute(id, groupId, names, possibleValues, currentValue);
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

	public Map<String, String> getNames() {
		return names;
	}

	public void setNames(Map<String, String> names) {
		this.names = names;
	}

	public List<String> getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(List<String> possibleValues) {
		this.possibleValues = possibleValues;
	}

	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}
}
