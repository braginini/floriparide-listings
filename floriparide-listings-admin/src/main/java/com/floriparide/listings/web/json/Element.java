package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *@author Mikhail Bragin
 */
public interface Element<T> {

	@JsonIgnore
	public T getModel();
}
