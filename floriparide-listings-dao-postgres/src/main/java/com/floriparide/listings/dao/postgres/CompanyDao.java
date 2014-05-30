package com.floriparide.listings.dao.postgres;

import com.floriparide.listings.dao.ICompanyDao;
import com.floriparide.listings.dao.postgres.springjdbc.CrudDao;
import com.floriparide.listings.dao.postgres.springjdbc.mapper.CompanyRowMapper;
import com.floriparide.listings.model.Company;
import com.floriparide.listings.model.Schema;
import com.floriparide.listings.model.sort.SortField;
import com.floriparide.listings.model.sort.SortType;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class CompanyDao extends CrudDao<Company> implements ICompanyDao {

    public CompanyDao(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate) {
        super(namedJdbcTemplate, jdbcTemplate);
        rowMapper = new CompanyRowMapper();
    }

	@Override
	protected RowMapper<Company> getRowMapper() {
		return new CompanyRowMapper();
	}

	@NotNull
    @Override
    public List<Company> getCompanies(long projectId, int offset, int limit) throws Exception {

        String query = "SELECT * FROM " + table + " WHERE " + Schema.TABLE_COMPANY_FIELD_PROJECT_ID + " = :project_id" +
                " LIMIT :limit OFFSET :offset";

        return getNamedJdbcTemplate().query(query,
                new MapSqlParameterSource()
                        .addValue("limit", limit)
                        .addValue("offset", offset)
                        .addValue("project_id", projectId),
                new CompanyRowMapper());
    }

    @NotNull
    @Override
    public List<Company> getCompanies(long projectId) throws Exception {


        String query = "SELECT * FROM " + table + " WHERE " + Schema.TABLE_COMPANY_FIELD_PROJECT_ID + " = :project_id";

        return getNamedJdbcTemplate().query(query,
                new MapSqlParameterSource("project_id", projectId),
                new CompanyRowMapper());
    }

    @Override
    public int size(long projectId) throws Exception {

        String query = "SELECT COUNT(*) FROM company WHERE " + Schema.TABLE_COMPANY_FIELD_PROJECT_ID + " = :project_id";

        return getNamedJdbcTemplate().queryForObject(query,
                new MapSqlParameterSource("project_id", projectId),
                Integer.class);
    }

    @NotNull
    @Override
    public List<Company> getCompanies(long projectId, int offset, int limit, @NotNull SortField sortField, @NotNull SortType sortType) throws Exception {
        String query = "SELECT * FROM " + table + " WHERE " + Schema.TABLE_COMPANY_FIELD_PROJECT_ID + " = :project_id" +
                " ORDER BY " + sortField.getKey() + " " + sortType.getKey() + " LIMIT :limit OFFSET :offset";

        return getNamedJdbcTemplate().query(query,
                new MapSqlParameterSource()
                        .addValue("limit", limit)
                        .addValue("offset", offset)
                        .addValue("project_id", projectId),
                new CompanyRowMapper());
    }
}
