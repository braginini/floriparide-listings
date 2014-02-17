package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.CreateRubricRequest;
import com.floriparide.listings.admin.api.request.UpdateRubricRequest;
import com.floriparide.listings.admin.api.response.BranchListResponse;
import com.floriparide.listings.admin.api.response.BranchResponse;
import com.floriparide.listings.admin.api.response.RubricListResponse;
import com.floriparide.listings.admin.api.response.RubricResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Mikhail Bragin
 */
@Controller
@RequestMapping("/admin/v1/rubric")
public class RubricController extends BaseController {

	/**
	 * Creates a rubric
	 *
	 * @param request     {@link com.floriparide.listings.admin.api.request.CreateRubricRequest} rubric data to create
	 * @param httpRequest The raw httpRequest type of {@link javax.servlet.http.HttpServletRequest} for advanced usage
	 *                    (headers, statuses, etc)
	 * @return an instance of {@link org.springframework.http.ResponseEntity} with an id of newly created rubric
	 * with a HTTP 200 or HTTP 204 status code
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/create")
	public ResponseEntity<Long> createRubric(CreateRubricRequest request,
	                                         HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity<Long>(1l, HttpStatus.OK);
	}

	/**
	 * Deletes a specific rubric
	 *
	 * @param id The id of the rubric to delete
	 * @return an instance of empty {@link org.springframework.http.ResponseEntity}
	 * with a HTTP 200 or HTTP 204 status code. In case if resource was not found, a HTTP 404 status should be returned along with custom
	 * error response. //todo create ExceptionResponse
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete")
	public ResponseEntity deleteRubric(
			@RequestParam(value = "id", required = true) long id,
			HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Updates a specific rubric
	 *
	 * @param request     {@link com.floriparide.listings.admin.api.request.UpdateRubricRequest} rubric data to update
	 * @param httpRequest The raw httpRequest type of {@link javax.servlet.http.HttpServletRequest} for advanced usage
	 *                    (headers, statuses, etc)
	 * @return an instance of empty {@link org.springframework.http.ResponseEntity}
	 * with a HTTP 200 or HTTP 204 status code. In case if resource was not found, a HTTP 404 status should be returned along with custom
	 * error response. //todo create ExceptionResponse
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/update")
	public ResponseEntity updateRubric(UpdateRubricRequest request,
	                                   HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Gets a specific rubric
	 *
	 * @param id The id of the branch to get
	 * @return an instance of {@link com.floriparide.listings.admin.api.response.RubricResponse} wrapped with
	 * {@link org.springframework.http.ResponseEntity} with a HTTP 200 or HTTP 204 status code.
	 * In case of resource was not found, HTTP 404 status code
	 * should be returned along with custom error response. //todo create ExceptionResponse
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/get")
	public ResponseEntity<RubricResponse> getRubric(
			@RequestParam(value = "id", required = true) long id,
			HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity<RubricResponse>(new RubricResponse(), HttpStatus.OK);
	}

	/**
	 * Gets a list of rubrics
	 *
	 * @param start Start index of a branch list
	 * @param end End index of a branch list
	 * @return an instance of {@link com.floriparide.listings.admin.api.response.RubricListResponse} wrapped with
	 * {@link org.springframework.http.ResponseEntity} with a HTTP 200 or HTTP 204 status code.
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/list")
	public ResponseEntity<RubricListResponse> listRubrics(
			@RequestParam(value = "start", required = true) int start,
			@RequestParam(value = "end", required = true) int end,
			HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity<RubricListResponse>(new  RubricListResponse(), HttpStatus.OK);
	}
}
