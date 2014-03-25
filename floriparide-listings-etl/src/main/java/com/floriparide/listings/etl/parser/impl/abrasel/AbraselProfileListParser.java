package com.floriparide.listings.etl.parser.impl.abrasel;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.floriparide.listings.etl.parser.Parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class AbraselProfileListParser implements Parser<List<JsonNode>> {

	static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
	}

	@Override
	public List<JsonNode> parse(String resource) throws IOException {


		Document doc = Jsoup.parse(resource);
		Element body = doc.getElementsByTag("body").get(0);
		String content = body.text();
		String[] parts = content.split("\\$A");
		//System.out.println(parts[1]);
		String part1 = parts[1];
		part1 = part1.substring(1, part1.length() - 29);
		part1 += "]";


		JsonNode[] nodes = mapper.readValue(part1, JsonNode[].class);

		return Arrays.asList(nodes);
	}
}
