package com.floriparide.listings.admin.api.response.impl;

import com.floriparide.listings.admin.api.response.EntityResponse;
import com.floriparide.listings.model.Project;
import com.floriparide.listings.web.json.ProjectElement;

/**
 * @author Andrey Parfenov
 */
public class ProjectResponse extends EntityResponse<ProjectElement> {

    public ProjectResponse(Project project) {
        super(new ProjectElement(project));
    }
}
