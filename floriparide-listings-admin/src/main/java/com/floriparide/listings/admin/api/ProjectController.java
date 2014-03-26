package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.PagingRequest;
import com.floriparide.listings.admin.api.response.ListResponse;
import com.floriparide.listings.dao.IProjectDao;
import com.floriparide.listings.model.Project;
import com.floriparide.listings.web.json.ProjectElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
public class ProjectController extends SimpleCrudController<ProjectElement, Project, IProjectDao> {

    @Autowired
    public void setDao(IProjectDao dao) {
        super.setDao(dao);
    }

    /**
     * Gets a list of projects
     *
     * @param request {@link com.floriparide.listings.admin.api.request.PagingRequest} with limit and offset
     * @return an instance of {@link com.floriparide.listings.admin.api.response.ListResponse<ProjectElement>}
     * wrapped with {@link org.springframework.http.ResponseEntity} with a HTTP 200 or HTTP 204 status code.
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, consumes = "application/json",
            headers = "Accept=application/json")
    public ResponseEntity<ListResponse<ProjectElement>> list(PagingRequest request,
                                                             HttpServletRequest httpRequest) throws Exception {

        request.validate();

        int totalCount = dao.size(); //todo mb do it other way and in transaction?
        List<Project> projects = dao.getList(request.getQuery());

        return new ResponseEntity<>(new ListResponse<>(totalCount,
                ProjectElement.projectsToElements(projects)), HttpStatus.OK);
    }


}
