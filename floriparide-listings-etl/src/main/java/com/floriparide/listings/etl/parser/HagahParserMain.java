package com.floriparide.listings.etl.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.floriparide.listings.etl.parser.impl.hagah.HagahCategoryParser;
import com.floriparide.listings.etl.parser.impl.hagah.HagahProfileListParser;
import com.floriparide.listings.etl.parser.impl.hagah.HagahProfileParser;
import com.floriparide.listings.etl.parser.util.HttpConnector;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class HagahParserMain {

	public static void main(String[] args) throws IOException {

		//todo
		//1) run HagahCategoryParser
		//2) for each result url parse page by selecting element with class="navegacao"
		//   and selecting last element text of "aNormal imgPag" elements
		//3) construct urls for each page
		//4) send result to HagahProfileListParser


		HagahCategoryParser parser = new HagahCategoryParser();
		String resource = HttpConnector.getPageAsString(args[0], new HashMap<String, String>());

		if (resource != null) {
			List<String> nodes = parser.parse(resource);
			for (String n : nodes) {
				System.out.println(n);
			}
		}


		/*HagahProfileParser profileParser = new HagahProfileParser();
		String resource = HttpConnector.getPageAsString(args[0], new HashMap<String, String>());
		if (resource != null) {
			JsonNode n = profileParser.parse(resource);
			System.out.println(n);
		}*/
	}
}
