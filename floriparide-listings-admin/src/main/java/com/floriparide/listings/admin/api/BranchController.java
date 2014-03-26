package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.PagingRequest;
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
import java.util.List;

/**
 * Defines API methods to manage company branches {@link com.floriparide.listings.model.Branch}
 *
 * @author Mikhail Bragin
 */
@Controller
@RequestMapping("/admin/v1/branch")
public class BranchController extends SimpleCrudController<BranchElement, Branch, IBranchDao> {

    @Autowired
    public void setDao(IBranchDao dao) {
        super.setDao(dao);
    }

    /**
     * Gets a list of branches
     *
     * @return an instance of {@link com.floriparide.listings.admin.api.response.impl.BranchListResponse} wrapped with
     * {@link org.springframework.http.ResponseEntity} with a HTTP 200 or HTTP 204 status code.
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, consumes = "application/json",
            headers = "Accept=application/json")
    public ResponseEntity<ListResponse<BranchElement>> list(PagingRequest request,
                                                            @RequestParam(value = "company_id", required = true) long companyId,
                                                            HttpServletRequest httpRequest) throws Exception {

        request.validate();

        int totalCount = dao.size(companyId); //todo mb do it other way and in transaction?
        List<Branch> branches;

        if (request.getSortFieldModel() == null) //no sorting specified
            branches = dao.getBranches(companyId, request.getOffset(), request.getLimit());
        else
            branches = dao.getBranches(companyId, request.getOffset(), request.getLimit(),
                    request.getSortFieldModel(), request.getSortTypeModel());

        return new ResponseEntity<>(new ListResponse<>(totalCount,
                BranchElement.branchesToBranchElements(branches)), HttpStatus.OK);
    }

}
