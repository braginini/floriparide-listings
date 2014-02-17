package com.floriparide.listings.admin.api.request;

import com.floriparide.listings.web.json.CompanyElement;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Mikhail Bragin
 */
public class CreateCompanyRequest implements IRequest {

	@JsonProperty("")
	CompanyElement company;

	public CreateCompanyRequest() {
	}

	public CompanyElement getCompany() {
		return company;
	}

	public void setCompany(CompanyElement company) {
		this.company = company;
	}

	@Override
	public void validate() throws Exception {
		//todo
	}
}
