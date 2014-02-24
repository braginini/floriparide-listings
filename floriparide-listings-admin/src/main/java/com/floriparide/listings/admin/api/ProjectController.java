package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.PagingRequest;
import com.floriparide.listings.admin.api.request.impl.CreateEntityRequest;
import com.floriparide.listings.admin.api.request.impl.UpdateEntityRequest;
import com.floriparide.listings.admin.api.response.impl.ProjectListResponse;
import com.floriparide.listings.dao.IProjectDao;
import com.floriparide.listings.model.Project;
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
 * Controller that defines all operations with projects.
 * <p/>
 * All the methods should throw an exception that are handled and converted to proper JSON response in
 * {@link com.floriparide.listings.admin.api.BaseController#handleException(javax.servlet.http.HttpServletResponse, Exception)}
 *
 * @author Andrey Parfenov
 */
@Controller
@RequestMapping("/admin/v1/project")
public class ProjectController extends BaseController implements CRUDController<ProjectElement> {

	@Autowired
	IProjectDao projectDao;

	@RequestMapping(method = RequestMethod.POST, value = "/create", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<Long> create(@RequestBody CreateEntityRequest<ProjectElement> request,
	                                   HttpServletRequest httpRequest) throws Exception {

		request.validate();

		ProjectElement projectElement = request.getEntity();
		long id = projectDao.create(projectElement.getModel());

		return new ResponseEntity<Long>(id, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/delete", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity delete(@RequestParam(value = "id", required = true) long id,
	                             HttpServletRequest httpRequest) throws Exception {

		projectDao.delete(id);

		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity update(@RequestBody UpdateEntityRequest<ProjectElement> request,
	                             HttpServletRequest httpRequest) throws Exception {

		request.validate();

		Project project = request.getEntity().getModel();
		projectDao.update(project);

		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/get", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<ProjectElement> get(@RequestParam(value = "id", required = true) long id,
	                                          HttpServletRequest httpRequest) throws Exception {

		Project project = projectDao.get(id);

		if (project == null)
			return new ResponseEntity<ProjectElement>(HttpStatus.NOT_FOUND);

		return new ResponseEntity<ProjectElement>(new ProjectElement(project), HttpStatus.OK);
	}

	/**
	 * Gets a list of projects
	 *
	 * @param request {@link com.floriparide.listings.admin.api.request.PagingRequest} with limit and offset
	 * @return an instance of {@link com.floriparide.listings.admin.api.response.impl.ProjectListResponse} wrapped with
	 * {@link org.springframework.http.ResponseEntity} with a HTTP 200 or HTTP 204 status code.
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/list", consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity<ProjectListResponse> list(PagingRequest request,
	                                                HttpServletRequest httpRequest) throws Exception {

		request.validate();

		int totalCount = projectDao.size(); //todo mb do it other way and in transaction?
		List<Project> projects;

		if (request.getSortFieldModel() == null) //no sorting specified
			projects = projectDao.getProjects(request.getOffset(), request.getLimit());
		else
			projects = projectDao.getProjects(request.getOffset(), request.getLimit(),
					request.getSortFieldModel(), request.getSortTypeModel());

		return new ResponseEntity<ProjectListResponse>(new ProjectListResponse(totalCount, projects.size(), projects),
				HttpStatus.OK);
	}


}
