package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.PagingRequest;
import com.floriparide.listings.admin.api.response.ListResponse;
import com.floriparide.listings.dao.IAttributeDao;
import com.floriparide.listings.model.Attribute;
import com.floriparide.listings.web.json.AttributeElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Controller that defines all operations with attributes.
 * <p/>
 * All the methods should throw an exception that are handled and converted to proper JSON response in
 * {@link com.floriparide.listings.BaseController#handleException(javax.servlet.http.HttpServletResponse, Exception)}
 *
 * @author Andrey Parfenov
 */
@Controller
@RequestMapping("/admin/v1/attribute")
public class AttributeController extends SimpleCrudController<AttributeElement, Attribute, IAttributeDao> {

    @Autowired
    public void setDao(IAttributeDao dao) {
        super.setDao(dao);
    }

    /**
     * Gets a list of attributes
     *
     * @param request {@link com.floriparide.listings.admin.api.request.PagingRequest} with limit and offset
     * @return an instance of {@link com.floriparide.listings.admin.api.response.ListResponse<AttributeElement>}
     * wrapped with {@link org.springframework.http.ResponseEntity} with a HTTP 200 or HTTP 204 status code.
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, consumes = "application/json",
            headers = "Accept=application/json")
    public ResponseEntity<ListResponse<AttributeElement>> list(PagingRequest request,
                                                               HttpServletRequest httpRequest) throws Exception {

        request.validate();

        int totalCount = dao.size(); //todo mb do it other way and in transaction?
        List<Attribute> attributes;

        if (request.getSortFieldModel() == null) //no sorting specified
            attributes = dao.getAttributes(request.getOffset(), request.getLimit());
        else
            attributes = dao.getAttributes(request.getOffset(), request.getLimit(),
                    request.getSortFieldModel(), request.getSortTypeModel());

        return new ResponseEntity<>(new ListResponse<>(totalCount,
                AttributeElement.attributesToAttributeElements(attributes)), HttpStatus.OK);
    }


}
