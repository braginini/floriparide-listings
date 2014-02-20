package com.floriparide.listings.admin.api.request;

import org.springframework.util.Assert;

/**
 * Basic class for paging requests .
 *
 * {@link this#start} is inclusive
 * {@link this#end} is exclusive
 *
 * @author Mikhail Bragin
 */
public class PagingRequest implements IRequest {

	Integer start;

	Integer end;

	String sortField;

	String type;  //asc/desc

	public PagingRequest() {
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void validate() throws Exception {
		Assert.notNull(start, "Field start must not be null");
		Assert.notNull(end, "Field end must not be null");
		Assert.isTrue(end > start, "Field end must be bigger than start");
		Assert.isTrue(start >= 0, "Field start must be non-negative");
		Assert.isTrue(end >= 0, "Field end must be non-negative");
	}
}
