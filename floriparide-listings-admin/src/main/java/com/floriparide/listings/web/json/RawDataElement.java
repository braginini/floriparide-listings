package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.RawData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class RawDataElement extends Element<RawData> {

	@JsonProperty("")
	String data;

	@JsonProperty("")
	String source;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@JsonIgnore
	@Override
	public RawData getModel() {
		return new RawData(id, data, RawData.Source.lookup(source));
	}

	public static List<RawData> getRawDataModelsFromRawDataElements(List<RawDataElement> rawDataElements) {
		if (rawDataElements == null || rawDataElements.isEmpty())
			return Collections.emptyList();

		ArrayList<RawData> rawDataModels = new ArrayList<>(rawDataElements.size());

		for (RawDataElement e : rawDataElements)
			rawDataModels.add(e.getModel());

		return rawDataModels;
	}
}
