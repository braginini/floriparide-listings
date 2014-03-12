package com.floriparide.listings.etl.parser;

import org.json.simple.JSONObject;

/**
 * @author Mikhail Bragin
 */
public interface ProfileParser<T> {

	public T parse(String resource);
}
