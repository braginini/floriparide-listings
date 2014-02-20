package com.floriparide.listings.dao;

import com.floriparide.listings.model.Company;
import com.floriparide.listings.model.sort.SortField;
import com.floriparide.listings.model.sort.SortType;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public interface ICompanyDao extends IBaseEntityDao<Company> {

	/**
	 * Gets a part of a list of the companies {@link com.floriparide.listings.model.Company}
	 *
	 * @param projectId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @param offset    row offset to return from
	 * @param limit     number of rows to return
	 * @return a list of companies {@link com.floriparide.listings.model.Company}
	 * @throws IOException if I/O error occurred
	 */
	@NotNull
	public List<Company> getCompanies(long projectId, int offset, int limit) throws Exception;

	/**
	 * Gets a list of all companies {@link com.floriparide.listings.model.Company}
	 *
	 * @param projectId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @return a list of companies {@link com.floriparide.listings.model.Company}
	 * @throws IOException if I/O error occurred
	 */
	@NotNull
	//todo add order
	public List<Company> getCompanies(long projectId) throws Exception;

	/**
	 * Gets company table size (count)
	 *
	 * @param projectId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @return the size of the companies list
	 * @throws Exception
	 */
	public int size(long projectId) throws Exception;

	/**
	 * Gets a part of a list of the companies {@link com.floriparide.listings.model.Company}
	 * ordered by specified {@link com.floriparide.listings.model.sort.SortField} and
	 * {@link com.floriparide.listings.model.sort.SortType}
	 *
	 * @param projectId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @param offset    row offset to return from
	 * @param limit     number of rows to return
	 * @param sortField sort field to sort by {@link com.floriparide.listings.model.sort.SortField}
	 * @param sortType  sort type to sort  {@link com.floriparide.listings.model.sort.SortType}
	 * @return a list of companies {@link com.floriparide.listings.model.Company} matching specified criteria
	 * @throws IOException if I/O error occurred
	 */
	@NotNull
	public List<Company> getCompanies(long projectId, int offset, int limit, @NotNull SortField sortField, @NotNull SortType sortType) throws Exception;
}
