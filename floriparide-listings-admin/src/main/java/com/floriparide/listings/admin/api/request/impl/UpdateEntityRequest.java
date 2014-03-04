package com.floriparide.listings.admin.api.request.impl;

import com.floriparide.listings.web.json.BranchElement;
import com.floriparide.listings.web.json.CompanyElement;
import com.floriparide.listings.web.json.Element;
import com.floriparide.listings.web.json.ProjectElement;

import org.springframework.util.Assert;

/**
 * @author Mikhail Bragin
 */
public class UpdateEntityRequest<E extends Element> extends CreateEntityRequest<E> {

	@Override
	public void validate() throws Exception {
		super.validate();
		if (entity instanceof CompanyElement) {
			Assert.notNull(((CompanyElement) entity).getId(), "Field id must not be null");
			Assert.notNull(((CompanyElement) entity).getPromo(), "Field promo must not be null");
		} else if (entity instanceof BranchElement) {
			Assert.notNull(((BranchElement) entity).getId(), "Field id must not be null");
		} else if (entity instanceof ProjectElement) {
			Assert.notNull(((ProjectElement) entity).getId(), "Field id must not be null");
		}
	}
}
