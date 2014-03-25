package com.floriparide.listings.etl.parser;

import com.floriparide.listings.etl.parser.impl.hagah.HagahProfileParser;
import com.floriparide.listings.etl.parser.util.HttpConnector;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Mikhail Bragin
 */
public class HagahParserMain {

	public static void main(String[] args) throws IOException {

		HagahProfileParser profileParser = new HagahProfileParser();
		profileParser.parse(HttpConnector.getPageAsString("" +
				"http://www.hagah.com.br/sc/florianopolis/local/103431,2,bella-pizza.html", new HashMap<String, String>()));
	}
}
