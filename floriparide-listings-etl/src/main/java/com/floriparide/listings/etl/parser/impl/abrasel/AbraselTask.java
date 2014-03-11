package com.floriparide.listings.etl.parser.impl.abrasel;

import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class AbraselTask {

	Map<String, String> formData;

	String url;

	public AbraselTask(String url, Map<String, String> formData) {
		this.formData = formData;
		this.url = url;
	}

	public Map<String, String> getFormData() {
		return formData;
	}

	public void setFormData(Map<String, String> formData) {
		this.formData = formData;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "AbraselTask{" +
				"formData=" + formData +
				", url='" + url + '\'' +
				'}';
	}
}
