package com.floriparide.listings.dao.postgres;

import com.floriparide.listings.dao.ICompanyDao;
import com.floriparide.listings.dao.postgres.springjdbc.AbstractSpringJdbc;
import com.floriparide.listings.dao.postgres.springjdbc.mapper.CompanyRowMapper;
import com.floriparide.listings.model.Company;
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
 * @author Mikhail Bragin
 */
public class CompanyDao extends AbstractSpringJdbc implements ICompanyDao {

	private static final String table = Schema.TABLE_COMPANY;

	private static final Logger log = LoggerFactory.getLogger(CompanyDao.class);

	public CompanyDao(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate) {
		super(namedJdbcTemplate, jdbcTemplate);
	}

	@Override
	public long create(@NotNull Company company) throws Exception {

		Assert.notNull(company, "Parameter company must not be null");

		String query = "INSERT INTO " + table + " ("
				+ Schema.FIELD_NAME_TABLE_COMPANY +
				"," + Schema.FIELD_DESCRIPTION_TABLE_COMPANY +
				"," + Schema.FIELD_PROJECT_ID_TABLE_COMPANY +
				"," + Schema.FIELD_PROMO_TABLE_COMPANY +
				") VALUES (:name, :description, :project_id, :promo)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		getNamedJdbcTemplate().update(query,
				new MapSqlParameterSource()
						.addValue("name", company.getName())
						.addValue("description", company.getDescription())
						.addValue("promo", company.getPromoText())
						.addValue("project_id", company.getProjectId()),
				keyHolder);

		Long id = (Long) keyHolder.getKeys().get(Schema.FIELD_ID_TABLE_COMPANY);

		return id;
	}

	@Override
	public void delete(long companyId) throws Exception {

		String query = "DELETE FROM " + table + " WHERE " + Schema.FIELD_ID_TABLE_COMPANY + " = :id";

		getNamedJdbcTemplate().update(query,
				new MapSqlParameterSource("id", companyId));
	}

	@Override
	public void update(@NotNull Company company) throws Exception {

		Assert.notNull(company);

		String query = "UPDATE " + table + " SET " +
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
						.addValue("id", company.getId()));
	}

	@Nullable
	@Override
	public Company get(long companyId) throws Exception {

		try {

			String query = "SELECT * FROM " + table + " WHERE " + Schema.FIELD_ID_TABLE_COMPANY + " = :id";

			return getNamedJdbcTemplate().queryForObject(query,
					new MapSqlParameterSource("id", companyId),
					new CompanyRowMapper());

		} catch (EmptyResultDataAccessException e) {
			log.debug("Empty result exception has been swallowed", e);
		}

		return null;
	}

	@NotNull
	@Override
	public List<Company> getCompanies(long projectId, int offset, int limit) throws Exception {

		String query = "SELECT * FROM " + table + " WHERE " + Schema.FIELD_PROJECT_ID_TABLE_COMPANY + " = :project_id" +
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


		String query = "SELECT * FROM " + table + " WHERE " + Schema.FIELD_PROJECT_ID_TABLE_COMPANY + " = :project_id";

		return getNamedJdbcTemplate().query(query,
				new MapSqlParameterSource("project_id", projectId),
				new CompanyRowMapper());
	}

	@Override
	public int size(long projectId) throws Exception {

		String query = "SELECT COUNT(*) FROM company WHERE " + Schema.FIELD_PROJECT_ID_TABLE_COMPANY + " = :project_id";

		return getNamedJdbcTemplate().queryForObject(query,
				new MapSqlParameterSource("project_id", projectId),
				Integer.class);


	}

	@NotNull
	@Override
	public List<Company> getCompanies(long projectId, int offset, int limit, @NotNull SortField sortField, @NotNull SortType sortType) throws Exception {
		String query = "SELECT * FROM " + table + " WHERE " + Schema.FIELD_PROJECT_ID_TABLE_COMPANY + " = :project_id" +
				" ORDER BY " + sortField.getKey() + " " + sortType.getKey() + " LIMIT :limit OFFSET :offset";

		return getNamedJdbcTemplate().query(query,
				new MapSqlParameterSource()
						.addValue("limit", limit)
						.addValue("offset", offset)
						.addValue("project_id", projectId),
				new CompanyRowMapper());
	}
}
