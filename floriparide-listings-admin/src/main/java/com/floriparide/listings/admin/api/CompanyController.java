package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.PagingRequest;
import com.floriparide.listings.admin.api.request.impl.CreateCompanyRequest;
import com.floriparide.listings.admin.api.request.impl.UpdateCompanyRequest;
import com.floriparide.listings.admin.api.response.impl.CompanyListResponse;
import com.floriparide.listings.admin.api.response.impl.CompanyResponse;
import com.floriparide.listings.dao.ICompanyDao;
import com.floriparide.listings.model.Company;
import com.floriparide.listings.web.json.CompanyElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * Controller that defines all operations with organizations and branches.
 *
 * All the methods should throw an exception that are handled and converted to proper JSON response in
 * {@link com.floriparide.listings.admin.api.BaseController#handleException(javax.servlet.http.HttpServletResponse, Exception)}
 *
 * @author Mikhail Bragin
 */
@Controller
@RequestMapping("/admin/v1/company")
public class CompanyController extends BaseController {

	@Autowired
	ICompanyDao companyDao;

	/**
	 * Creates a company
	 *
	 * @param request     {@link com.floriparide.listings.admin.api.request.impl.CreateCompanyRequest} request object
	 * @param httpRequest The raw httpRequest type of {@link javax.servlet.http.HttpServletRequest} for advanced usage
	 *                    (headers, statuses, etc)
	 * @return an instance of {@link org.springframework.http.ResponseEntity} with an id of newly created organization
	 * with a HTTP 200 or HTTP 204 status code. In case of resource (company) already exists a HTTP 409 Conflict status should be returned along with custom
	 * error response. //todo create ExceptionResponse
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/create", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<Long> createCompany(@RequestBody CreateCompanyRequest request,
	                                          HttpServletRequest httpRequest) throws Exception {

		request.validate();
		CompanyElement companyElement = request.getEntity();
		long id = companyDao.create(companyElement.getProjectId(), companyElement.getModel());

		return new ResponseEntity<Long>(id, HttpStatus.OK);
	}

	/**
	 * Deletes a specific company
	 *
	 * @param id The id of the company to delete
	 * @return an instance of empty {@link org.springframework.http.ResponseEntity}
	 * with a HTTP 200 or HTTP 204 status code. In case if resource (company) was not found HTTP 404 should be returned along with custom
	 * error response. //todo create ExceptionResponse
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity deleteCompany(
			@RequestParam(value = "id", required = true) long id,
			@RequestParam(value = "project_id", required = true) long projectId,
			HttpServletRequest httpRequest) throws Exception {

		companyDao.delete(projectId, id);

		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Updates a specific company
	 *
	 * @param request {@link com.floriparide.listings.admin.api.request.impl.UpdateCompanyRequest} company data to update
	 * @return an instance of empty {@link org.springframework.http.ResponseEntity}
	 * with a HTTP 200 or HTTP 204 status code. In case if resource (company) was not found HTTP 404 should be returned along with custom
	 * error response. //todo create ExceptionResponse
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/update", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity updateCompany(
			UpdateCompanyRequest request,
			HttpServletRequest httpRequest) throws Exception {

		Company company = request.getEntity().getModel();
		companyDao.update(company.getProjectId(), company);

		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Gets a specific company
	 *
	 * @param id The id of the company to get
	 * @return an instance of {@link com.floriparide.listings.admin.api.response.impl.CompanyResponse} wrapped with
	 * {@link org.springframework.http.ResponseEntity} with a HTTP 200 or HTTP 204 status code. In case if resource (company) was not found, HTTP 404 status code
	 * should be returned along with custom error response. //todo create ExceptionResponse
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/get", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<CompanyResponse> getCompany(
			@RequestParam(value = "id", required = true) long id,
			@RequestParam(value = "project_id", required = true) long projectId,
			HttpServletRequest httpRequest) throws Exception {

		Company company = companyDao.get(projectId, id);

		if (company == null)
			return new ResponseEntity<CompanyResponse>(HttpStatus.NOT_FOUND);

		return new ResponseEntity<CompanyResponse>(new CompanyResponse(company), HttpStatus.OK);
	}

	/**
	 * Gets a list of companies
	 *
	 * @param request {@link com.floriparide.listings.admin.api.request.PagingRequest} with start and end indexes
	 * @return an instance of {@link com.floriparide.listings.admin.api.response.impl.CompanyListResponse} wrapped with
	 * {@link org.springframework.http.ResponseEntity} with a HTTP 200 or HTTP 204 status code.
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/list", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<CompanyListResponse> listCompanies(
			PagingRequest request,
			@RequestParam(value = "project_id", required = true) long projectId,
			HttpServletRequest httpRequest) throws Exception {

		request.validate();

		int totalCount = companyDao.size(projectId); //todo mb do it other way and in transaction?
		List<Company> companies = companyDao.getCompanies(projectId, request.getOffset(), request.getLimit());

		return new ResponseEntity<CompanyListResponse>(new CompanyListResponse(totalCount, companies.size(), companies), HttpStatus.OK);
	}



}
