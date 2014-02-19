package com.floriparide.listings.admin.api.request.impl;

import com.floriparide.listings.admin.api.request.CreateEntityRequest;
import com.floriparide.listings.admin.api.request.IRequest;
import com.floriparide.listings.model.Company;
import com.floriparide.listings.web.json.CompanyElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.util.Assert;

/**
 * @author Mikhail Bragin
 */
public class CreateCompanyRequest extends CreateEntityRequest<CompanyElement> {

	@Override
	public void validate() throws Exception {
		Assert.notNull(entity.getName(), "Field name must not be null");
		Assert.notNull(entity.getDescription(), "Field description must not be null");
		Assert.notNull(entity.getProjectId(), "Field project_id must not be null");
	}
}
