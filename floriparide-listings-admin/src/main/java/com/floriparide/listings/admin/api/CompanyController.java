package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.impl.CreateCompanyRequest;
import com.floriparide.listings.admin.api.request.impl.UpdateCompanyRequest;
import com.floriparide.listings.admin.api.response.impl.CompanyListResponse;
import com.floriparide.listings.admin.api.response.impl.CompanyResponse;
import com.floriparide.listings.model.Company;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;

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

		return new ResponseEntity<Long>(1l, HttpStatus.OK);
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
			HttpServletRequest httpRequest) throws Exception {

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
			HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity<CompanyResponse>(new CompanyResponse(new Company()), HttpStatus.OK);
	}

	/**
	 * Gets a list of companies
	 *
	 * @param start Start index of a company list
	 * @param end End index of a company list
	 * @return an instance of {@link com.floriparide.listings.admin.api.response.impl.CompanyListResponse} wrapped with
	 * {@link org.springframework.http.ResponseEntity} with a HTTP 200 or HTTP 204 status code.
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/list", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<CompanyListResponse> listCompanies(
			@RequestParam(value = "start", required = true) int start,
			@RequestParam(value = "end", required = true) int end,
			HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity<CompanyListResponse>(new CompanyListResponse(0, 1, Arrays.asList(new Company())), HttpStatus.OK);
	}



}
