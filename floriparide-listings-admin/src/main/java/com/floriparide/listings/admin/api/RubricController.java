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
import org.springframework.web.bind.annotation.RequestMapping;

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
	public ResponseEntity<Long> create(CreateEntityRequest<RubricElement> request,
	                                   HttpServletRequest httpRequest) throws Exception {

		request.validate();

		Rubric rubric = request.getEntity().getModel();
		long id = rubricDao.create(rubric);

		return new ResponseEntity<>(id, HttpStatus.OK);
	}

	@Override
	public ResponseEntity delete(long id, HttpServletRequest httpRequest) throws Exception {
		return null;
	}

	@Override
	public ResponseEntity update(UpdateEntityRequest<RubricElement> request, HttpServletRequest httpRequest) throws Exception {
		return null;
	}

	@Override
	public ResponseEntity<RubricElement> get(long id, HttpServletRequest httpRequest) throws Exception {
		return null;
	}
}
