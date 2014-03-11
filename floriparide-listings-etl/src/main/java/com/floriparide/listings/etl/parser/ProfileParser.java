package com.floriparide.listings.etl.parser;

import org.json.simple.JSONObject;

/**
 * @author Mikhail Bragin
 */
public interface ProfileParser {

	public JSONObject parse(String resource);
}
