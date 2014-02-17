package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Mikhail Bragin
 */
public class RubricElement {

	@JsonProperty("")
	Long id;

	@JsonProperty("")
	String name;

	@JsonProperty("")
	Long parentId;

	public RubricElement() {
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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}
