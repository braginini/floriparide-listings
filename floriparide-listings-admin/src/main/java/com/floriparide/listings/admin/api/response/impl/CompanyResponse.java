package com.floriparide.listings.admin.api.response.impl;

import com.floriparide.listings.admin.api.response.EntityResponse;
import com.floriparide.listings.model.Company;
import com.floriparide.listings.web.json.CompanyElement;

/**
 * @author Mikhail Bragin
 */
public class CompanyResponse extends EntityResponse<CompanyElement> {

	public CompanyResponse(Company company) {
		super(new CompanyElement(company));
	}
}
