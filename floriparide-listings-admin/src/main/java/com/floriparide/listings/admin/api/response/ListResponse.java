package com.floriparide.listings.admin.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Mikhail Bragin
 */
public abstract class ListResponse<T> implements IResponse {

	@JsonProperty("")
	Integer totalCount;

	@JsonProperty("")
	Integer currentCount;

	@JsonProperty("")
	List<T> list;

	protected ListResponse(Integer totalCount, Integer currentCount, List<T> list) {
		this.totalCount = totalCount;
		this.currentCount = currentCount;
		this.list = list;
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

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
}
