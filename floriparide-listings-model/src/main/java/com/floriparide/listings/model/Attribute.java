package com.floriparide.listings.model;

import org.jetbrains.annotations.Nullable;

import javax.persistence.Table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Describes {@link com.floriparide.listings.model.Rubric} attributes.
 * E.g. Restaurants rubric will have WiFi, Take Home, European cuisine, etc attributes.
 *
 * @author Mikhail Bragin
 */
@Table(name = "attribute")
public class Attribute extends MultiLangMetaModel {

	Long id;

	Long groupId;

	InputType inputType;

	FilterType filterType;

	//list of possible values, can be null if no values are present for this attribute
	@Nullable
	List<String> possibleValues;

	//the current value set while creating/editing branch
	String currentValue;

	public Attribute(Long id, String currentValue) {
		this.id = id;
		this.currentValue = currentValue;
	}

	public Attribute(Long id, Long groupId, Map<String, String> names, List<String> possibleValues) {
		super(names);
		this.id = id;
		this.groupId = groupId;
		this.possibleValues = possibleValues;
	}

	public Attribute(Long id, Long groupId,Map<String, String> names, List<String> possibleValues, String currentValue) {
		super(names);
		this.id = id;
		this.groupId = groupId;
		this.possibleValues = possibleValues;
		this.currentValue = currentValue;
	}

	public Attribute(Long id, Long groupId,Map<String, String> names, List<String> possibleValues, String currentValue, InputType inputType, FilterType filterType) {
		super(names);
		this.id = id;
		this.groupId = groupId;
		this.inputType = inputType;
		this.filterType = filterType;
		this.possibleValues = possibleValues;
		this.currentValue = currentValue;
	}

	public Attribute() {
	}

	public Long getId() {
		return id;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public InputType getInputType() {
		return inputType;
	}

	public void setInputType(InputType inputType) {
		this.inputType = inputType;
	}

	public FilterType getFilterType() {
		return filterType;
	}

	public void setFilterType(FilterType filterType) {
		this.filterType = filterType;
	}

	public void setPossibleValues(@Nullable List<String> possibleValues) {
		this.possibleValues = possibleValues;
	}

	public List<String> getPossibleValues() {
		return possibleValues;
	}

	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Attribute attribute = (Attribute) o;

		if (id != null ? !id.equals(attribute.id) : attribute.id != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	/**
	 * Describes {@link com.floriparide.listings.model.AttributesGroup} type
	 *
	 */
	public enum InputType {
		BOOLEAN("boolean"), NUMBER("number"), LIST("list");

		String type;

		InputType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}

		static final HashMap<String, InputType> map = new HashMap<>();

		static {
			for (InputType t : InputType.values())
				map.put(t.getType(), t);
		}

		public static InputType lookup(String type) {
			return (type != null) ? map.get(type.toLowerCase()) : null;
		}
	}

	public enum FilterType {
		SLIDER("slider"), CHECKBOX("checkbox"), LIST("list");

		String type;

		FilterType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}

		static final HashMap<String, FilterType> map = new HashMap<>();

		static {
			for (FilterType t : FilterType.values())
				map.put(t.getType(), t);
		}

		public static FilterType lookup(String type) {
			return (type != null) ? map.get(type.toLowerCase()) : null;
		}
	}
}
