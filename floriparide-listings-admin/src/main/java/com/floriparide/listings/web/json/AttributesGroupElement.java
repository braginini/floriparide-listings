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

	@JsonProperty("input_type")
	String inputType;

	@JsonProperty("filter_type")
	String filterType;

	@JsonProperty("")
	List<String> values;

	public AttributesGroupElement() {
	}

	public AttributesGroupElement(@NotNull AttributesGroup attributesGroup) {
		this.id = attributesGroup.getId();
		this.names = attributesGroup.getNames();
		this.inputType = attributesGroup.getInputType().getType();
		this.filterType = attributesGroup.getFilterType().getType();
		this.values = attributesGroup.getValues();
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

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	@Override
	@JsonIgnore
	public AttributesGroup getModel() {
		return new AttributesGroup(id, names, AttributesGroup.InputType.lookup(inputType),
				AttributesGroup.FilterType.lookup(filterType), values);
	}
}


