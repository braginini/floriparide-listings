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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

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

    @Override
    public long create(@NotNull Company company) throws Exception {

        Assert.notNull(company, "Parameter company must not be null");

        String query = "INSERT INTO " + table + " ("
                + Schema.FIELD_NAME +
                "," + Schema.FIELD_DESCRIPTION +
                "," + Schema.TABLE_COMPANY_FIELD_PROJECT_ID +
                "," + Schema.TABLE_COMPANY_FIELD_PROMO +
                ") VALUES (:name, :description, :project_id, :promo)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        getNamedJdbcTemplate().update(query,
                new MapSqlParameterSource()
                        .addValue("name", company.getName())
                        .addValue("description", company.getDescription())
                        .addValue("promo", company.getPromoText())
                        .addValue("project_id", company.getProjectId()),
                keyHolder);

        Long id = (Long) keyHolder.getKeys().get(Schema.FIELD_ID);

        return id;
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
                new CompanyRowMapper( ));
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
