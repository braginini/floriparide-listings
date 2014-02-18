package com.floriparide.listings.admin.api.response.impl;
import com.floriparide.listings.admin.api.response.EntityResponse;
import com.floriparide.listings.model.Branch;
import com.floriparide.listings.web.json.BranchElement;

/**
 * @author Mikhail Bragin
 */
public class BranchResponse extends EntityResponse<BranchElement> {

	public BranchResponse(Branch branch) {
		super(new BranchElement(branch));
	}
}
