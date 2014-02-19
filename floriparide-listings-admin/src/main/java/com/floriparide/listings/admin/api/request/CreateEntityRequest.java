package com.floriparide.listings.admin.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.jetbrains.annotations.NotNull;

/**
 * Basic class for Create entity requests.
 *
 * @author Mikhail Bragin
 */
public abstract class CreateEntityRequest<T> implements IRequest {

	@JsonProperty("")
	protected T entity;

	protected CreateEntityRequest() {
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}
}
