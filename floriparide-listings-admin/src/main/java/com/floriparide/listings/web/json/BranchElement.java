package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Mikhail Bragin
 */
//todo Schedule
public class BranchElement {

	@JsonProperty("")
	Long id;

	@JsonProperty("")
	String name;

	@JsonProperty("")
	String description;

	@JsonProperty("")
	List<ContactElement> contacts;

	@JsonProperty("")
	List<RubricElement> rubrics;

	@JsonProperty("")
	List<RubricAttributeElement> attributes;

	@JsonProperty("company_id")
	Long companyId;

	@JsonProperty("")
	String lat;

	@JsonProperty("")
	String lon;

	@JsonProperty("")
	String address;

	@JsonProperty("")
	String office;

	@JsonProperty("")
	String currency;

	@JsonProperty("")
	String article;

	@JsonProperty("payment_opts")
	String paymentOptions; //comma-separated values

	public BranchElement() {
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

	public List<ContactElement> getContacts() {
		return contacts;
	}

	public void setContacts(List<ContactElement> contacts) {
		this.contacts = contacts;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<RubricElement> getRubrics() {
		return rubrics;
	}

	public void setRubrics(List<RubricElement> rubrics) {
		this.rubrics = rubrics;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getPaymentOptions() {
		return paymentOptions;
	}

	public void setPaymentOptions(String paymentOptions) {
		this.paymentOptions = paymentOptions;
	}

	public List<RubricAttributeElement> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<RubricAttributeElement> attributes) {
		this.attributes = attributes;
	}
}
