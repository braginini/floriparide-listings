package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.PagingRequest;
import com.floriparide.listings.admin.api.request.impl.CreateEntityRequest;
import com.floriparide.listings.admin.api.request.impl.UpdateEntityRequest;
import com.floriparide.listings.admin.api.response.ListResponse;
import com.floriparide.listings.dao.IAttributeDao;
import com.floriparide.listings.model.Attribute;
import com.floriparide.listings.model.Project;
import com.floriparide.listings.web.json.AttributeElement;
import com.floriparide.listings.web.json.ProjectElement;

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
 * Controller that defines all operations with attributes.
 * <p/>
 * All the methods should throw an exception that are handled and converted to proper JSON response in
 * {@link com.floriparide.listings.admin.api.BaseController#handleException(javax.servlet.http.HttpServletResponse, Exception)}
 *
 * @author Andrey Parfenov
 */
@Controller
@RequestMapping("/admin/v1/attribute")
public class AttributeController extends BaseController implements CRUDController<AttributeElement> {

    @Autowired
    IAttributeDao attributeDao;

    @RequestMapping(method = RequestMethod.POST, value = "/create", consumes = "application/json",
            headers = "Accept=application/json")
    public ResponseEntity<Long> create(@RequestBody CreateEntityRequest<AttributeElement> request,
                                       HttpServletRequest httpRequest) throws Exception {

        request.validate();

        AttributeElement attributeElement = request.getEntity();
        long id = attributeDao.create(attributeElement.getModel());

        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete", consumes = "application/json",
            headers = "Accept=application/json")
    public ResponseEntity delete(@RequestParam(value = "id", required = true) long id,
                                 HttpServletRequest httpRequest) throws Exception {

        attributeDao.delete(id);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update", consumes = "application/json",
            headers = "Accept=application/json")
    public ResponseEntity update(@RequestBody UpdateEntityRequest<AttributeElement> request,
                                 HttpServletRequest httpRequest) throws Exception {

        request.validate();

        Attribute attribute = request.getEntity().getModel();
        attributeDao.update(attribute);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get", consumes = "application/json",
            headers = "Accept=application/json")
    public ResponseEntity<AttributeElement> get(@RequestParam(value = "id", required = true) long id,
                                              HttpServletRequest httpRequest) throws Exception {

        Attribute attribute = attributeDao.get(id);

        if (attribute == null)
            return new ResponseEntity<AttributeElement>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<AttributeElement>(new AttributeElement(attribute), HttpStatus.OK);
    }

    /**
     * Gets a list of attributes
     *
     * @param request {@link com.floriparide.listings.admin.api.request.PagingRequest} with limit and offset
     * @return an instance of {@link com.floriparide.listings.admin.api.response.ListResponse<AttributeElement>}
     * wrapped with {@link org.springframework.http.ResponseEntity} with a HTTP 200 or HTTP 204 status code.
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, value = "/list", consumes = "application/json",
            headers = "Accept=application/json")
    public ResponseEntity<ListResponse<AttributeElement>> list(PagingRequest request,
                                                             HttpServletRequest httpRequest) throws Exception {

        request.validate();

        int totalCount = attributeDao.size(); //todo mb do it other way and in transaction?
        List<Attribute> attributes;

        if (request.getSortFieldModel() == null) //no sorting specified
            attributes = attributeDao.getAttributes(request.getOffset(), request.getLimit());
        else
            attributes = attributeDao.getAttributes(request.getOffset(), request.getLimit(),
                    request.getSortFieldModel(), request.getSortTypeModel());

        return new ResponseEntity<ListResponse<AttributeElement>>(new ListResponse<AttributeElement>(totalCount,
                AttributeElement.attributesToAttributeElements(attributes)), HttpStatus.OK);
    }


}
