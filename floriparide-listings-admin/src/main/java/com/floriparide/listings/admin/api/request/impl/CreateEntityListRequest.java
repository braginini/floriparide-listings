package com.floriparide.listings.admin.api.request.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.admin.api.request.IRequest;
import com.floriparide.listings.web.json.Element;
import com.floriparide.listings.web.json.RawDataElement;

import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class CreateEntityListRequest<E extends Element> implements IRequest {

	@JsonProperty("")
	List<E> entities;

	public CreateEntityListRequest() {
	}

	public List<E> getEntities() {
		return entities;
	}

	public void setEntities(List<E> entities) {
		this.entities = entities;
	}

	@Override
	public void validate() throws Exception {

	}
}
