package com.floriparide.listings.admin.api.request.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.admin.api.request.IRequest;
import com.floriparide.listings.model.Attribute;
import com.floriparide.listings.model.AttributesGroup;
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
		Assert.notNull(entity, "Field entity must not be null");

		if (entity instanceof MultiLangElement) {
			Assert.notNull(((MultiLangElement) entity).getNames(), "Field names must not be null");
		} else if (entity instanceof CompanyElement) {
			Assert.notNull(((CompanyElement) entity).getName(), "Field name must not be null");
			Assert.notNull(((CompanyElement) entity).getDescription(), "Field description must not be null");
			Assert.notNull(((CompanyElement) entity).getProjectId(), "Field project_id must not be null");
		} else if (entity instanceof BranchElement) {
			Assert.notNull(((BranchElement) entity).getName(), "Field name must not be null");
			Assert.notNull(((BranchElement) entity).getCompanyId(), "Field company_id must not be null");
		} else if (entity instanceof ProjectElement) {
			Assert.notNull(((ProjectElement) entity).getName(), "Field name must not be null");
		} else if (entity instanceof AttributeElement) {
			Assert.notNull(((AttributeElement) entity).getGroupId(), "Field group_id must not be null");
			Assert.notNull(Attribute.InputType.lookup(((AttributeElement) entity).getInputType()),
					"Unknown input_type " + ((AttributeElement) entity).getInputType());
			Assert.notNull(Attribute.FilterType.lookup(((AttributeElement) entity).getFilterType()),
					"Unknown filter_type " + ((AttributeElement) entity).getFilterType());
		} else if (entity instanceof AttributesGroupElement) {

		}

	}
}
