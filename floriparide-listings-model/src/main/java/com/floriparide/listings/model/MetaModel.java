package com.floriparide.listings.model;

/**
 * @author Mikhail Bragin
 */
public abstract class MetaModel {

	Long created;

	Long updated;

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public Long getUpdated() {
		return updated;
	}

	public void setUpdated(Long updated) {
		this.updated = updated;
	}
}
