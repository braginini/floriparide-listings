package com.floriparide.listings.model;

import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public abstract class MultiLangMetaModel extends MetaModel {

	Map<String, String> names;

	protected MultiLangMetaModel() {
	}

	protected MultiLangMetaModel(Map<String, String> names) {
		this.names = names;
	}

	public Map<String, String> getNames() {
		return names;
	}

	public void setNames(Map<String, String> names) {
		this.names = names;
	}
}
