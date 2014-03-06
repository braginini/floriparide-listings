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
public class AttributesGroup extends MetaModel {

	Long id;

	Map<String, String> names;

	InputType inputType;

	FilterType filterType;

	List<String> values;

	public AttributesGroup(Long id, Map<String, String> names, InputType inputType, FilterType filterType, List<String> values) {
		this.id = id;
		this.names = names;
		this.inputType = inputType;
		this.filterType = filterType;
		this.values = values;
	}

	public AttributesGroup() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<String> getValues() {
		return (this.values != null) ? this.values : Collections.<String>emptyList();
	}

	public Map<String, String> getNames() {
		return names;
	}

	public void setNames(Map<String, String> names) {
		this.names = names;
	}

	public void setValues(List<String> values) {
		this.values = values;
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
