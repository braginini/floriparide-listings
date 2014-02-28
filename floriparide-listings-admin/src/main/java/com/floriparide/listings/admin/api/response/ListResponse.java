package com.floriparide.listings.admin.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.web.json.Element;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class ListResponse<T extends Element> implements IResponse {

	@JsonProperty("")
	Integer totalCount;

	@JsonProperty("")
	List<T> list;

	public ListResponse(Integer totalCount, @NotNull List<T> list) {
		this.totalCount = totalCount;
		this.list = list;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getCurrentCount() { return list.size(); }

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
}
