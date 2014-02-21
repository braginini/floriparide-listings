package com.floriparide.listings.admin.api.response.impl;

import com.floriparide.listings.admin.api.response.ListResponse;
import com.floriparide.listings.model.Project;
import com.floriparide.listings.web.json.ProjectElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Andrey Parfenov
 */
public class ProjectListResponse extends ListResponse<ProjectElement> {

    public ProjectListResponse(Integer totalCount, Integer currentCount, @NotNull List<Project> projects) {
        super(totalCount, currentCount, ProjectElement.projectsToElements(projects));
    }
}
