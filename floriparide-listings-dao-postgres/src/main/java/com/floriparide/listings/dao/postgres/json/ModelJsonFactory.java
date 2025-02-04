package com.floriparide.listings.dao.postgres.json;

import com.floriparide.listings.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

/**
 * @author Mikhail Bragin
 */
public class ModelJsonFactory {

    public static String getBranchJSONData(Branch branch) {

        JSONObject object = new JSONObject();

        if (branch.getDescription() != null && !branch.getDescription().isEmpty())
            object.put(JSONSchema.DESCRIPTION, branch.getDescription());

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
                contact.put(JSONSchema.VALUE, c.getValue());
                contact.put(JSONSchema.BRANCH_DATA_CONTACTS_COMMENT, c.getComment());
                contacts.add(contact);
            }

            object.put(JSONSchema.BRANCH_DATA_CONTACTS, contacts);
        }

        if (branch.getPaymentOptions() != null && !branch.getPaymentOptions().isEmpty()) {
            JSONArray options = new JSONArray();
            for (String o : branch.getPaymentOptions()) {
                JSONObject option = new JSONObject();
                option.put(JSONSchema.BRANCH_DATA_PAYMENT_OPTIONS_OPTION, o.toLowerCase());
                options.add(option);
            }

            object.put(JSONSchema.BRANCH_DATA_PAYMENT_OPTIONS, options);
        }

        if (branch.getAttributes() != null && !branch.getAttributes().isEmpty()) {
            JSONArray attributesGroup = new JSONArray();

            for (Attribute a : branch.getAttributes()) {
                JSONObject agObj = new JSONObject();
                agObj.put(JSONSchema.ID, a.getId());
                agObj.put(JSONSchema.VALUE, a.getCurrentValue());
                attributesGroup.add(agObj);
            }

            object.put(JSONSchema.BRANCH_DATA_ATTRIBUTES, attributesGroup);
        }

        if (branch.getSchedule() != null) {
            JSONObject schedule = new JSONObject();

            JSONArray monday = fillUpIntervals(branch.getSchedule().getMonday());
            JSONArray tuesday = fillUpIntervals(branch.getSchedule().getTuesday());
            JSONArray wednesday = fillUpIntervals(branch.getSchedule().getWednesday());
            JSONArray thursday = fillUpIntervals(branch.getSchedule().getThursday());
            JSONArray friday = fillUpIntervals(branch.getSchedule().getFriday());
            JSONArray saturday = fillUpIntervals(branch.getSchedule().getSaturday());
            JSONArray sunday = fillUpIntervals(branch.getSchedule().getSunday());

            schedule.put(JSONSchema.SCHEDULE_DATA_MONDAY, monday);
            schedule.put(JSONSchema.SCHEDULE_DATA_TUESDAY, tuesday);
            schedule.put(JSONSchema.SCHEDULE_DATA_SATURDAY, wednesday);
            schedule.put(JSONSchema.SCHEDULE_DATA_THURSDAY, thursday);
            schedule.put(JSONSchema.SCHEDULE_DATA_FRIDAY, friday);
            schedule.put(JSONSchema.SCHEDULE_DATA_SATURDAY, saturday);
            schedule.put(JSONSchema.SCHEDULE_DATA_SUNDAY, sunday);

            object.put(JSONSchema.SCHEDULE_FIELD, schedule);
        }

        if (branch.getRawSchedule() != null && !branch.getRawSchedule().isEmpty()) {
            object.put(JSONSchema.BRANCH_RAW_SCHEDULE, branch.getRawSchedule());
        }

        if (branch.getRawAddress() != null && !branch.getRawAddress().isEmpty()) {
            object.put(JSONSchema.BRANCH_RAW_ADDRESS, branch.getRawAddress());
        }

        if (branch.getRubrics() != null && !branch.getRubrics().isEmpty()) {
            JSONArray rubrics = new JSONArray();

            for (Rubric r : branch.getRubrics()) {
                JSONObject rubricObj = new JSONObject();
                rubricObj.put(JSONSchema.ID, r.getId());
                rubrics.add(rubricObj);
            }

            object.put(JSONSchema.BRANCH_DATA_RUBRICS, rubrics);
        }

        return object.toJSONString();
    }

    private static JSONArray fillUpIntervals(List<Interval> intervals) {
        JSONArray intervalArray = new JSONArray();
        for (Interval i : intervals) {
            JSONObject intervalObj = new JSONObject();
            intervalObj.put(JSONSchema.INTERVAL_DATA_FROM, i.getFrom());
            intervalObj.put(JSONSchema.INTERVAL_DATA_TO, i.getTo());
            intervalArray.add(intervalObj);
        }
        return intervalArray;
    }

    public static void populateBranchDataFromJSON(Branch branch, String json) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(json);

        branch.setDescription((String) object.get(JSONSchema.DESCRIPTION));
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
                        (String) c.get(JSONSchema.VALUE),
                        (String) c.get(JSONSchema.BRANCH_DATA_CONTACTS_COMMENT)
                )
                );
            }
        }
        branch.setContacts(contactList);

        List<String> paymentOptionList = new ArrayList<>();
        JSONArray paymentOptions = (JSONArray) object.get(JSONSchema.BRANCH_DATA_PAYMENT_OPTIONS);
        if (paymentOptions != null) {
            Iterator<JSONObject> it = paymentOptions.iterator();
            while (it.hasNext()) {
                JSONObject p = it.next();
                paymentOptionList.add((String) p.get(JSONSchema.BRANCH_DATA_PAYMENT_OPTIONS_OPTION));
            }
        }
        branch.setPaymentOptions(paymentOptionList);

        JSONObject geometry = (JSONObject) object.get(JSONSchema.BRANCH_DATA_GEOMETRY);
        if (geometry != null) {
            JSONObject point = (JSONObject) geometry.get(JSONSchema.BRANCH_DATA_POINT);
            branch.setPoint(new Point((Double) point.get(JSONSchema.BRANCH_DATA_POINT_LAT),
                    (Double) point.get(JSONSchema.BRANCH_DATA_POINT_LON)));
        }

        List<Attribute> attributes = new ArrayList<>();
        JSONArray ags = (JSONArray) object.get(JSONSchema.BRANCH_DATA_ATTRIBUTES);
        if (ags != null) {
            Iterator<JSONObject> it = ags.iterator();
            while (it.hasNext()) {
                JSONObject attribute = it.next();

                attributes.add(new Attribute((Long) attribute.get(JSONSchema.ID),
                        (String) attribute.get(JSONSchema.VALUE)));
            }
        }

        branch.setAttributes(attributes);

        JSONObject scheduleObj = (JSONObject) object.get(JSONSchema.SCHEDULE_FIELD);
        if (scheduleObj != null) {
            Schedule schedule = new Schedule();
            JSONArray dayObj = (JSONArray) scheduleObj.get(JSONSchema.SCHEDULE_DATA_MONDAY);
            Iterator<JSONObject> it = dayObj.iterator();
            JSONObject intervalObj;
            while (it.hasNext()) {
                intervalObj = it.next();
                schedule.addToMonday(new Interval((String) intervalObj.get(JSONSchema.INTERVAL_DATA_FROM),
                        (String) intervalObj.get(JSONSchema.INTERVAL_DATA_TO)));
            }

            dayObj = (JSONArray) scheduleObj.get(JSONSchema.SCHEDULE_DATA_TUESDAY);
            it = dayObj.iterator();
            while (it.hasNext()) {
                intervalObj = it.next();
                schedule.addToTuesday(new Interval((String) intervalObj.get(JSONSchema.INTERVAL_DATA_FROM),
                        (String) intervalObj.get(JSONSchema.INTERVAL_DATA_TO)));
            }

            dayObj = (JSONArray) scheduleObj.get(JSONSchema.SCHEDULE_DATA_WEDNESDAY);
            it = dayObj.iterator();
            while (it.hasNext()) {
                intervalObj = it.next();
                schedule.addToWednesday(new Interval((String) intervalObj.get(JSONSchema.INTERVAL_DATA_FROM),
                        (String) intervalObj.get(JSONSchema.INTERVAL_DATA_TO)));
            }

            dayObj = (JSONArray) scheduleObj.get(JSONSchema.SCHEDULE_DATA_THURSDAY);
            it = dayObj.iterator();
            while (it.hasNext()) {
                intervalObj = it.next();
                schedule.addToThursday(new Interval((String) intervalObj.get(JSONSchema.INTERVAL_DATA_FROM),
                        (String) intervalObj.get(JSONSchema.INTERVAL_DATA_TO)));
            }

            dayObj = (JSONArray) scheduleObj.get(JSONSchema.SCHEDULE_DATA_FRIDAY);
            it = dayObj.iterator();
            while (it.hasNext()) {
                intervalObj = it.next();
                schedule.addToFriday(new Interval((String) intervalObj.get(JSONSchema.INTERVAL_DATA_FROM),
                        (String) intervalObj.get(JSONSchema.INTERVAL_DATA_TO)));
            }

            dayObj = (JSONArray) scheduleObj.get(JSONSchema.SCHEDULE_DATA_SATURDAY);
            it = dayObj.iterator();
            while (it.hasNext()) {
                intervalObj = it.next();
                schedule.addToSaturday(new Interval((String) intervalObj.get(JSONSchema.INTERVAL_DATA_FROM),
                        (String) intervalObj.get(JSONSchema.INTERVAL_DATA_TO)));
            }

            dayObj = (JSONArray) scheduleObj.get(JSONSchema.SCHEDULE_DATA_SUNDAY);
            it = dayObj.iterator();
            while (it.hasNext()) {
                intervalObj = it.next();
                schedule.addToSunday(new Interval((String) intervalObj.get(JSONSchema.INTERVAL_DATA_FROM),
                        (String) intervalObj.get(JSONSchema.INTERVAL_DATA_TO)));
            }

            branch.setSchedule(schedule);
        }

        List<Rubric> rubrics = new ArrayList<>();
        JSONArray rubricsArray = (JSONArray) object.get(JSONSchema.BRANCH_DATA_RUBRICS);
        if (rubricsArray != null) {
            Iterator<JSONObject> it = rubricsArray.iterator();
            while (it.hasNext()) {
                JSONObject rubricObj = it.next();
                rubrics.add(new Rubric((Long) rubricObj.get(JSONSchema.ID)));
            }
        }

        branch.setRubrics(rubrics);


    }

    public static String getAttributesGroupJSONData(AttributesGroup attributesGroup) {
        JSONObject object = new JSONObject();

        if (attributesGroup.getNames() != null && !attributesGroup.getNames().isEmpty())
            object.put(JSONSchema.NAMES, attributesGroup.getNames());

        object.put(JSONSchema.DESCRIPTION, attributesGroup.getDescription());
        object.put(JSONSchema.STRING_ID, attributesGroup.getStringId());

        return object.toJSONString();
    }

    public static void populateAttributesGroupDataFromJSON(AttributesGroup attributesGroup, String json) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(json);
        attributesGroup.setNames(getNamesFromJSON(object));
        attributesGroup.setStringId(object.get(JSONSchema.STRING_ID).toString());
        attributesGroup.setDescription(object.get(JSONSchema.DESCRIPTION).toString());
    }

    public static String getRubricJSONData(Rubric rubric) {
        JSONObject object = new JSONObject();

        if (rubric.getNames() != null && !rubric.getNames().isEmpty())
            object.put(JSONSchema.NAMES, rubric.getNames());
        return object.toJSONString();
    }

    public static String getAttributeJSONData(Attribute attribute) {

        JSONObject object = new JSONObject();

        if (attribute.getNames() != null && !attribute.getNames().isEmpty())
            object.put(JSONSchema.NAMES, attribute.getNames());

        if (attribute.getPossibleValues() != null && !attribute.getPossibleValues().isEmpty())
            object.put(JSONSchema.ATTRIBUTE_POSSIBLE_VALUES, attribute.getPossibleValues());

        if (attribute.getInputType() != null)
            object.put(JSONSchema.ATTRIBUTES_GROUP_DATA_INPUT_TYPE, attribute.getInputType().getType());

        if (attribute.getFilterType() != null)
            object.put(JSONSchema.ATTRIBUTES_GROUP_DATA_FILTER_TYPE, attribute.getFilterType().getType());

        return object.toJSONString();
    }

    public static void populateAttributeDataFromJSON(Attribute attribute, String json) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(json);
        attribute.setNames(getNamesFromJSON(object));

        attribute.setPossibleValues((JSONArray) object.get(JSONSchema.ATTRIBUTE_POSSIBLE_VALUES));

        attribute.setInputType(Attribute.InputType.lookup(
                (String) object.get(JSONSchema.ATTRIBUTES_GROUP_DATA_INPUT_TYPE)));
        attribute.setFilterType(Attribute.FilterType.lookup(
                (String) object.get(JSONSchema.ATTRIBUTES_GROUP_DATA_FILTER_TYPE)));
    }

    public static void populateRubricDataFromJSON(Rubric rubric, String json) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(json);
        rubric.setNames(getNamesFromJSON(object));
    }

    public static Map<String, String> getNamesFromJSON(JSONObject json) throws ParseException {
        JSONObject namesObj = (JSONObject) json.get(JSONSchema.NAMES);
        if (namesObj != null)
            return namesObj;

        return Collections.emptyMap();

    }

    public class JSONSchema {

        public static final String DESCRIPTION = "description";
        public static final String STRING_ID = "string_id";

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
        public static final String BRANCH_DATA_ATTRIBUTES = "attributes";
        public static final String BRANCH_DATA_RUBRICS = "rubrics";

        public static final String ATTRIBUTES_GROUP_DATA_INPUT_TYPE = "input_type";
        public static final String ATTRIBUTES_GROUP_DATA_FILTER_TYPE = "filter_type";

        public static final String ATTRIBUTE_POSSIBLE_VALUES = "possible_values";

        public static final String NAMES = "names";
        public static final String VALUES = "values";
        public static final String VALUE = "value";
        public static final String ID = "id";

        public static final String META_CREATED = "created";
        public static final String META_UPDATED = "updated";
        public static final String SCHEDULE_DATA_MONDAY = "monday";
        public static final String SCHEDULE_DATA_TUESDAY = "tuesday";
        public static final String SCHEDULE_DATA_WEDNESDAY = "wednesday";
        public static final String SCHEDULE_DATA_THURSDAY = "thursday";
        public static final String SCHEDULE_DATA_FRIDAY = "friday";
        public static final String SCHEDULE_DATA_SATURDAY = "saturday";
        public static final String SCHEDULE_DATA_SUNDAY = "sunday";
        public static final String INTERVAL_DATA_FROM = "from";
        public static final String INTERVAL_DATA_TO = "to";
        public static final String SCHEDULE_FIELD = "schedule";
        public static final String BRANCH_RAW_ADDRESS = "raw_address";
        public static final String BRANCH_RAW_SCHEDULE = "raw_schedule";
    }
}
