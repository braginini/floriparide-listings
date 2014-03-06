package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.PagingRequest;
import com.floriparide.listings.admin.api.request.impl.CreateEntityRequest;
import com.floriparide.listings.admin.api.request.impl.UpdateEntityRequest;
import com.floriparide.listings.admin.api.response.ListResponse;
import com.floriparide.listings.admin.api.response.impl.BranchListResponse;
import com.floriparide.listings.dao.IBranchDao;
import com.floriparide.listings.model.Branch;
import com.floriparide.listings.web.json.BranchElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;

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
	public ResponseEntity<Long> create(@RequestBody CreateEntityRequest<BranchElement> request,
	                                   HttpServletRequest httpRequest) throws Exception {

		request.validate();
		long id = branchDao.create(request.getEntity().getModel());
		return new ResponseEntity<>(id, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/delete", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity delete(@RequestParam(value = "id", required = true) long id,
	                             HttpServletRequest httpRequest) throws Exception {

		branchDao.delete(id);

		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity update(@RequestBody UpdateEntityRequest<BranchElement> request,
	                             HttpServletRequest httpRequest) throws Exception {

		request.validate();

		Branch branch = request.getEntity().getModel();

		branchDao.update(branch);

		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/get", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<BranchElement> get(@RequestParam(value = "id", required = true) long id,
	                                         HttpServletRequest httpRequest) throws Exception {

		Branch branch = branchDao.get(id);

		if (branch == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		return new ResponseEntity<>(new BranchElement(branch), HttpStatus.OK);
	}

	/**
	 * Gets a list of branches
	 *
	 * @return an instance of {@link com.floriparide.listings.admin.api.response.impl.BranchListResponse} wrapped with
	 * {@link org.springframework.http.ResponseEntity} with a HTTP 200 or HTTP 204 status code.
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/list", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<ListResponse<BranchElement>> list(PagingRequest request,
	                                                        @RequestParam(value = "company_id", required = true) long companyId,
	                                                        HttpServletRequest httpRequest) throws Exception {

		request.validate();

		int totalCount = branchDao.size(companyId); //todo mb do it other way and in transaction?
		List<Branch> branches;

		if (request.getSortFieldModel() == null) //no sorting specified
			branches = branchDao.getBranches(companyId);
		else
		branches = branchDao.getBranches(companyId, request.getOffset(), request.getLimit(),
				request.getSortFieldModel(), request.getSortTypeModel());

		return new ResponseEntity<ListResponse<BranchElement>>(new ListResponse<BranchElement>(0,
				BranchElement.branchesToBranchElements(branches)), HttpStatus.OK);
	}

}
