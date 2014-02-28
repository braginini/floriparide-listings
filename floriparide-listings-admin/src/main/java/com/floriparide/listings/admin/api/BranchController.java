package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.PagingRequest;
import com.floriparide.listings.admin.api.request.impl.CreateEntityRequest;
import com.floriparide.listings.admin.api.request.impl.UpdateEntityRequest;
import com.floriparide.listings.admin.api.response.ListResponse;
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

	@RequestMapping(method = RequestMethod.POST, value = "/create", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<Long> create(CreateEntityRequest<BranchElement> request,
	                                   HttpServletRequest httpRequest) throws Exception {

		request.validate();
		long id = branchDao.create(request.getEntity().getModel());
		return new ResponseEntity<Long>(id, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/delete", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity delete(@RequestParam(value = "id", required = true) long id,
	                             HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity update(UpdateEntityRequest<BranchElement> request,
	                             HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity(HttpStatus.OK);
	}

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
	 * @return an instance of {@link com.floriparide.listings.admin.api.response.ListResponse<BranchElement>}
     * wrapped with {@link org.springframework.http.ResponseEntity} with a HTTP 200 or HTTP 204 status code.
     *
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/list", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<ListResponse<BranchElement>> list(PagingRequest request,
	                                                        HttpServletRequest httpRequest) throws Exception {

		return new ResponseEntity<ListResponse<BranchElement>>(new ListResponse<BranchElement>(0,
                Arrays.asList(new BranchElement())), HttpStatus.OK);
	}

}
