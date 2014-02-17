package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Mikhail Bragin
 */
public class RubricAttributeElement {

	@JsonProperty("")
	Long id;

	@JsonProperty("")
	Long rubricId;

	@JsonProperty("")
	String name;

	@JsonProperty("")
	String type;

	public RubricAttributeElement() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRubricId() {
		return rubricId;
	}

	public void setRubricId(Long rubricId) {
		this.rubricId = rubricId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
