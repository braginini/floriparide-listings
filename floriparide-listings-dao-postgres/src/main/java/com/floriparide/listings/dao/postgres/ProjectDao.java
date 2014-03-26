package com.floriparide.listings.dao.postgres;

import com.floriparide.listings.dao.IProjectDao;
import com.floriparide.listings.dao.postgres.springjdbc.CrudDao;
import com.floriparide.listings.dao.postgres.springjdbc.mapper.ProjectRowMapper;
import com.floriparide.listings.model.Project;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @author Andrey Parfenov
 */
public class ProjectDao extends CrudDao<Project> implements IProjectDao {

    public ProjectDao(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate) {
        super(namedJdbcTemplate, jdbcTemplate);
        rowMapper = new ProjectRowMapper();
    }
}
