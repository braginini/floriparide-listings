package com.floriparide.listings.model.sort;

/**
 * @author Mikhail Bragin
 */
public abstract class SortModel {

	public boolean supportsSortField(SortField sortField) {
		return SortingDictionary.supportsSorting(this.getClass(), sortField);
	}

}
