package com.floriparide.listings.etl.parser.impl.hagah;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.floriparide.listings.etl.parser.Parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mikhail Bragin
 */
public class HagahProfileParser implements Parser<JsonNode> {

	static ObjectMapper mapper;
	final static JsonNodeFactory factory;


	static {
		mapper = new ObjectMapper();
		factory = JsonNodeFactory.instance;
		//mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	@Override
	public JsonNode parse(String resource) {
		ObjectNode obj = mapper.createObjectNode();


		Document doc = Jsoup.parse(resource);

		Element orgNameEl = doc.getElementsByClass("org").get(0);
		String name = orgNameEl.text();
		obj.put("name", name);

		List<String> categories = new ArrayList<>();
		Element categoryEl = doc.getElementsByClass("categoriasDetalhe").get(0);
		for (Element e : categoryEl.getElementsByAttributeValueStarting("id", "cat_")) {
			categories.add(e.text());
		}

		ArrayNode categoryNode = createArrayNodeFromList(categories, factory);
		obj.put("categories", categoryNode);

		String address = null;
		String website = null;
		List<String> phones = new ArrayList<>();
		Elements infoE = doc.getElementsByClass("infoDetalhe");
		if (infoE != null && !infoE.isEmpty()) {
			Element ul = infoE.first().getElementsByTag("ul").first();
			for (Element e : ul.getElementsByTag("li")) {

				//street address
				Elements children = e.getElementsByClass("link-endereco");
				for (Element el : children) {
					el = children.first();
					StringBuilder sb = new StringBuilder(el.text());
					sb.append(((TextNode) el.nextSibling()).text());
					sb.append(((Element) el.nextSibling().nextSibling()).text());
					address = sb.toString();
					break;
				}

				//region (city, state)
				children = e.getElementsByClass("region");
				for (Element el : children) {
					StringBuilder sb = new StringBuilder(address != null ? address : "");
					sb.append(" ");
					sb.append(e.text());
					address = sb.toString();
					break;
				}

				children = e.getElementsByAttributeValue("onclick", "SLoader.getObj('GA').tracker('Detalhe', 'Engajamento', 'Site');;");
				for (Element el : children) {
					website = el.text();
				}
				children = e.getElementsByClass("telefone");
				if (!children.isEmpty()) {
					String raw = e.text().replace("Telefones: ", "");
					String[] split = raw.split(" ");
					boolean isCode = true;
					String phone = "";
					for (String s : split) {
						if (isCode) {
							phone += s;
							isCode = false;
						} else {
							phone += s;
							isCode = true;
							phone = phone.replace("-", "");
							phones.add(phone);
							phone = "";
						}
					}
				}
			}
		}
		obj.put("address", address);
		ArrayNode contacts = factory.arrayNode();
		if (website != null) {
			ObjectNode n = factory.objectNode();
			n.put("type", "website");
			n.put("value", website);
			contacts.add(n);
		}

		for (String c : phones) {
			ObjectNode n = factory.objectNode();
			n.put("type", "phone");
			n.put("value", c);
			contacts.add(n);
		}

		obj.put("contacts", contacts);


		String principalText = getSingleElementByClass("textoPrincipal", doc);
		if (principalText != null) {
			principalText = principalText.replace("\"", "");
			obj.put("promo", principalText);
		}

		String description = null;
		Elements articleEls = doc.getElementsByTag("article");
		for (Element e : articleEls) {
			for (Element p : e.getElementsByTag("p")) {
				description = p.text();
				break;
			}
			break;
		}
		obj.put("description", description);

		String hours = null;

		List<String> facilities = new ArrayList<>();
		Elements characteristicsElements = doc.getElementsByClass("caract");
		if (characteristicsElements != null && !characteristicsElements.isEmpty()) {
			for (Element e : characteristicsElements.get(0).getElementsByClass("listaCaract")) {
				for (Element span : e.getElementsByTag("span")) {
					facilities.add(span.text());
				}
			}
		}


		ArrayNode facilitiesNode = createArrayNodeFromList(facilities, factory);
		obj.put("facilities", facilitiesNode);

		List<String> cards = new ArrayList<>();
		Map<String, String> addInfo = new HashMap<>();
		Elements cardsEls = doc.getElementsByClass("cartao");
		if (cardsEls != null && !cardsEls.isEmpty()) {
			for (Element ciEl : cardsEls) {
				String cardTypeString = ciEl.getElementsByTag("h3").get(0).text();
				CardType cardType = CardType.lookup(cardTypeString); //todo think if need this
				for (Element e : ciEl.getElementsByClass("listaCartoes")) {
					for (Element li : e.getElementsByTag("li")) {
						for (Element cardEl : li.children()) {
							if (!cardEl.attr("title").isEmpty())
								cards.add(cardEl.attr("title"));
							else {
								Element p = cardEl.parent().parent().parent().getElementsByTag("h3").first();
								if (p != null) {
									addInfo.put(p.text().replace(":", ""), cardEl.text());
								}
							}
						}
					}
				}
			}
		}

		ArrayNode paymentNode = createArrayNodeFromList(cards, factory);
		ArrayNode addInfoNode = factory.arrayNode();
		for (Map.Entry<String, String> e : addInfo.entrySet()) {
			ObjectNode n = factory.objectNode();
			n.put("name", e.getKey());
			n.put("value", e.getValue());
			addInfoNode.add(n);
		}
		obj.put("payment_options", paymentNode);
		obj.put("add_info", addInfoNode);

		String openedFrom = null;
		String updated = null;
		Integer capacity = null;
		Elements infoEls = doc.getElementsByClass("itemInfo");
		for (Element infoEl : infoEls) {
			if (infoEl.getElementsContainingText("Horário de atendimento:") != null &&
					!infoEl.getElementsContainingText("Horário de atendimento:").isEmpty()) {
				//working hours
				hours = infoEl.getElementsByTag("span").get(0).text();
			} else if (infoEl.getElementsContainingText("Aberto desde: ") != null &&
					!infoEl.getElementsContainingText("Aberto desde: ").isEmpty()) {
				openedFrom = infoEl.getElementsByTag("span").get(0).text();
			} else if (infoEl.getElementsByTag("script") != null &&
					!infoEl.getElementsByTag("script").isEmpty()) {
				Element el = infoEl.select("script").first();
				Pattern p = Pattern.compile("(?is)data = \'(.+?)\'"); // Regex for the value of the key
				Matcher m = p.matcher(el.html());
				while (m.find()) {
					updated = m.group(1);
					break;
				}
			} else if (infoEl.getElementsContainingText("Capacidade:") != null &&
					!infoEl.getElementsContainingText("Capacidade:").isEmpty()) {
				capacity = Integer.valueOf(infoEl.getElementsByTag("span").get(0).text().split(" Lugares.")[0]);
			}
		}

		obj.put("hours", hours);
		obj.put("updated", updated);  //todo updated
		obj.put("capacity", capacity);
		obj.put("since", openedFrom);

		//todo add contacts object!!!!!!!!!!!!!!!!!!! and add website and address and telephone
		return obj;
	}

	//todo duplicate in @{link AbraselProfileParser}
	private ArrayNode createArrayNodeFromList(List<String> list, JsonNodeFactory factory) {
		ArrayNode node = factory.arrayNode();
		for (String s : list) {
			node.add(s);
		}
		return node;
	}

	private String getSingleElementByClass(String className, Element doc) {
		Elements els = doc.getElementsByClass(className);
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
