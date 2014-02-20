package com.floriparide.listings.model.sort;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public enum  SortType {

	ASC("asc"), DESC("desc");

	String key;

	static final Map<String, SortType> map = new HashMap<>();

	static {
		for (SortType st : SortType.values())
			map.put(st.getKey(), st);
	}

	SortType(String id) {
		this.key = id;
	}

	@NotNull
	public String getKey() {
		return key;
	}

	@Nullable
	public static SortType lookup(@NotNull String key) {
		return map.get(key.toLowerCase());
	}
}
