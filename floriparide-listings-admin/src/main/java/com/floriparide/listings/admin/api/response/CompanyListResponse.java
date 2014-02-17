package com.floriparide.listings.admin.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.Company;
import com.floriparide.listings.web.json.CompanyElement;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class CompanyListResponse implements IResponse {

	@JsonProperty("")
	Integer totalCount;

	@JsonProperty("")
	Integer currentCount;

	@JsonProperty("")
	List<CompanyElement> companies;

	public CompanyListResponse() {
	}

	public CompanyListResponse(Integer totalCount, Integer currentCount, @NotNull List<Company> companies) {
		this.totalCount = totalCount;
		this.currentCount = currentCount;
		this.companies = new ArrayList<CompanyElement>(companies.size());

		for (Company c : companies)
			this.companies.add(new CompanyElement(c));

	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getCurrentCount() {
		return currentCount;
	}

	public void setCurrentCount(Integer currentCount) {
		this.currentCount = currentCount;
	}

	public List<CompanyElement> getCompanies() {
		return companies;
	}

	public void setCompanies(List<CompanyElement> companies) {
		this.companies = companies;
	}
}
