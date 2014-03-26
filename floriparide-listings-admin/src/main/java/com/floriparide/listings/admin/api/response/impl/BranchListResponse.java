package com.floriparide.listings.admin.api.response.impl;

import com.floriparide.listings.admin.api.response.ListResponse;
import com.floriparide.listings.web.json.BranchElement;

import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class BranchListResponse extends ListResponse<BranchElement> {

    public BranchListResponse(Integer totalCount, Integer currentCount, List<BranchElement> list) {
        super(totalCount, list);
    }
}
