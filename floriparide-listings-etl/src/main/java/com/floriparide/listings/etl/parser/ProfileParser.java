package com.floriparide.listings.etl.parser;

/**
 * @author Mikhail Bragin
 */
public interface ProfileParser<T> {

	public T parse(String resource);
}
