package com.floriparide.listings.admin.api.request;

import org.springframework.util.Assert;

/**
 * Basic class for paging requests .
 *
 * {@link this#offset} row to start from
 * {@link this#limit} how many rows to return
 *
 * @author Mikhail Bragin
 */
public class PagingRequest implements IRequest {

	Integer offset;

	Integer limit;

	String sortField;

	String sortType;  //asc/desc

	public PagingRequest() {
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	@Override
	public void validate() throws Exception {
		Assert.notNull(offset, "Field start must not be null");
		Assert.notNull(limit, "Field end must not be null");
		Assert.isTrue(offset >= 0, "Field start must be non-negative");
		Assert.isTrue(limit >= 0, "Field end must be non-negative");
	}
}
