package com.floriparide.listings.dao;

import com.floriparide.listings.data.Filter;
import com.floriparide.listings.data.Query;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Mikhail Bragin
 */
public interface IBaseEntityDao<E> {

    /**
     * Creates a record of an entity in a database
     *
     * @param entity entity to create
     * @return an id of newly created entity
     * @throws java.io.IOException if I/O error occurred
     */
    public long create(@NotNull E entity) throws Exception;

    /**
     * Deletes an entity in a database
     *
     * @param entityId the id of the entity to look in
     * @throws java.io.IOException if I/O error occurred
     */
    public void delete(long entityId) throws Exception;

    /**
     * Deletes set of entities in a database
     *
     * @param entityId the id of the entity to look in
     * @throws java.io.IOException if I/O error occurred
     */
    public void delete(Long[] entityId) throws Exception;

    /**
     * Updates an entity in a database
     *
     * @param entity the entity to update
     * @throws java.io.IOException if I/O error occurred
     */
    public void update(@NotNull E entity) throws Exception;

    /**
     * Updates an entity in a database
     *
     * @param entity   the entity to update
     * @param entityId id of the entity to update
     * @throws Exception
     */
    public void update(@NotNull E entity, long entityId) throws Exception;

    /**
     * Gets an entity
     *
     * @param entityId the entity id to get
     * @return an entity
     * @throws java.io.IOException if I/O error occurred
     */
    @Nullable
    public E get(long entityId) throws Exception;

    /**
     * Get list of all entities
     *
     * @return list of entities
     */
    public List<E> getList();

    /**
     * Get list of all entities specified by Query {@link com.floriparide.listings.data.Query }
     *
     * @param query specification for getting entities
     * @return list of finding entities
     */
    public List<E> getList(@NotNull Query query);

    /**
     * @return Total amount of entities
     * @throws Exception
     */
    public int size() throws Exception;

    /**
     * @param filters Filter entities
     * @return Amount of entities appropriated conditions
     * @throws Exception
     */
    public int size(List<Filter> filters) throws Exception;
}