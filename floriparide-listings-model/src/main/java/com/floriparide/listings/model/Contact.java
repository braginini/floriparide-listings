package com.floriparide.listings.model;

import java.util.HashMap;

/**
 * Created by Mikhail Bragin
 */
public class Contact {

	Long id;

	ContactType type;

	String value;

	String comment;

	public Contact(ContactType type, String value, String comment) {
		this(type, value);
		this.comment = comment;
	}

	public Contact(ContactType type, String value) {
		this.type = type;
		this.value = value;
	}

	public Contact(Long id, ContactType type, String value) {
		this(id, type, value, null);
	}

	public Contact(Long id, ContactType type, String value, String comment) {
		this(type, value, comment);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public String getComment() {
		return comment;
	}

	public ContactType getType() {
		return type;
	}

	public enum ContactType {

		EMAIL("email"), WEBSITE("website"), PHONE("phone"), FAX("fax"), SKYPE("skype"), FACEBOOK("facebook"), TWITTER("twitter");

		String type;

		ContactType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}

		public static final HashMap<String, ContactType> map = new HashMap<>();

		static {
			for (ContactType t : ContactType.values())
				map.put(t.getType(), t);
		}

		public static ContactType lookup(String type) {
			return (type != null) ? map.get(type.toLowerCase()) : null;
		}
	}
}
