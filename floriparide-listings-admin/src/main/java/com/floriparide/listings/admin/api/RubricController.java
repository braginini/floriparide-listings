package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.impl.CreateEntityRequest;
import com.floriparide.listings.admin.api.request.impl.UpdateEntityRequest;
import com.floriparide.listings.dao.IRubricDao;
import com.floriparide.listings.model.Rubric;
import com.floriparide.listings.web.json.RubricElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Mikhail Bragin
 */
@Controller
@RequestMapping("/admin/v1/rubric")
public class RubricController extends BaseController implements CRUDController<RubricElement> {

	@Autowired
	IRubricDao rubricDao;

	@Override
	public ResponseEntity<Long> create(@RequestBody CreateEntityRequest<RubricElement> request,
	                                   HttpServletRequest httpRequest) throws Exception {

		request.validate();

		Rubric rubric = request.getEntity().getModel();
		long id = rubricDao.create(rubric);

		return new ResponseEntity<>(id, HttpStatus.OK);
	}

	@Override
	public ResponseEntity delete(@RequestParam(value = "id", required = true) long id, HttpServletRequest httpRequest) throws Exception {

		rubricDao.delete(id);

		return new ResponseEntity(HttpStatus.OK);
	}

	@Override
	public ResponseEntity update(@RequestBody UpdateEntityRequest<RubricElement> request, HttpServletRequest httpRequest) throws Exception {

		request.validate();

		rubricDao.update(request.getEntity().getModel());

		return new ResponseEntity(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RubricElement> get(long id, HttpServletRequest httpRequest) throws Exception {
		return null;
	}
}
