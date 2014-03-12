package com.floriparide.listings.etl.parser.impl.abrasel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.floriparide.listings.etl.parser.ProfileParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class AbraselProfileParser implements ProfileParser<JsonNode> {

	static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	@Override
	public JsonNode parse(String resource) {

		ObjectNode obj = mapper.createObjectNode();
		final JsonNodeFactory factory = JsonNodeFactory.instance;

		Document doc = Jsoup.parse(resource);

		String name = doc.getElementById("titulo_central").text().split("-")[0];
		if (name.charAt(name.length() - 1) == ' ')
			name = name.substring(0, name.length());
		obj.put("name", name);

		Element descriptionEl = doc.getElementById("detalhamento");
		String description = null;
		obj.put("description", description);

		Elements itemEls = descriptionEl.getElementsByTag("p");
		itemEls.remove(0);
		itemEls.remove(itemEls.size() - 1);


		Element facilityEl = itemEls.first();
		String facilities = getDetailsValueString(facilityEl);
		List<String> facilitiesList = splitMultipleValues(facilities);
		ArrayNode facilitiesNode = createArrayNodeFromList(facilitiesList, factory);
		obj.put("facilities", facilitiesNode);

		Element specialityEl = itemEls.get(1);
		String speciality = getDetailsValueString(specialityEl);
		List<String> specialityList = splitMultipleValues(speciality);
		ArrayNode specialityNode = createArrayNodeFromList(specialityList, factory);
		obj.put("specialities", specialityNode);

		Element categoryEl = itemEls.get(2);
		String category = getDetailsValueString(categoryEl);
		List<String> categoryList = splitMultipleValues(category);
		ArrayNode categoryNode = createArrayNodeFromList(categoryList, factory);
		obj.put("categories", categoryNode);

		Element capacityEl = itemEls.get(3);
		String capacity = getDetailsValueString(capacityEl);
		capacity = capacity.substring(0, capacity.length() - 8);
		capacity = capacity.replace(" ", "");
		obj.put("capacity", capacity);

		Element averagePriceEl = itemEls.get(4).getElementById("txtValorMedioEstab");
		String avgPrice = null;
		if (averagePriceEl != null) {
			avgPrice = averagePriceEl.attr("value");
			obj.put("avg_price", avgPrice);
		}

		Element workHoursEl = itemEls.get(5);
		String workHours = getDetailsValueString(workHoursEl);
		obj.put("hours", workHours);

		Element paymentEl = itemEls.get(6);
		String payment = getDetailsValueString(paymentEl);
		List<String> paymentList = splitMultipleValues(payment);
		ArrayNode paymentNode = createArrayNodeFromList(paymentList, factory);
		obj.put("payment_options", paymentNode);

		Element menuEl = itemEls.get(7);
		String menu = getDetailsValueString(menuEl);
		obj.put("menu", menu);

		Element contactsEl = doc.getElementById("estab_endereco");
		ArrayNode contacts = factory.arrayNode();
		if (contactsEl != null) {

			ObjectNode phoneNode = factory.objectNode();

			String phone = contactsEl.getElementById("txtTelefone").attr("value");
			phone = phone.replace("-", "");
			phone = phone.replace(")", "");
			phone = phone.replace("(", "");
			phone = phone.replace(" ", "");
			phoneNode.put("type", "phone");
			phoneNode.put("value", phone);
			contacts.add(phoneNode);


			Elements style2Els = contactsEl.getElementsByClass("style2");
			String address = style2Els.get(0).text();
			obj.put("address", address);

			String email = style2Els.get(2).text();
			ObjectNode emailNode = factory.objectNode();
			emailNode.put("type", "email");
			emailNode.put("value", email);
			contacts.add(emailNode);

			String website = contactsEl.getElementsByTag("a").get(0).attr("href");
			if (website.equals("http://"))
				website = null;

			ObjectNode websiteNode = factory.objectNode();
			websiteNode.put("type", "website");
			websiteNode.put("value", website);
			contacts.add(websiteNode);


		}
		obj.put("contacts", contacts);

		System.out.println(obj.toString());
		return obj;
	}

	private ArrayNode createArrayNodeFromList(List<String> list, JsonNodeFactory factory) {
		ArrayNode node = factory.arrayNode();
		for (String s : list) {
			node.add(s);
		}
		return node;
	}

	private List<String> splitMultipleValues(String multipleValue) {
		List<String> resultList = new ArrayList<>();
		if (multipleValue != null) {
			String[] fa = multipleValue.split(",");
			if (fa.length > 1) {
				String[] lastSplit = fa[fa.length - 1].split(" e ");
				for (String s : lastSplit) {
					s = replaceSpaceAtTheBeginning(s);

					resultList.add(s);
				}

				fa[fa.length - 1] = null;
			}

			for (String s : fa) {
				if (s != null) {
					s = replaceSpaceAtTheBeginning(s);

					resultList.add(s);
				}
			}
		}
		return resultList;
	}

	private String replaceSpaceAtTheBeginning(String s) {
		if (s.charAt(0) == ' ')
			s = s.substring(1, s.length());
		return s;
	}

	private String getDetailsValueString(Element el) {
		String value = null;
		if (el.textNodes() != null && el.textNodes().size() > 0) {
			value = el.textNodes().get(0).text();
		}
		return value;
	}
}
