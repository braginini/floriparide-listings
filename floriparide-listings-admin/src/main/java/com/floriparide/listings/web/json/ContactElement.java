package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Defines a web representation of {@link com.floriparide.listings.model.Contact} object
 *
 * @author Mikhail Bragin
 */
public class ContactElement {

	@JsonProperty("")
	Long id;

	@JsonProperty("")
	String type;

	@JsonProperty("")
	String value;

	@JsonProperty("")
	String comment;

	public ContactElement() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
