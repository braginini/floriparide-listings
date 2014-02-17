package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Mikhail Bragin
 */
public class CompanyElement {

	@JsonProperty("")
	Long id;

	@JsonProperty("")
	String name;

	@JsonProperty("")
	String description;

	@JsonProperty("")
	String promo;

	public CompanyElement() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPromo() {
		return promo;
	}

	public void setPromo(String promo) {
		this.promo = promo;
	}
}
