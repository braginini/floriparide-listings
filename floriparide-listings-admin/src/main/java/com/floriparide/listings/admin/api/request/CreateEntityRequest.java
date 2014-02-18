package com.floriparide.listings.admin.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Basic class for Create entity requests.
 *
 * @author Mikhail Bragin
 */
public abstract class CreateEntityRequest<T> implements IRequest {

	@JsonProperty("")
	T entity;

	protected CreateEntityRequest() {
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}
}
