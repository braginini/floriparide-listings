package com.floriparide.listings.admin.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.Branch;
import com.floriparide.listings.web.json.BranchElement;

/**
 * @author Mikhail Bragin
 */
public class BranchResponse implements IResponse {

	@JsonProperty("")
	BranchElement branch;

	public BranchResponse() {
	}

	public BranchResponse(Branch branch) {
		this.branch = new BranchElement(branch);
	}




}
