package com.floriparide.listings.etl.parser;

import java.io.IOException;

/**
 * @author Mikhail Bragin
 */
public interface Parser<T> {

	T parse(String resource) throws IOException;
}
