package com.floriparide.listings.admin.api.response.impl;

import com.floriparide.listings.admin.api.response.ListResponse;
import com.floriparide.listings.model.Rubric;
import com.floriparide.listings.web.json.RubricElement;

import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class RubricListResponse extends ListResponse<RubricElement> {

	public RubricListResponse(Integer totalCount, Integer currentCount, List<RubricElement> list) {
		super(totalCount, list);
	}
}
