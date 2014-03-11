package com.floriparide.listings.etl.parser.impl.abrasel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * @author Mikhail Bragin
 */
public class StartPageParser {

	public int parse(String url) throws IOException {

		Document doc = Jsoup.parse(new URL(url), 60000);

		Elements pageEls = doc.getElementsByClass("totalPaginas destaqueCorLista");
		Element el = pageEls.get(0);

		return Integer.valueOf(el.text());
	}
}
