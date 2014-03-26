package com.floriparide.listings.admin.api.request;

import com.floriparide.listings.data.Query;
import com.floriparide.listings.model.sort.SortField;
import com.floriparide.listings.model.sort.SortType;
import org.springframework.util.Assert;

/**
 * Basic class for paging requests .
 * <p/>
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


    static int defaultOffset = 0;
    static int defaultLimit = 25;

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

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortField() {
        return sortField;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public SortField getSortFieldModel() {
        if (sortField == null)
            return null;
        return SortField.lookup(sortField);
    }

    public SortType getSortTypeModel() {
        if (sortType == null)
            return null;
        return SortType.lookup(sortType);
    }

    @Override
    public void validate() throws Exception {
//		Assert.notNull(offset, "Field offset must not be null");
//		Assert.notNull(limit, "Field limit must not be null");

        if (offset == null)
            offset = defaultOffset;

        if (limit == null)
            limit = defaultLimit;

        Assert.isTrue(offset >= 0, "Field offset must be non-negative");
        Assert.isTrue(limit >= 0, "Field limit must be non-negative");

        if (sortField != null && sortType == null)
            sortType = SortType.ASC.getKey();

        if (sortField != null)
            Assert.notNull(SortField.lookup(sortField), "SortField not supported " + sortField);

        if (sortType != null)
            Assert.notNull(SortType.lookup(sortType), "SortType not supported " + sortType);
    }

    public Query getQuery() {
        return new Query(offset, limit, getSortFieldModel(), getSortTypeModel());
    }
}
