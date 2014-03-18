package com.floriparide.listings.etl.parser.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.floriparide.listings.model.RawData;

/**
 * @author Mikhail Bragin
 */
public class ArchiveTask {

	RawData.Source source;

	JsonNode node;

	public ArchiveTask(RawData.Source source, JsonNode node) {
		this.source = source;
		this.node = node;
	}

	public RawData.Source getSource() {
		return source;
	}

	public void setSource(RawData.Source source) {
		this.source = source;
	}

	public JsonNode getNode() {
		return node;
	}

	public void setNode(JsonNode node) {
		this.node = node;
	}
}
