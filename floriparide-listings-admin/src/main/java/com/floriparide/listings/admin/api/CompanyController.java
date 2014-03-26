package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.PagingRequest;
import com.floriparide.listings.admin.api.response.ListResponse;
import com.floriparide.listings.dao.ICompanyDao;
import com.floriparide.listings.model.Company;
import com.floriparide.listings.web.json.CompanyElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
public class CompanyController extends SimpleCrudController<CompanyElement, Company, ICompanyDao> {

    @Autowired
    public void setDao(ICompanyDao dao) {
        super.setDao(dao);
    }

    /**
     * Gets a list of companies
     *
     * @param request {@link com.floriparide.listings.admin.api.request.PagingRequest} with limit and offset
     * @return an instance of {@link com.floriparide.listings.admin.api.response.ListResponse<CompanyElement>}
     * wrapped with {@link org.springframework.http.ResponseEntity} with a HTTP 200 or HTTP 204 status code.
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, consumes = "application/json",
            headers = "Accept=application/json")
    public ResponseEntity<ListResponse<CompanyElement>> list(PagingRequest request,
                                                             @RequestParam(value = "project_id", required = true) long projectId,
                                                             HttpServletRequest httpRequest) throws Exception {

        request.validate();

        int totalCount = dao.size(projectId); //todo mb do it other way and in transaction?
        List<Company> companies;

        if (request.getSortFieldModel() == null) //no sorting specified
            companies = dao.getCompanies(projectId, request.getOffset(), request.getLimit());
        else
            companies = dao.getCompanies(projectId, request.getOffset(), request.getLimit(),
                    request.getSortFieldModel(), request.getSortTypeModel());

        return new ResponseEntity<>(new ListResponse<>(totalCount,
                CompanyElement.companiesToElements(companies)), HttpStatus.OK);
    }


}
