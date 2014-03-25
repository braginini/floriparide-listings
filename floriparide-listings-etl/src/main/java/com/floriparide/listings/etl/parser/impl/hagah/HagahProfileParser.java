package com.floriparide.listings.etl.parser.impl.hagah;

import com.fasterxml.jackson.databind.JsonNode;
import com.floriparide.listings.etl.parser.Parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class HagahProfileParser implements Parser<JsonNode> {
	@Override
	public JsonNode parse(String resource) {

		Document doc = Jsoup.parse(resource);

		Element orgNameEl = doc.getElementsByClass("org").get(0);
		String name = orgNameEl.text();

		List<String> caategories = new ArrayList<>();
		Element categoryEl = doc.getElementsByClass("categoriasDetalhe").get(0);
		for (Element e : categoryEl.getElementsByAttributeValueStarting("id", "cat_")) {
			caategories.add(e.text());
		}

		Element addressEl = doc.getElementsByClass("link-endereco").get(0);
		String address = addressEl.text();   //todo check
		address += addressEl.nextSibling().toString();
		address += ((Element) addressEl.nextSibling().nextSibling()).text();

		Elements webSiteEls = doc.getElementsByAttributeValueStarting("onclick",
				"SLoader.getObj('GA').tracker('Detalhe', 'Engajamento', 'Site');;");
		String website = null;

		String principalText = getSingleElementByClass("textoPrincipal", doc);

		//todo article

		String hours = null;

		List<String> characteristics = new ArrayList<>();
		Elements characteristicsElements = doc.getElementsByClass("caract");
		if (characteristicsElements != null && !characteristicsElements.isEmpty()) {
			for (Element e : characteristicsElements.get(0).getElementsByClass("listaCaract")) {
				for (Element span : e.getElementsByTag("span")) {
					characteristics.add(span.text());
				}
			}
		}

		List<String> cards = new ArrayList<>();
		Elements cardsEls = doc.getElementsByClass("cartao");
		if (characteristicsElements != null && !characteristicsElements.isEmpty()) {
			for (Element ciEl : cardsEls) {
				String cardTypeString = ciEl.getElementsByTag("h3").get(0).text();
				CardType cardType = CardType.lookup(cardTypeString); //todo think if need this
				for (Element e : ciEl.getElementsByClass("listaCartoes")) {
					for (Element li : e.getElementsByTag("li")) {
						for (Element cardEl : li.children()) {
							if (!cardEl.attr("title").isEmpty())
								cards.add(cardEl.attr("title"));
						}
					}
				}
			}
		}

		String openedFrom = null;
		String updated = null;
		Integer capacity = null;
		Elements infoEls = doc.getElementsByClass("itemInfo");
		for (Element infoEl : infoEls) {
			if (infoEl.getElementsContainingText("Horário de atendimento:") != null &&
					!infoEl.getElementsContainingText("Horário de atendimento:").isEmpty()) {
				//working hours
				hours = infoEl.getElementsByTag("span").get(0).text();
			} else if (infoEl.getElementsContainingText("Sites:") != null &&
					!infoEl.getElementsContainingText("Sites:").isEmpty()) {
				website = infoEl.getElementsByAttributeValueContaining("onclick",
						"SLoader.getObj('GA').detailTracker('Site');").get(0).text();

			} else if (infoEl.getElementsContainingText("Aberto desde: ") != null &&
					!infoEl.getElementsContainingText("Aberto desde: ").isEmpty()) {
				openedFrom = infoEl.getElementsByTag("span").get(0).text();
			} else if (infoEl.getElementsByTag("script") != null &&
					!infoEl.getElementsByTag("script").isEmpty()) {
				//Element el = infoEl.getElementsByClass("atualizadoTempo").get(0);
				//updated = el.text().split(": ")[1];
			} else if (infoEl.getElementsContainingText("Capacidade:") != null &&
					!infoEl.getElementsContainingText("Capacidade:").isEmpty()) {
				capacity = Integer.valueOf(infoEl.getElementsByTag("span").get(0).text().split(" Lugares.")[0]);
			}
		}


		return null;
	}

	private String getSingleElementByClass(String className, Element doc) {
		Elements els = doc.getElementsByClass("className");
		if (els != null && !els.isEmpty()) {
			return els.get(0).text();
		}

		return null;
	}

	public enum CardType {
		CREDIT("Aceita Cartão de Crédito:"), DEBIT("Aceita Cartão de Débito:"), FOOD("Aceita Tíquetes:");

		String text;

		CardType(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}

		static Map<String, CardType> map = new HashMap<>();

		static {
			for (CardType cardType : CardType.values())
				map.put(cardType.getText(), cardType);
		}

		public static CardType lookup(String text) {
			return (text != null) ? map.get(text.toLowerCase()) : null;
		}
	}
}
