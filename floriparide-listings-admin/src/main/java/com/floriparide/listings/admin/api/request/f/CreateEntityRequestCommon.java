package com.floriparide.listings.admin.api.request.f;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.admin.api.request.IRequest;
import com.floriparide.listings.web.json.BranchElement;
import com.floriparide.listings.web.json.CompanyElement;
import com.floriparide.listings.web.json.Element;
import com.floriparide.listings.web.json.ProjectElement;

import org.springframework.util.Assert;

/**
 * @author Mikhail Bragin
 */
public class CreateEntityRequestCommon<E extends Element> implements IRequest {

	@JsonProperty("")
	protected E entity;

	protected CreateEntityRequestCommon() {
	}

	public E getEntity() {
		return entity;
	}

	public void setEntity(E entity) {
		this.entity = entity;
	}

	@Override
	public void validate() throws Exception {
		if (entity instanceof CompanyElement) {
			Assert.notNull(((CompanyElement) entity).getName(), "Field name must not be null");
			Assert.notNull(((CompanyElement) entity).getDescription(), "Field description must not be null");
			Assert.notNull(((CompanyElement) entity).getProjectId(), "Field project_id must not be null");
		} else if (entity instanceof BranchElement) {

		} else if (entity instanceof ProjectElement) {
			Assert.notNull(((ProjectElement)entity).getName(), "Field name must not be null");
		}

	}
}
