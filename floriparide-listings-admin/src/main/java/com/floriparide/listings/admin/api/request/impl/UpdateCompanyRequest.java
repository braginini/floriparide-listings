package com.floriparide.listings.admin.api.request.impl;

import org.springframework.util.Assert;

/**
 * @author Mikhail Bragin
 */
public class UpdateCompanyRequest extends CreateCompanyRequest {

	@Override
	public void validate() throws Exception {
		super.validate();
		Assert.notNull(entity.getId(), "Field id must not be null");
		Assert.notNull(entity.getPromo(), "Field promo must not be null");
	}
}
