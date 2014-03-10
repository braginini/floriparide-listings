package com.floriparide.listings.dao;

import com.floriparide.listings.model.Attribute;
import com.floriparide.listings.model.sort.SortField;
import com.floriparide.listings.model.sort.SortType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Mikhail Bragin
 */
public interface IAttributeDao extends IBaseEntityDao<Attribute> {
    /**
     * Gets attributes table size (count)
     *
     * @return the size of the attributes list
     * @throws Exception
     */
    public int size() throws Exception;

    /**
     * Gets a part of a list of the attributes {@link com.floriparide.listings.model.Attribute}
     *
     * @param offset    row offset to return from
     * @param limit     number of rows to return
     * @return a list of attributes {@link com.floriparide.listings.model.Attribute}
     * @throws java.io.IOException if I/O error occurred
     */
    @NotNull
    public List<Attribute> getAttributes(int offset, int limit) throws Exception;

    /**
     * Gets a part of a list of the attributes {@link com.floriparide.listings.model.Attribute}
     * ordered by specified {@link com.floriparide.listings.model.sort.SortField} and
     * {@link com.floriparide.listings.model.sort.SortType}
     *
     * @param offset    row offset to return from
     * @param limit     number of rows to return
     * @param sortField sort field to sort by {@link com.floriparide.listings.model.sort.SortField}
     * @param sortType  sort type to sort  {@link com.floriparide.listings.model.sort.SortType}
     * @return a list of projects {@link com.floriparide.listings.model.Attribute} matching specified criteria
     * @throws java.io.IOException if I/O error occurred
     */
    @NotNull
    public List<Attribute> getAttributes(int offset, int limit, @NotNull SortField sortField, @NotNull SortType sortType) throws Exception;

	public void create(@NotNull List<Attribute> attributes) throws Exception;
}
