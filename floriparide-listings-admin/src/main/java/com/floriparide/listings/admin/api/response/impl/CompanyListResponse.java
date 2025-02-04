package com.floriparide.listings.admin.api.response.impl;

import com.floriparide.listings.admin.api.response.ListResponse;
import com.floriparide.listings.web.json.CompanyElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class CompanyListResponse extends ListResponse<CompanyElement> {

    public CompanyListResponse(Integer totalCount, Integer currentCount, @NotNull List<CompanyElement> companies) {
        super(totalCount, companies);
    }
}
