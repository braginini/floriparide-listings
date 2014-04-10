package com.floriparide.listings.etl.parser.impl.hagah;

import com.floriparide.listings.etl.parser.Parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * @author Mikhail Bragin
 */
public class HagahProfileListPagesCountParser implements Parser<Integer> {

	@Override
	public Integer parse(String resource) throws IOException {

		Document doc = Jsoup.parse(resource);
		Element resultsNumEl = doc.getElementsByClass("spanResultado").first();
		if (resultsNumEl != null) {
			String raw = resultsNumEl.text();
			int resultsNum = Integer.valueOf(raw.split(new String(new char[]{160}))[0]);
			return resultsNum / 20 + 1;
		}

		return 0;
	}
}
