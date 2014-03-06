package com.floriparide.listings.dao.postgres.json;

import com.floriparide.listings.model.AttributesGroup;
import com.floriparide.listings.model.Branch;
import com.floriparide.listings.model.Contact;
import com.floriparide.listings.model.MetaModel;
import com.floriparide.listings.model.PaymentOption;
import com.floriparide.listings.model.Point;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class ModelJsonFactory {

	public static String getBranchJSONData(Branch branch) {

		JSONObject object = new JSONObject();

		if (branch.getDescription() != null && !branch.getDescription().isEmpty())
			object.put(JSONSchema.BRANCH_DATA_DESCRIPTION, branch.getDescription());

		if (branch.getArticle() != null && !branch.getArticle().isEmpty())
			object.put(JSONSchema.BRANCH_DATA_ARTICLE, branch.getArticle());

		if (branch.getAddress() != null && !branch.getAddress().isEmpty())
			object.put(JSONSchema.BRANCH_DATA_ADDRESS, branch.getAddress());

		if (branch.getOffice() != null && !branch.getOffice().isEmpty())
			object.put(JSONSchema.BRANCH_DATA_OFFICE, branch.getOffice());

		if (branch.getCurrency() != null && !branch.getCurrency().isEmpty())
			object.put(JSONSchema.BRANCH_DATA_CURRENCY, branch.getCurrency());

		if (branch.getPoint() != null && branch.getPoint().getLat() != null && branch.getPoint().getLon() != null) {
			JSONObject latLon = new JSONObject();
			latLon.put(JSONSchema.BRANCH_DATA_POINT_LAT, branch.getPoint().getLat());
			latLon.put(JSONSchema.BRANCH_DATA_POINT_LON, branch.getPoint().getLon());

			JSONObject point = new JSONObject();
			point.put(JSONSchema.BRANCH_DATA_POINT, latLon);

			object.put(JSONSchema.BRANCH_DATA_GEOMETRY, point);
		}

		if (branch.getContacts() != null && !branch.getContacts().isEmpty()) {
			JSONArray contacts = new JSONArray();
			for (Contact c : branch.getContacts()) {
				JSONObject contact = new JSONObject();
				contact.put(JSONSchema.BRANCH_DATA_CONTACTS_CONTACT, c.getType().name().toLowerCase());
				contact.put(JSONSchema.BRANCH_DATA_CONTACTS_VALUE, c.getValue());
				contact.put(JSONSchema.BRANCH_DATA_CONTACTS_COMMENT, c.getComment());
				contacts.add(contact);
			}

			object.put(JSONSchema.BRANCH_DATA_CONTACTS, contacts);
		}

		if (branch.getPaymentOptions() != null && !branch.getPaymentOptions().isEmpty()) {
			JSONArray options = new JSONArray();
			for (PaymentOption o : branch.getPaymentOptions()) {
				JSONObject option = new JSONObject();
				option.put(JSONSchema.BRANCH_DATA_PAYMENT_OPTIONS_OPTION, o.name().toLowerCase());
				options.add(option);
			}

			object.put(JSONSchema.BRANCH_DATA_PAYMENT_OPTIONS, options);
		}

		if (branch.getAttributes() != null && !branch.getAttributes().isEmpty()) {
			JSONArray attributesGroup = new JSONArray();

			for (AttributesGroup ag : branch.getAttributes()) {
				JSONObject agObj = new JSONObject();
				agObj.put(JSONSchema.ID, ag.getId());
				JSONArray values = new JSONArray();
				for (String s : ag.getValues()) {
					values.add(s);
				}

				agObj.put(JSONSchema.VALUES, values);
				attributesGroup.add(agObj);
			}

			object.put(JSONSchema.BRANCH_DATA_ATTRIBUTES_GROUPS, attributesGroup);
		}

		return object.toJSONString();
	}

	public static void populateBranchDataFromJSON(Branch branch, String json) throws ParseException {
		JSONParser parser = new JSONParser();
		JSONObject object = (JSONObject) parser.parse(json);

		branch.setDescription((String) object.get(JSONSchema.BRANCH_DATA_DESCRIPTION));
		branch.setArticle((String) object.get(JSONSchema.BRANCH_DATA_ARTICLE));
		branch.setOffice((String) object.get(JSONSchema.BRANCH_DATA_OFFICE));
		branch.setCurrency((String) object.get(JSONSchema.BRANCH_DATA_CURRENCY));
		branch.setAddress((String) object.get(JSONSchema.BRANCH_DATA_ADDRESS));

		List<Contact> contactList = new ArrayList<>();
		JSONArray contacts = (JSONArray) object.get(JSONSchema.BRANCH_DATA_CONTACTS);
		if (contacts != null) {
			Iterator<JSONObject> it = contacts.iterator();
			while (it.hasNext()) {
				JSONObject c = it.next();
				contactList.add(new Contact(
						Contact.ContactType.lookup((String) c.get(JSONSchema.BRANCH_DATA_CONTACTS_CONTACT)),
						(String) c.get(JSONSchema.BRANCH_DATA_CONTACTS_VALUE),
						(String) c.get(JSONSchema.BRANCH_DATA_CONTACTS_COMMENT)
				)
				);
			}
		}
		branch.setContacts(contactList);

		List<PaymentOption> paymentOptionList = new ArrayList<>();
		JSONArray paymentOptions = (JSONArray) object.get(JSONSchema.BRANCH_DATA_PAYMENT_OPTIONS);
		if (paymentOptions != null) {
			Iterator<JSONObject> it = paymentOptions.iterator();
			while (it.hasNext()) {
				JSONObject p = it.next();
				paymentOptionList.add(PaymentOption.lookup((String) p.get(JSONSchema.BRANCH_DATA_PAYMENT_OPTIONS_OPTION)));
			}
		}
		branch.setPaymentOptions(paymentOptionList);

		JSONObject geometry = (JSONObject) object.get(JSONSchema.BRANCH_DATA_GEOMETRY);
		if (geometry != null) {
			JSONObject point = (JSONObject) geometry.get(JSONSchema.BRANCH_DATA_POINT);
			branch.setPoint(new Point((Double) point.get(JSONSchema.BRANCH_DATA_POINT_LAT),
					(Double) point.get(JSONSchema.BRANCH_DATA_POINT_LON)));
		}

		List<AttributesGroup> attributesGroups = new ArrayList<>();
		JSONArray ags = (JSONArray) object.get(JSONSchema.BRANCH_DATA_ATTRIBUTES_GROUPS);
		if (ags != null) {
			Iterator<JSONObject> it = ags.iterator();
			while (it.hasNext()) {
				JSONObject attribute = it.next();

				JSONArray valuesArray = (JSONArray) attribute.get(JSONSchema.VALUES);
				ArrayList<String> values = new ArrayList<>();
				Iterator<String> vIt = valuesArray.iterator();

				while (vIt.hasNext())
					values.add(vIt.next());

				attributesGroups.add(new AttributesGroup((Long) attribute.get(JSONSchema.ID),
						values));
			}
		}

		branch.setAttributes(attributesGroups);
	}

	public static String getAttributesGroupJSONData(AttributesGroup attributesGroup) {
		JSONObject object = new JSONObject();

		if (attributesGroup.getNames() != null && !attributesGroup.getNames().isEmpty())
			object.put(JSONSchema.NAMES, attributesGroup.getNames());

		if (attributesGroup.getInputType() != null)
			object.put(JSONSchema.ATTRIBUTES_GROUP_DATA_INPUT_TYPE, attributesGroup.getInputType().getType());

		if (attributesGroup.getFilterType() != null)
			object.put(JSONSchema.ATTRIBUTES_GROUP_DATA_FILTER_TYPE, attributesGroup.getFilterType().getType());

		if (attributesGroup.getValues() != null && !attributesGroup.getValues().isEmpty()) {
			JSONArray array = new JSONArray();


			for (String v : attributesGroup.getValues())
				array.add(v);

			object.put(JSONSchema.VALUES, array);
		}

		return object.toJSONString();
	}

	public static void populateAttributesGroupDataFromJSON(AttributesGroup attributesGroup, String json) throws ParseException {
		JSONParser parser = new JSONParser();
		JSONObject object = (JSONObject) parser.parse(json);

		JSONObject namesObj = (JSONObject) object.get(JSONSchema.NAMES);
		if (namesObj != null)
			attributesGroup.setNames(getNamesFromJSON(namesObj.toJSONString()));

		attributesGroup.setInputType(AttributesGroup.InputType.lookup(
				(String) object.get(JSONSchema.ATTRIBUTES_GROUP_DATA_INPUT_TYPE)));
		attributesGroup.setFilterType(AttributesGroup.FilterType.lookup(
				(String) object.get(JSONSchema.ATTRIBUTES_GROUP_DATA_FILTER_TYPE)));

		JSONArray array = (JSONArray) object.get(JSONSchema.VALUES);
		ArrayList<String> values = new ArrayList<>();
		if (array != null && !array.isEmpty()) {
			Iterator it = array.iterator();

			while (it.hasNext()) {
				values.add((String) it.next());
			}
		}

		attributesGroup.setValues(values);

	}

	public static String getNamesJSON(Map<String, String> map) {
		return new JSONObject(map).toJSONString();
	}

	public static Map<String, String> getNamesFromJSON(String json) throws ParseException {
		HashMap<String, String> map = new HashMap<>();

		JSONParser parser = new JSONParser();
		return (Map<String, String>) parser.parse(json);

	}

	public class JSONSchema {

		public static final String BRANCH_DATA_DESCRIPTION = "description";
		public static final String BRANCH_DATA_ARTICLE = "article";
		public static final String BRANCH_DATA_ADDRESS = "address";
		public static final String BRANCH_DATA_OFFICE = "office";
		public static final String BRANCH_DATA_CURRENCY = "currency";
		public static final String BRANCH_DATA_GEOMETRY = "geometry";
		public static final String BRANCH_DATA_POINT = "point";
		public static final String BRANCH_DATA_POINT_LAT = "lat";
		public static final String BRANCH_DATA_POINT_LON = "lon";
		public static final String BRANCH_DATA_CONTACTS = "contacts";
		public static final String BRANCH_DATA_CONTACTS_CONTACT = "contact";
		public static final String BRANCH_DATA_CONTACTS_VALUE = "value";
		public static final String BRANCH_DATA_CONTACTS_COMMENT = "comment";
		public static final String BRANCH_DATA_PAYMENT_OPTIONS = "payment_options";
		public static final String BRANCH_DATA_PAYMENT_OPTIONS_OPTION = "option";
		public static final String BRANCH_DATA_ATTRIBUTES_GROUPS = "attributes_groups";

		public static final String ATTRIBUTES_GROUP_DATA_INPUT_TYPE = "input_type";
		public static final String ATTRIBUTES_GROUP_DATA_FILTER_TYPE = "filter_type";

		public static final String NAMES = "names";
		public static final String VALUES = "values";
		public static final String ID = "id";

		public static final String META_CREATED = "created";
		public static final String META_UPDATED = "updated";
	}
}
