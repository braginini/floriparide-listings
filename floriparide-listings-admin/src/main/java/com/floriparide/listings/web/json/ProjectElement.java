package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.Project;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrey Parfenov
 */
public class ProjectElement {

    @JsonProperty("")
    Long id;

    public ProjectElement() {
    }

    public ProjectElement(@NotNull Project p) {
        this.id = p.getId();
    }

    public static List<ProjectElement> projectsToElements(@NotNull List<Project> projects) {
        ArrayList<ProjectElement> projectElements = new ArrayList<ProjectElement>(projects.size());

        for (Project p : projects)
            projectElements.add(new ProjectElement(p));

        return projectElements;
    }

    @NotNull
    @JsonIgnore
    public Project getModel() {
        Project project = new Project();
        project.setId(this.id);

        return project;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
