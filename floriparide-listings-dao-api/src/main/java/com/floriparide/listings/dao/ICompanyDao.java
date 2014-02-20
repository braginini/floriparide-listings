package com.floriparide.listings.dao;

import com.floriparide.listings.model.Company;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public interface ICompanyDao {

	/**
	 * Creates a record of a company {@link com.floriparide.listings.model.Company }in a database
	 *
	 *  @param projectId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @param company {@link com.floriparide.listings.model.Company} to create
	 * @return an id of newly created company
	 * @throws IOException if I/O error occurred
	 */
	public long create(long projectId, @NotNull Company company) throws Exception;

	/**
	 * Deletes a record of a company {@link com.floriparide.listings.model.Company }in a database
	 *
	 * @param projectId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @param companyId an id of the company to delete
	 * @throws IOException if I/O error occurred
	 */
	public void delete(long projectId, long companyId) throws Exception;

	/**
	 * Updates a record of a company {@link com.floriparide.listings.model.Company }in a database
	 *
	 * @param projectId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @param company {@link com.floriparide.listings.model.Company} to update
	 * @throws IOException if I/O error occurred
	 */
	public void update(long projectId, @NotNull Company company) throws Exception;

	/**
	 * Gets a company
	 *
	 * @param projectId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @param companyId the id of the {@link com.floriparide.listings.model.Company} to look for
	 * @return a {@link com.floriparide.listings.model.Company}
	 * @throws IOException if I/O error occurred
	 */
	@Nullable
	public Company get(long projectId, long companyId) throws Exception;

	/**
	 * Gets a part of a list of the companies {@link com.floriparide.listings.model.Company}
	 *
	 * @param projectId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @param offset row offset to return from
	 * @param limit number of rows to return
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
}
