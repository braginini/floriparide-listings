package com.floriparide.listings.admin.api.request.f;

import com.floriparide.listings.web.json.BranchElement;
import com.floriparide.listings.web.json.CompanyElement;
import com.floriparide.listings.web.json.Element;
import com.floriparide.listings.web.json.ProjectElement;

import org.springframework.util.Assert;

/**
 * @author Mikhail Bragin
 */
public class UpdateEntityRequestCommon<E extends Element> extends CreateEntityRequestCommon<E> {

	@Override
	public void validate() throws Exception {
		super.validate();
		if (entity instanceof CompanyElement) {
			Assert.notNull(((CompanyElement)entity).getId(), "Field id must not be null");
			Assert.notNull(((CompanyElement)entity).getPromo(), "Field promo must not be null");
		} else if (entity instanceof BranchElement) {

		} else if (entity instanceof ProjectElement) {
			Assert.notNull(((ProjectElement)entity).getId(), "Field id must not be null");
		}
	}
}
