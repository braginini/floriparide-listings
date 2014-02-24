package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.PagingRequest;
import com.floriparide.listings.admin.api.request.impl.CreateEntityRequest;
import com.floriparide.listings.admin.api.request.impl.UpdateEntityRequest;
import com.floriparide.listings.admin.api.response.impl.CompanyListResponse;
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

import javax.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * Controller that defines all operations with organizations and branches.
 * <p/>
 * All the methods should throw an exception that are handled and converted to proper JSON response in
 * {@link com.floriparide.listings.admin.api.BaseController#handleException(javax.servlet.http.HttpServletResponse, Exception)}
 *
 * @author Mikhail Bragin
 */
@Controller
@RequestMapping("/admin/v1/company")
public class CompanyController extends BaseController implements CRUDController<CompanyElement> {

	@Autowired
	ICompanyDao companyDao;

	@RequestMapping(method = RequestMethod.POST, value = "/create", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<Long> create(@RequestBody CreateEntityRequest<CompanyElement> request,
	                                   HttpServletRequest httpRequest) throws Exception {

		request.validate();

		CompanyElement companyElement = request.getEntity();
		long id = companyDao.create(companyElement.getModel());

		return new ResponseEntity<Long>(id, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/delete", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity delete(@RequestParam(value = "id", required = true) long id,
	                             HttpServletRequest httpRequest) throws Exception {

		companyDao.delete(id);

		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity update(@RequestBody UpdateEntityRequest<CompanyElement> request,
	                             HttpServletRequest httpRequest) throws Exception {

		request.validate();

		Company company = request.getEntity().getModel();
		companyDao.update(company);

		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/get", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<CompanyElement> get(@RequestParam(value = "id", required = true) long id,
	                                           HttpServletRequest httpRequest) throws Exception {

		Company company = companyDao.get(id);

		if (company == null)
			return new ResponseEntity<CompanyElement>(HttpStatus.NOT_FOUND);

		return new ResponseEntity<CompanyElement>(new CompanyElement(company), HttpStatus.OK);
	}

	/**
	 * Gets a list of companies
	 *
	 * @param request {@link com.floriparide.listings.admin.api.request.PagingRequest} with limit and offset
	 * @return an instance of {@link com.floriparide.listings.admin.api.response.impl.CompanyListResponse} wrapped with
	 * {@link org.springframework.http.ResponseEntity} with a HTTP 200 or HTTP 204 status code.
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/list", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<CompanyListResponse> list(PagingRequest request,
	                                                @RequestParam(value = "project_id", required = true) long projectId,
	                                                HttpServletRequest httpRequest) throws Exception {

		request.validate();

		int totalCount = companyDao.size(projectId); //todo mb do it other way and in transaction?
		List<Company> companies;

		if (request.getSortFieldModel() == null) //no sorting specified
			companies = companyDao.getCompanies(projectId, request.getOffset(), request.getLimit());
		else
			companies = companyDao.getCompanies(projectId, request.getOffset(), request.getLimit(),
					request.getSortFieldModel(), request.getSortTypeModel());

		return new ResponseEntity<CompanyListResponse>(new CompanyListResponse(totalCount, companies.size(),
				CompanyElement.companiesToElements(companies)), HttpStatus.OK);
	}


}
