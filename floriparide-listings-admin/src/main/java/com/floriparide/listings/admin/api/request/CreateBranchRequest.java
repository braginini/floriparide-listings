package com.floriparide.listings.admin.api.request;


import com.floriparide.listings.web.json.BranchElement;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Mikhail Bragin
 */
public class CreateBranchRequest implements IRequest {

	@JsonProperty("")
	BranchElement branch;

	public CreateBranchRequest() {
	}

	public BranchElement getBranch() {
		return branch;
	}

	public void setBranch(BranchElement branch) {
		this.branch = branch;
	}

	@Override
	public void validate() throws Exception {

	}
}
