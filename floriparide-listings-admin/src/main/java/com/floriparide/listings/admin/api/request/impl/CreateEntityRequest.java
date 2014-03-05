package com.floriparide.listings.admin.api.request.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.admin.api.request.IRequest;
import com.floriparide.listings.web.json.*;

import org.springframework.util.Assert;

/**
 * @author Mikhail Bragin
 */
public class CreateEntityRequest<E extends Element> implements IRequest {

	@JsonProperty("")
	protected E entity;

	protected CreateEntityRequest() {
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
		} else if (entity instanceof AttributeElement) {
            Assert.notNull(((AttributeElement)entity).getName(), "Field name must not be null");
            Assert.notNull(((AttributeElement)entity).getGroupId(), "Field group_id must not be null");
        }

	}
}
