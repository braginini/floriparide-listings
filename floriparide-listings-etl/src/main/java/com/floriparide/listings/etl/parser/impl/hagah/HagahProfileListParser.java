package com.floriparide.listings.etl.parser.impl.hagah;

import com.floriparide.listings.etl.parser.Parser;

import com.floriparide.listings.etl.parser.util.HttpConnector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class HagahProfileListParser implements Parser<List<String>> {

    @Override
    public List<String> parse(String resource) throws IOException {
        ArrayList<String> result = new ArrayList<>();

        Document doc = Jsoup.parse(resource);

        Elements lineElements = doc.getElementsByClass("linha");

        if (lineElements != null && !lineElements.isEmpty()) {
            for (Element e : lineElements) {
                if (!e.getElementsByClass("destacado").isEmpty()) {
                    System.out.println("highlighted");
                    //todo increment here
                }
                Element el = e.getElementsByAttributeValueStarting("title", "Veja mais detalhes ").get(0);

                String url = el.attr("href");
                result.add(url);
            }
        }

        return result;
    }

    public static void main(String[] args) throws IOException {
        HagahProfileListParser p = new HagahProfileListParser();
        String url = args[0];

        for (String s : p.parse(HttpConnector.getPageAsString(url, new HashMap<String, String>()))) {
            System.out.println(s);
        }
    }
}
