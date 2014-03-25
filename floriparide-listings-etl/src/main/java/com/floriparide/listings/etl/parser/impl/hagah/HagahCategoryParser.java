package com.floriparide.listings.etl.parser.impl.hagah;

import com.floriparide.listings.etl.parser.Parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses category page and returns a list of  category urls
 * @author Mikhail Bragin
 */
public class HagahCategoryParser implements Parser<List<String>> {


	@Override
	public List<String> parse(String resource) throws IOException {
		ArrayList<String> result = new ArrayList<>();

		Document doc = Jsoup.parse(resource);

		Elements categoryListEls = doc.getElementsByClass("lista-categorias");

		if (categoryListEls != null && !categoryListEls.isEmpty()) {
			Element categoriesEl = categoryListEls.get(0);

			Elements elements = categoriesEl.getElementsByAttributeValueContaining("onclick", "SLoader.getObj('GA').tracker('Categoria',");

			if (elements != null && !elements.isEmpty()) {
				for (Element e : elements) {
					String url = e.attr("href");
					result.add(url);
				}
			}

		}

		return result;
	}
}
