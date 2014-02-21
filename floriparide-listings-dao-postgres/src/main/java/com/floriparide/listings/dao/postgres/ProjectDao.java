package com.floriparide.listings.dao.postgres;

import com.floriparide.listings.dao.ICompanyDao;
import com.floriparide.listings.dao.IProjectDao;
import com.floriparide.listings.dao.postgres.springjdbc.AbstractSpringJdbc;
import com.floriparide.listings.dao.postgres.springjdbc.mapper.CompanyRowMapper;
import com.floriparide.listings.dao.postgres.springjdbc.mapper.ProjectRowMapper;
import com.floriparide.listings.model.Company;
import com.floriparide.listings.model.Project;
import com.floriparide.listings.model.Schema;
import com.floriparide.listings.model.sort.SortField;
import com.floriparide.listings.model.sort.SortType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Andrey Parfenov
 */
public class ProjectDao extends AbstractSpringJdbc implements IProjectDao {

    private static final String table = Schema.TABLE_PROJECT;

    private static final Logger log = LoggerFactory.getLogger(ProjectDao.class);

    public ProjectDao(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate) {
        super(namedJdbcTemplate, jdbcTemplate);
    }

    @Override
    public long create(@NotNull Project project) throws Exception {

        Assert.notNull(project, "Parameter project must not be null");

        String query = "INSERT INTO " + table + " () VALUES ()";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        getNamedJdbcTemplate().update(query,
                new MapSqlParameterSource(),
                keyHolder);

        Long id = (Long) keyHolder.getKeys().get(Schema.FIELD_ID_TABLE_PROJECT);

        return id;
    }

    @Override
    public void delete(long projectId) throws Exception {

        String query = "DELETE FROM " + table + " WHERE " + Schema.FIELD_ID_TABLE_PROJECT + " = :id";

        getNamedJdbcTemplate().update(query,
                new MapSqlParameterSource("id", projectId));
    }

    @Override
    public void update(@NotNull Project project) throws Exception {

        Assert.notNull(project);

        /*String query = "UPDATE " + table + " SET " +
                Schema.FIELD_NAME_TABLE_COMPANY + "= :name," +
                Schema.FIELD_DESCRIPTION_TABLE_COMPANY + "= : description," +
                Schema.FIELD_PROJECT_ID_TABLE_COMPANY + "= : project_id," +
                Schema.FIELD_PROMO_TABLE_COMPANY + "= : promo" +
                " WHERE " + Schema.FIELD_ID_TABLE_COMPANY + " = :id";

        getNamedJdbcTemplate().update(query,
                new MapSqlParameterSource()
                        .addValue("name", company.getName())
                        .addValue("description", company.getDescription())
                        .addValue("project_id", company.getProjectId())
                        .addValue("promo", company.getPromoText())
                        .addValue("id", company.getId()));*/
        //TODO implement where there is something to update
    }

    @Nullable
    @Override
    public Project get(long projectId) throws Exception {

        try {

            String query = "SELECT * FROM " + table + " WHERE " + Schema.FIELD_ID_TABLE_PROJECT + " = :id";

            return getNamedJdbcTemplate().queryForObject(query,
                    new MapSqlParameterSource("id", projectId),
                    new ProjectRowMapper());

        } catch (EmptyResultDataAccessException e) {
            log.debug("Empty result exception has been swallowed", e);
        }

        return null;
    }

    @NotNull
    @Override
    public List<Project> getProjects(int offset, int limit) throws Exception {

        String query = "SELECT * FROM " + table + " LIMIT :limit OFFSET :offset";

        return getNamedJdbcTemplate().query(query,
                new MapSqlParameterSource()
                        .addValue("limit", limit)
                        .addValue("offset", offset),
                new ProjectRowMapper());
    }

    @NotNull
    @Override
    public List<Project> getProjects() throws Exception {


        String query = "SELECT * FROM " + table;

        return getNamedJdbcTemplate().query(query,
                new ProjectRowMapper());
    }

    @Override
    public int size() throws Exception {

        String query = "SELECT COUNT(*) FROM " + table;

        return getNamedJdbcTemplate().queryForObject(query, new MapSqlParameterSource(), Integer.class);
    }

    @NotNull
    @Override
    public List<Project> getProjects(int offset, int limit, @NotNull SortField sortField, @NotNull SortType sortType) throws Exception {
        String query = "SELECT * FROM " + table +
                " ORDER BY " + sortField.getKey() + " " + sortType.getKey() + " LIMIT :limit OFFSET :offset";

        return getNamedJdbcTemplate().query(query,
                new MapSqlParameterSource()
                        .addValue("limit", limit)
                        .addValue("offset", offset),
                new ProjectRowMapper());
    }
}
