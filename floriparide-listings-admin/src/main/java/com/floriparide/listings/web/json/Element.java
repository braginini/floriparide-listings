package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *@author Mikhail Bragin
 */
public abstract class Element<T> {

	@JsonProperty("")
	Long id;

	@JsonIgnore
	public abstract T getModel();

	protected Element() {
	}

	protected Element(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
