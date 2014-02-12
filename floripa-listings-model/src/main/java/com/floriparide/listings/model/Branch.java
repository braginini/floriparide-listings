package com.floriparide.listings.model;

import java.util.List;

/**
 * Created by Mikhail Bragin
 */
public class Branch {

	Long id;

	String name;

	Long companyId;

	String description;

	Point point;

	List<Contact> contacts;

	String address;

	//additional location info like floor
	String office;

	//ISO 4217 format
	String currency;

	List<PaymentOption> paymentOptions;

	List<Rubric> rubrics;

	Schedule schedule;

	//only for paid
	String article;

	public Branch(Long id, String name, Long companyId, String description, Point point, List<Contact> contacts, String address, String office, String currency, List<PaymentOption> paymentOptions, List<Rubric> rubrics, Schedule schedule, String article) {
		this.id = id;
		this.name = name;
		this.companyId = companyId;
		this.description = description;
		this.point = point;
		this.contacts = contacts;
		this.address = address;
		this.office = office;
		this.currency = currency;
		this.paymentOptions = paymentOptions;
		this.rubrics = rubrics;
		this.schedule = schedule;
		this.article = article;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public String getDescription() {
		return description;
	}

	public Point getPoint() {
		return point;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public String getAddress() {
		return address;
	}

	public String getOffice() {
		return office;
	}

	public String getCurrency() {
		return currency;
	}

	public List<PaymentOption> getPaymentOptions() {
		return paymentOptions;
	}

	public List<Rubric> getRubrics() {
		return rubrics;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public String getArticle() {
		return article;
	}
}
