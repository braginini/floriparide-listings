package com.floriparide.listings.model;

/**
 * @author Mikhail Bragin
 */
public class RawData {

	Long id;

	String data;

	public RawData(Long id, String data) {
		this.id = id;
		this.data = data;
	}

	public RawData(String data) {
		this.data = data;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
