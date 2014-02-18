package com.floriparide.listings.admin.api.request.impl;

import com.floriparide.listings.admin.api.request.CreateEntityRequest;
import com.floriparide.listings.admin.api.request.IRequest;
import com.floriparide.listings.web.json.CompanyElement;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Mikhail Bragin
 */
public class CreateCompanyRequest extends CreateEntityRequest<CompanyElement> {

	@Override
	public void validate() throws Exception {
		//todo
	}
}
