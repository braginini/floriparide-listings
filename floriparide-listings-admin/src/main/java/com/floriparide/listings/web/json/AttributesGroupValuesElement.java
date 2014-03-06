package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.AttributesGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class AttributesGroupValuesElement implements Element<AttributesGroup> {

	@JsonProperty("")
	Long id;

	@JsonProperty("")
	List<String> values;

	public AttributesGroupValuesElement() {
	}

	public AttributesGroupValuesElement(@NotNull AttributesGroup attributesGroup) {
		this.id = attributesGroup.getId();
		this.values = attributesGroup.getValues();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	@Override
	public AttributesGroup getModel() {
		return new AttributesGroup(id, values);
	}

	public static List<AttributesGroupValuesElement> attributesGroupToAttributeGroupElements(List<AttributesGroup> attributes) {
		if (attributes == null) return Collections.emptyList();

		ArrayList<AttributesGroupValuesElement> attributesGroupValuesElements = new ArrayList<>();

		for (AttributesGroup a : attributes) {
			attributesGroupValuesElements.add(new AttributesGroupValuesElement(a));
		}

		return attributesGroupValuesElements;
	}

	public static List<AttributesGroup> attributesGroupElementsToAttributeGroup(List<AttributesGroupValuesElement> attributes) {
		if (attributes == null) return Collections.emptyList();

		ArrayList<AttributesGroup> attributesGroups = new ArrayList<>();
		for (AttributesGroupValuesElement a : attributes)
			attributesGroups.add(a.getModel());

		return attributesGroups;
	}
}
