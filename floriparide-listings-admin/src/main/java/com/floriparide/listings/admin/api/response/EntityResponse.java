package com.floriparide.listings.admin.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Mikhail Bragin
 */
public abstract class EntityResponse<T> implements IResponse {

	@JsonProperty("")
	T entity;

	public EntityResponse(T entity) {
		this.entity = entity;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}
}
