package com.floriparide.listings.data;

import com.floriparide.listings.model.sort.SortField;
import com.floriparide.listings.model.sort.SortType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Andrei Tupitcyn
 */
public class Query {

    protected Integer offset;

    protected Integer limit;

    protected SortField sortField;

    protected SortType sortType;

    protected List<Filter> filters = new ArrayList<>();

    public Query() {

    }

    public Query(Integer offset, Integer limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public Query(SortField sortField, SortType sortType) {
        this.sortField = sortField;
        this.sortType = sortType;
    }

    public Query(Integer offset, Integer limit, SortField sortField, SortType sortType) {
        this.offset = offset;
        this.limit = limit;
        this.sortField = sortField;
        this.sortType = sortType;
    }


    public Integer getOffset() {
        return offset;
    }

    public Query setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public Query setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public SortField getSortField() {
        return sortField;
    }

    public Query setSortField(SortField sortField) {
        this.sortField = sortField;
        return this;
    }

    public SortType getSortType() {
        return sortType;
    }

    public Query setSortType(SortType sortType) {
        this.sortType = sortType;
        return this;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public Query setFilters(List<Filter> filters) {
        this.filters = filters;
        return this;
    }

    public Query filter(String filter) {
        this.filters.add(new Filter(filter));
        return this;
    }

    public Query filter(String filter, Object... params) {
        this.filters.add(new Filter(filter, Arrays.asList(params)));
        return this;
    }

    public Query filterByField(String field, Object value) {
        Filter f = new Filter("\"" + field + "\" = ?");
        f.getBinds().add(value);
        this.filters.add(f);
        return this;
    }
}
