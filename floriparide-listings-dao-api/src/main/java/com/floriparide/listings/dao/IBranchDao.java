package com.floriparide.listings.dao;

import com.floriparide.listings.model.Branch;
import com.floriparide.listings.model.sort.SortField;
import com.floriparide.listings.model.sort.SortType;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Mikhail Bragin
 */
public interface IBranchDao extends IBaseEntityDao<Branch> {

	/**
	 * Gets a part of a list of the companies {@link com.floriparide.listings.model.Company}
	 *
	 * @param companyId the id of the {@link com.floriparide.listings.model.Company} to look in
	 * @param offset    row offset to return from
	 * @param limit     number of rows to return
	 * @return a list of {@link com.floriparide.listings.model.Branch}
	 * @throws java.io.IOException if I/O error occurred
	 */
	@NotNull
	public List<Branch> getBranches(long companyId, int offset, int limit) throws Exception;

	/**
	 * Gets a part of a list of the companies {@link com.floriparide.listings.model.Company}
	 *
	 * @param projectId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @param offset    row offset to return from
	 * @param limit     number of rows to return
	 * @return a list of {@link com.floriparide.listings.model.Branch}
	 * @throws java.io.IOException if I/O error occurred
	 */
	@NotNull
	public List<Branch> getBranchesInProject(long projectId, int offset, int limit) throws Exception;

	/**
	 * Gets a list of all companies {@link com.floriparide.listings.model.Company}
	 *
	 * @param companyId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @return a list of {@link com.floriparide.listings.model.Branch}
	 * @throws java.io.IOException if I/O error occurred
	 */
	@NotNull
	//todo add order
	public List<Branch> getBranches(long companyId) throws Exception;

	/**
	 * Gets company table size (count)
	 *
	 * @param companyId the id of the {@link com.floriparide.listings.model.Company} to look in
	 * @return the size of the list
	 * @throws Exception
	 */
	public int size(long companyId) throws Exception;

	/**
	 * Gets company table size (count)
	 *
	 * @param projectId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @return the size of the list
	 * @throws Exception
	 */
	public int sizeInProject(long projectId) throws Exception;

	/**
	 * Gets a part of a list of the branches {@link com.floriparide.listings.model.Branch}
	 * ordered by specified {@link com.floriparide.listings.model.sort.SortField} and
	 * {@link com.floriparide.listings.model.sort.SortType}
	 *
	 * @param companyId the id of the {@link com.floriparide.listings.model.Company} to look in
	 * @param offset    row offset to return from
	 * @param limit     number of rows to return
	 * @param sortField sort field to sort by {@link com.floriparide.listings.model.sort.SortField}
	 * @param sortType  sort type to sort  {@link com.floriparide.listings.model.sort.SortType}
	 * @return a list of {@link com.floriparide.listings.model.Branch} matching specified criteria
	 * @throws java.io.IOException if I/O error occurred
	 */
	@NotNull
	public List<Branch> getBranches(long companyId, int offset, int limit, @NotNull SortField sortField, @NotNull SortType sortType) throws Exception;

	/**
	 * Gets a part of a list of the branches {@link com.floriparide.listings.model.Branch}
	 * ordered by specified {@link com.floriparide.listings.model.sort.SortField} and
	 * {@link com.floriparide.listings.model.sort.SortType}
	 *
	 * @param projectId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @param offset    row offset to return from
	 * @param limit     number of rows to return
	 * @param sortField sort field to sort by {@link com.floriparide.listings.model.sort.SortField}
	 * @param sortType  sort type to sort  {@link com.floriparide.listings.model.sort.SortType}
	 * @return a list of {@link com.floriparide.listings.model.Branch} matching specified criteria
	 * @throws java.io.IOException if I/O error occurred
	 */
	@NotNull
	public List<Branch> getBranchesInProject(long projectId, int offset, int limit, @NotNull SortField sortField, @NotNull SortType sortType) throws Exception;
}
