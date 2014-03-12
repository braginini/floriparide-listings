package com.floriparide.listings.etl.parser;

import com.fasterxml.jackson.core.JsonParseException;

import java.io.IOException;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public interface ProfileListParser<T> {

	public List<T> parse(String resource) throws IOException;
}
