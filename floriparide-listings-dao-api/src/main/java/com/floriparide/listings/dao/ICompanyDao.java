package com.floriparide.listings.dao;

import com.floriparide.listings.model.Company;

import org.jetbrains.annotations.NotNull;

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
	public long create(long projectId, @NotNull Company company) throws IOException;

	/**
	 * Deletes a record of a company {@link com.floriparide.listings.model.Company }in a database
	 *
	 * @param projectId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @param companyId an id of the company to delete
	 * @throws IOException if I/O error occurred
	 */
	public void delete(long projectId, long companyId) throws IOException;

	/**
	 * Updates a record of a company {@link com.floriparide.listings.model.Company }in a database
	 *
	 *  @param projectId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @param company {@link com.floriparide.listings.model.Company} to update
	 * @return an id of newly created company
	 * @throws IOException if I/O error occurred
	 */
	public long update(long projectId, @NotNull Company company) throws IOException;

	/**
	 * Gets a company
	 *
	 * @param projectId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @param companyId the id of the {@link com.floriparide.listings.model.Company} to look for
	 * @return a {@link com.floriparide.listings.model.Company}
	 * @throws IOException if I/O error occurred
	 */
	@NotNull
	public Company get(long projectId, long companyId) throws IOException;

	/**
	 * Gets a part of a list of the companies {@link com.floriparide.listings.model.Company}
	 *
	 * @param projectId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @param start Start index of the list to return (inclusive)
	 * @param end End index of the list to return (exclusive)
	 * @return a list of companies {@link com.floriparide.listings.model.Company}
	 * @throws IOException if I/O error occurred
	 */
	@NotNull
	public List<Company> getCompanies(long projectId, int start, int end) throws IOException;

	/**
	 * Gets a list of all companies {@link com.floriparide.listings.model.Company}
	 *
	 * @param projectId the id of the {@link com.floriparide.listings.model.Project} to look in
	 * @return a list of companies {@link com.floriparide.listings.model.Company}
	 * @throws IOException if I/O error occurred
	 */
	@NotNull
	public List<Company> getCompanies(long projectId) throws IOException;
}
