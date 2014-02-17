package com.floriparide.listings.admin.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.Company;
import com.floriparide.listings.web.json.CompanyElement;

import org.jetbrains.annotations.NotNull;

/**
 * @author Mikhail Bragin
 */
public class CompanyResponse implements IResponse {

	@JsonProperty("")
	CompanyElement company;

	public CompanyResponse() {
	}

	public CompanyResponse(@NotNull Company company) {
		this.company = new CompanyElement(company);
	}

	public CompanyElement getCompany() {
		return company;
	}

	public void setCompany(CompanyElement company) {
		this.company = company;
	}
}
