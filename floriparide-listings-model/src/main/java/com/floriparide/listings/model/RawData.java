package com.floriparide.listings.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class RawData extends Model {

	Long id;

	String data;

	Source source;

	public RawData(Long id, String data, Source source) {
		this.id = id;
		this.data = data;
		this.source = source;
	}

	public RawData(String data, Source source) {
		this.data = data;
		this.source = source;
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

	public Source getSource() {
		return source;
	}

	public enum Source {
		ABRSEl("abrasel"), FOURSQUARE("4square"), HAGAH("hagah");

		String source;

		Source(String source) {
			this.source = source;
		}

		public String getSource() {
			return source;
		}

		static Map<String, Source> map = new HashMap<>();

		static {
			for (Source s : Source.values())
				map.put(s.getSource(), s);
		}

		public static Source lookup(String source) {
			return (source != null) ? map.get(source.toLowerCase()) : null;
		}
	}
}
