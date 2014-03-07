package com.floriparide.listings.dao.postgres;

import com.floriparide.listings.dao.IRubricDao;
import com.floriparide.listings.dao.postgres.json.ModelJsonFactory;
import com.floriparide.listings.dao.postgres.springjdbc.AbstractSpringJdbc;
import com.floriparide.listings.dao.postgres.springjdbc.mapper.CompanyRowMapper;
import com.floriparide.listings.dao.postgres.springjdbc.mapper.RubricRowMapper;
import com.floriparide.listings.model.Rubric;
import com.floriparide.listings.model.Schema;

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

/**
 * @author Mikhail Bragin
 */
public class RubricDao extends AbstractSpringJdbc implements IRubricDao {

	private static final String table = Schema.TABLE_RUBRIC;

	private static final Logger log = LoggerFactory.getLogger(RubricDao.class);

	public RubricDao(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate) {
		super(namedJdbcTemplate, jdbcTemplate);
	}

	@Override
	public long create(@NotNull Rubric entity) throws Exception {
		String query = "INSERT INTO " + table + " ("
				+ Schema.FIELD_CREATED +
				"," + Schema.FIELD_UPDATED +
				"," + Schema.FIELD_DATA +
				"," + Schema.TABLE_RUBRIC_FIELD_PARENT_ID +
				") VALUES (:created, :updated, :data::json, :parent_id)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		getNamedJdbcTemplate().update(query,
				new MapSqlParameterSource()
						.addValue("created", System.currentTimeMillis())
						.addValue("updated", System.currentTimeMillis())
						.addValue("parent_id", entity.getParentId())
						.addValue("data", ModelJsonFactory.getRubricJSONData(entity)),
				keyHolder);

		return (Long) keyHolder.getKeys().get(Schema.FIELD_ID);
	}

	@Override
	public void delete(long entityId) throws Exception {
		String query = "DELETE FROM " + table + " WHERE " + Schema.FIELD_ID + " = :id";

		getNamedJdbcTemplate().update(query,
				new MapSqlParameterSource("id", entityId));
	}

	@Override
	public void update(@NotNull Rubric entity) throws Exception {
		String query = "UPDATE " + table + " SET " +
				Schema.FIELD_UPDATED + " = :updated" + "," +
				Schema.TABLE_RUBRIC_FIELD_PARENT_ID + " = :parent_id" + ","
				+ Schema.FIELD_DATA + " = :data::json" +
				" WHERE " + Schema.FIELD_ID + " = :id";

		getNamedJdbcTemplate().update(query,
				new MapSqlParameterSource()
						.addValue("updated", System.currentTimeMillis())
						.addValue("parent_id", entity.getParentId())
						.addValue("data", ModelJsonFactory.getRubricJSONData(entity))
						.addValue("id", entity.getId()));

	}

	@Nullable
	@Override
	public Rubric get(long entityId) throws Exception {
		try {

			String query = "SELECT * FROM " + table + " WHERE " + Schema.FIELD_ID + " = :id";

			return getNamedJdbcTemplate().queryForObject(query,
					new MapSqlParameterSource("id", entityId),
					new RubricRowMapper());

		} catch (EmptyResultDataAccessException e) {
			log.debug("Empty result exception has been swallowed", e);
		}

		return null;
	}
}
