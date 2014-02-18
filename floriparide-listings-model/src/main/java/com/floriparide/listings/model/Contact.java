package com.floriparide.listings.model;

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

		EMAIL, WEBSITE, PHONE, FAX, SKYPE, FACEBOOK

	}
}
