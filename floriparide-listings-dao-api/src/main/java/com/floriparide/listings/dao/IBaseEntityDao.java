package com.floriparide.listings.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Mikhail Bragin
 */
public interface IBaseEntityDao<E> {

	/**
	 * Creates a record of a company {@link com.floriparide.listings.model.Company }in a database
	 *
	 * @param entity entity to create
	 * @return an id of newly created entity
	 * @throws java.io.IOException if I/O error occurred
	 */
	public long create(@NotNull E entity) throws Exception;

	/**
	 * Deletes a record of a company {@link com.floriparide.listings.model.Company }in a database
	 *
	 * @param entityId the id of the entity to look in
	 * @throws java.io.IOException if I/O error occurred
	 */
	public void delete(long entityId) throws Exception;

	/**
	 * Updates a record of a company {@link com.floriparide.listings.model.Company }in a database
	 *
	 * @param entity the entity to update
	 * @throws java.io.IOException if I/O error occurred
	 */
	public void update(@NotNull E entity) throws Exception;

	/**
	 * Gets a company
	 *
	 * @param entityId the entity id to get
	 * @return an entity
	 * @throws java.io.IOException if I/O error occurred
	 */
	@Nullable
	public E get(long entityId) throws Exception;
}
