package com.floriparide.listings.etl.parser;

import java.util.List;

/**
 * @author Mikhail Bragin
 */
public interface ProfileListParser {

	public List<String> parse(String resource);
}
