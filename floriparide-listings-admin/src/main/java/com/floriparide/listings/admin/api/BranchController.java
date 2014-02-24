package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.PagingRequest;
import com.floriparide.listings.admin.api.request.f.CreateEntityRequestCommon;
import com.floriparide.listings.admin.api.request.f.UpdateEntityRequestCommon;
import com.floriparide.listings.admin.api.response.ListResponse;
import com.floriparide.listings.admin.api.response.impl.BranchListResponse;
import com.floriparide.listings.dao.IBranchDao;
import com.floriparide.listings.model.Branch;
import com.floriparide.listings.web.json.BranchElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;

/**
 * Defines API methods to manage company branches {@link com.floriparide.listings.model.Branch}
 *
 * @author Mikhail Bragin
 */
@Controller
@RequestMapping("/admin/v1/branch")
public class BranchController extends BaseController implements CRUDController<BranchElement> {

	@Autowired
	IBranchDao branchDao;

	/**
	 * Creates a branch
	 *
	 * @param request     {@link com.floriparide.listings.admin.api.request.impl.CreateBranchRequest} branch data to create
	 * @param httpRequest The raw httpRequest type of {@link javax.servlet.http.HttpServletRequest} for advanced usage
	 *                    (headers, statuses, etc)
	 * @return an instance of {@link org.springframework.http.ResponseEntity} with an id of newly created organization
	 * with a HTTP 200 or HTTP 204 status code
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/create", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<Long> create(CreateEntityRequestCommon<BranchElement> request,
	                                   HttpServletRequest httpRequest) throws Exception {

		request.validate();
		long id = branchDao.create(request.getEntity().getModel());
		return new ResponseEntity<Long>(id, HttpStatus.OK);
	}

	/**
	 * Deletes a specific branch
	 *
	 * @param id The id of the branch to delete
	 * @return an instance of empty {@link org.springframework.http.ResponseEntity}
	 * with a HTTP 200 or HTTP 204 status code. In case if resource was not found, a HTTP 404 status should be returned along with custom
	 * error response. //todo create ExceptionResponse
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity delete(@RequestParam(value = "id", required = true) long id,
	                             HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Updates a specific branch
	 *
	 * @param request     {@link com.floriparide.listings.admin.api.request.impl.UpdateBranchRequest} branch data to update
	 * @param httpRequest The raw httpRequest type of {@link javax.servlet.http.HttpServletRequest} for advanced usage
	 *                    (headers, statuses, etc)
	 * @return an instance of empty {@link org.springframework.http.ResponseEntity}
	 * with a HTTP 200 or HTTP 204 status code. In case if resource was not found, a HTTP 404 status should be returned along with custom
	 * error response. //todo create ExceptionResponse
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/update", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity update(UpdateEntityRequestCommon<BranchElement> request,
	                             HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Gets a specific branch
	 *
	 * @param id The id of the branch to get
	 * @return an instance of {@link com.floriparide.listings.admin.api.response.impl.BranchResponse} wrapped with
	 * {@link org.springframework.http.ResponseEntity} with a HTTP 200 or HTTP 204 status code.
	 * In case of resource was not found, HTTP 404 status code
	 * should be returned along with custom error response. //todo create ExceptionResponse
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/get", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<BranchElement> get(
			@RequestParam(value = "id", required = true) long id,
			HttpServletRequest httpRequest) throws Exception {

		Branch branch = branchDao.get(id);

		if (branch == null)
			return new ResponseEntity<BranchElement>(HttpStatus.NOT_FOUND);

		return new ResponseEntity<BranchElement>(new BranchElement(branch), HttpStatus.OK);
	}

	/**
	 * Gets a list of branches
	 *
	 * @param start Start index of a branch list (inclusive)
	 * @param end   End index of a branch list (exclusive)
	 * @return an instance of {@link com.floriparide.listings.admin.api.response.impl.BranchListResponse} wrapped with
	 * {@link org.springframework.http.ResponseEntity} with a HTTP 200 or HTTP 204 status code.
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/list", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<ListResponse<BranchElement>> list(PagingRequest request,
	                                                        HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity<ListResponse<BranchElement>>(new BranchListResponse(0, 1, Arrays.asList(new BranchElement())), HttpStatus.OK);
	}

}
