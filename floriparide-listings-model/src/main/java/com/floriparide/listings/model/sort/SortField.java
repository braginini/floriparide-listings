package com.floriparide.listings.model.sort;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * All sort fields supported with lowercase keys.
 * All keys should be correspondent to database schema fields.
 *
 * @author Mikhail Bragin
 */
public enum SortField {

	NAME("name"), CREATED("created"), UPDATED("updated");

	String key;

	static final Map<String, SortField> map = new HashMap<>();

	static {
		for (SortField s : SortField.values())
			map.put(s.getKey(), s);
	}

	SortField(String id) {
		this.key = id;
	}

	@NotNull
	public String getKey() {
		return key;
	}

	@Nullable
	public static SortField lookup(@NotNull String key) {
		return map.get(key.toLowerCase());
	}
}
