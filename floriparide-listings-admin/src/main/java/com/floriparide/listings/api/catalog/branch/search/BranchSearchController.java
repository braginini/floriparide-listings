package com.floriparide.listings.api.catalog.branch.search;

import com.floriparide.listings.BaseController;
import com.floriparide.listings.admin.api.SimpleCrudController;
import com.floriparide.listings.admin.api.request.PagingRequest;
import com.floriparide.listings.admin.api.response.ListResponse;
import com.floriparide.listings.dao.IAttributeDao;
import com.floriparide.listings.model.Attribute;
import com.floriparide.listings.web.json.AttributeElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
@Controller
@RequestMapping("/catalog/1.0/branch")
public class BranchSearchController extends BaseController {

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity<String> get(@RequestParam(value = "q", required = true) String query,
                                      @RequestParam(value = "limit", required = true) Integer limit,
                                      @RequestParam(value = "start", required = true) Integer start,
                                      @RequestParam(value = "project_id", required = true) Long projectId,
                                      HttpServletRequest httpRequest,
                                      @RequestParam(value = "attrs", required = false) String... attributes) throws Exception {

        return new ResponseEntity<>("", HttpStatus.OK);
    }

}
