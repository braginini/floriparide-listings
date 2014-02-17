package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.CreateBranchRequest;
import com.floriparide.listings.admin.api.request.CreateCompanyRequest;
import com.floriparide.listings.admin.api.request.UpdateBranchRequest;
import com.floriparide.listings.admin.api.request.UpdateCompanyRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller that defines all operations with organizations and branches.
 * All API methods should return result wrapped by {@link org.springframework.http.ResponseEntity} object which makes easier to
 * manage {@link org.springframework.http.HttpStatus} status codes and {@link org.springframework.http.HttpHeaders} headers.
 * <p/>
 * Since two entities ({@link com.floriparide.listings.model.Company} and {@link com.floriparide.listings.model.Branch})
 * are present in this controller here we use action+Entity as a request mapping, e.g. {@link this#createCompany(String, String, javax.servlet.http.HttpServletRequest)}
 * where 'create' is an action and 'Company' is an entity.
 * <p/>
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
	 * @param request     {@link com.floriparide.listings.admin.api.request.CreateCompanyRequest} request object
	 * @param httpRequest The raw httpRequest type of {@link javax.servlet.http.HttpServletRequest} for advanced usage
	 *                    (headers, statuses, etc)
	 * @return an instance of {@link org.springframework.http.ResponseEntity} with an id of newly created organization
	 * with a HTTP 200 or HTTP 204 status code. In case of resource (company) already exists a HTTP 409 Conflict status should be returned along with custom
	 * error response. //todo create ExceptionResponse
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/createCompany/")
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
	@RequestMapping(method = RequestMethod.DELETE, value = "/deleteCompany/")
	public ResponseEntity deleteCompany(
			@RequestParam(value = "id", required = true) long id,
			HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Updates a specific company
	 *
	 * @param request {@link com.floriparide.listings.admin.api.request.UpdateCompanyRequest} company data to update
	 * @return an instance of empty {@link org.springframework.http.ResponseEntity}
	 * with a HTTP 200 or HTTP 204 status code. In case if resource (company) was not found HTTP 404 should be returned along with custom
	 * error response. //todo create ExceptionResponse
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/updateCompany/")
	public ResponseEntity updateCompany(
			UpdateCompanyRequest request,
			HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Creates a branch
	 *
	 * @param request     {@link com.floriparide.listings.admin.api.request.CreateBranchRequest} branch data to create
	 * @param httpRequest The raw httpRequest type of {@link javax.servlet.http.HttpServletRequest} for advanced usage
	 *                    (headers, statuses, etc)
	 * @return an instance of {@link org.springframework.http.ResponseEntity} with an id of newly created organization
	 * with a HTTP 200 or HTTP 204 status code
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/createBranch/")
	public ResponseEntity<Long> createBranch(CreateBranchRequest request,
	                                         HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity<Long>(1l, HttpStatus.OK);
	}

	/**
	 * Deletes a specific branch
	 *
	 * @param id The id of the branch to delete
	 * @return an instance of empty {@link org.springframework.http.ResponseEntity}
	 * with a HTTP 200 or HTTP 204 status code. In case if resource (company) was not found, a HTTP 404 status should be returned along with custom
	 * error response. //todo create ExceptionResponse
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/deleteBranch/")
	public ResponseEntity deleteBranch(
			@RequestParam(value = "id", required = true) long id,
			HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Updates a specific branch
	 *
	 * @param request     {@link com.floriparide.listings.admin.api.request.UpdateBranchRequest} branch data to update
	 * @param httpRequest The raw httpRequest type of {@link javax.servlet.http.HttpServletRequest} for advanced usage
	 *                    (headers, statuses, etc)
	 * @return an instance of empty {@link org.springframework.http.ResponseEntity}
	 * with a HTTP 200 or HTTP 204 status code. In case if resource was not found, a HTTP 404 status should be returned along with custom
	 * error response. //todo create ExceptionResponse
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/updateBranch/")
	public ResponseEntity updateBranch(UpdateBranchRequest request,
	                                         HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity(HttpStatus.OK);
	}


}
