package com.floriparide.listings.model.sort;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mikhail Bragin
 */
public class SortingDictionary {

	private static Multimap<Class, SortField> map = HashMultimap.create();

	private static final Logger log = LoggerFactory.getLogger(SortingDictionary.class);

	public static void registerSortFields(Class clazz, SortField... sortFields) {

		if (map.containsKey(clazz)) {
			log.error("Sort fields already registered for Model " + clazz);
			return;
		}

		for (SortField sortField : sortFields)
			map.put(clazz, sortField);

	}

	public static boolean supportsSorting(Class clazz, SortField sortField) {
		return map.containsEntry(clazz, sortField);
	}
}
