package com.floriparide.listings.admin.api.response.impl;

import com.floriparide.listings.admin.api.response.ListResponse;
import com.floriparide.listings.model.Branch;

import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class BranchListResponse extends ListResponse<Branch> {

	public BranchListResponse(Integer totalCount, Integer currentCount, List<Branch> list) {
		super(totalCount, currentCount, list);
	}
}
