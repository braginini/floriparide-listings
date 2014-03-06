package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.impl.CreateEntityRequest;
import com.floriparide.listings.admin.api.request.impl.UpdateEntityRequest;
import com.floriparide.listings.dao.IAttributesGroupDao;
import com.floriparide.listings.model.AttributesGroup;
import com.floriparide.listings.web.json.AttributesGroupElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikhail Bragin
 */
@Controller
@RequestMapping("/admin/v1/attributesgroup")
public class AttributesGroupController extends BaseController implements CRUDController<AttributesGroupElement> {

	@Autowired
	IAttributesGroupDao attributesGroupDao;

	@Override
	@RequestMapping(method = RequestMethod.POST, value = "/create", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<Long> create(@RequestBody CreateEntityRequest<AttributesGroupElement> request,
	                                   HttpServletRequest httpRequest) throws Exception {

		request.validate();

		Long id = attributesGroupDao.create(request.getEntity().getModel());

		return new ResponseEntity<Long>(id, HttpStatus.OK);
	}

	@Override
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity delete(@RequestParam(required = true, value = "id") long id,
	                             HttpServletRequest httpRequest) throws Exception {

		attributesGroupDao.delete(id);

		return new ResponseEntity(HttpStatus.OK);
	}

	@Override
	@RequestMapping(method = RequestMethod.POST, value = "/update", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity update(@RequestBody UpdateEntityRequest<AttributesGroupElement> request,
	                             HttpServletRequest httpRequest) throws Exception {
		request.validate();

		attributesGroupDao.update(request.getEntity().getModel());

		return new ResponseEntity(HttpStatus.OK);
	}

	@Override
	@RequestMapping(method = RequestMethod.GET, value = "/get", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<AttributesGroupElement> get(@RequestParam(required = true, value = "id") long id,
	                                                  HttpServletRequest httpRequest) throws Exception {

		AttributesGroup attributesGroup = attributesGroupDao.get(id);

		if (attributesGroup == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		return new ResponseEntity<>(new AttributesGroupElement(attributesGroup), HttpStatus.OK);
	}
}
