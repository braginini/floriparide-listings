package com.floriparide.listings.etl.parser;

import com.fasterxml.jackson.databind.JsonNode;
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
		JsonNode n  = profileParser.parse(HttpConnector.getPageAsString("" +
				"http://www.hagah.com.br/sc/florianopolis/local/103431,2,bella-pizza.html", new HashMap<String, String>()));
		System.out.println(n);
	}
}
