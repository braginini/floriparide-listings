package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.Contact;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a web representation of {@link com.floriparide.listings.model.Contact} object
 *
 * @author Mikhail Bragin
 */
public class ContactElement implements Element<Contact> {

	@JsonProperty("")
	Long id;

	@JsonProperty("")
	String type;

	@JsonProperty("")
	String value;

	@JsonProperty("")
	String comment;

	public ContactElement() {
	}

	public ContactElement(@NotNull Contact contact) {
		this.id = contact.getId();
		this.type = contact.getType().name();
		this.value = contact.getValue();
		this.comment = contact.getComment();
	}


	public static List<ContactElement> contactsToContactElements(@NotNull List<Contact> contacts) {
		List<ContactElement> contactElements = new ArrayList<>(contacts.size());
		for (Contact c : contacts) {
			contactElements.add(new ContactElement(c));
		}

		return contactElements;
	}

	public static List<Contact> contactsElementsToToContacts(@NotNull List<ContactElement> contactElements) {
		List<Contact> contacts = new ArrayList<Contact>(contactElements.size());
		for (ContactElement c : contactElements) {
			contacts.add(c.getModel());
		}

		return contacts;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	@JsonIgnore
	public Contact getModel() {
		return new Contact(id, Contact.ContactType.lookup(type), value, comment);
	}
}
