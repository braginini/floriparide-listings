package com.floriparide.listings.dao.postgres.springjdbc.mapper;

import com.floriparide.listings.model.Project;
import com.floriparide.listings.model.Schema;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Andrey Parfenov
 */
public class ProjectRowMapper implements RowMapper<Project> {

    @Override
    @NotNull
    //todo check for values that are not present in ResultSet. Define list of NotNull values
    public Project mapRow(ResultSet rs, int i) throws SQLException {

        Project project = new Project();
        project.setId(rs.getLong(Schema.FIELD_ID));
        project.setName(rs.getString(Schema.FIELD_NAME));

        return project;
    }
}
