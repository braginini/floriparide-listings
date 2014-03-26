package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.impl.CreateEntityListRequest;
import com.floriparide.listings.dao.IRawDataDao;
import com.floriparide.listings.web.json.RawDataElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikhail Bragin
 */
@Controller
@RequestMapping("/admin/v1/rawdata")
public class RawDataController extends BaseController {

    @Autowired
    IRawDataDao rawDataDao;

    @RequestMapping(method = RequestMethod.POST, value = "/create", consumes = "application/json",
            headers = "Accept=application/json")
    public ResponseEntity create(@RequestBody CreateEntityListRequest<RawDataElement> request,
                                 HttpServletRequest httpRequest) throws Exception {

        request.validate();
        rawDataDao.create(RawDataElement.getRawDataModelsFromRawDataElements(request.getEntities()));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
