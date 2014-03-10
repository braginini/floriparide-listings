package com.floriparide.listings.dao.postgres;

import com.floriparide.listings.dao.IAttributeDao;
import com.floriparide.listings.dao.IAttributesGroupDao;
import com.floriparide.listings.dao.postgres.json.ModelJsonFactory;
import com.floriparide.listings.dao.postgres.springjdbc.AbstractSpringJdbc;
import com.floriparide.listings.dao.postgres.springjdbc.mapper.AttributesGroupRowMapper;
import com.floriparide.listings.dao.postgres.springjdbc.mapper.BranchRowMapper;
import com.floriparide.listings.model.AttributesGroup;
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
public class AttributesGroupDao extends AbstractSpringJdbc implements IAttributesGroupDao {

	private static final Logger log = LoggerFactory.getLogger(AttributesGroupDao.class);

	IAttributeDao attributeDao;

	protected AttributesGroupDao(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate, IAttributeDao attributeDao) {
		super(namedJdbcTemplate, jdbcTemplate);
		this.attributeDao = attributeDao;
	}

	private static final String table = Schema.TABLE_ATTRIBUTES_GROUP;

	@Override
	public long create(@NotNull AttributesGroup entity) throws Exception {

		String query = "INSERT INTO " + table + " ("
				+ Schema.FIELD_CREATED +
				"," + Schema.FIELD_UPDATED +
				"," + Schema.FIELD_DATA +
				") VALUES (:created, :updated, :data::json)"+
				" WHERE " + Schema.FIELD_ID + " = :id";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		getNamedJdbcTemplate().update(query,
				new MapSqlParameterSource()
						.addValue("created", System.currentTimeMillis())
						.addValue("updated", System.currentTimeMillis())
						.addValue("id", entity.getId())
						.addValue("data", ModelJsonFactory.getAttributesGroupJSONData(entity)),
				keyHolder);

		Long id = (Long) keyHolder.getKeys().get(Schema.FIELD_ID);
		if (entity.getAttributes() != null && !entity.getAttributes().isEmpty()) {

		}

		return id;
	}

	@Override
	public void delete(long entityId) throws Exception {
		String query = "DELETE FROM " + table + " WHERE " + Schema.FIELD_ID + " = :id";

		getNamedJdbcTemplate().update(query,
				new MapSqlParameterSource("id", entityId));
	}

	@Override
	public void update(@NotNull AttributesGroup entity) throws Exception {

		String query = "UPDATE " + table + " SET " +
				Schema.FIELD_UPDATED + " = :updated" +
				"," + Schema.FIELD_DATA + " = :data::json";

		getNamedJdbcTemplate().update(query,
				new MapSqlParameterSource()
						.addValue("updated", System.currentTimeMillis())
						.addValue("data", ModelJsonFactory.getAttributesGroupJSONData(entity)));
	}

	@Nullable
	@Override
	public AttributesGroup get(long entityId) throws Exception {
		try {

			String query = "SELECT * FROM " + table + " WHERE " + Schema.FIELD_ID + " = :id";

			return getNamedJdbcTemplate().queryForObject(query,
					new MapSqlParameterSource("id", entityId),
					new AttributesGroupRowMapper());

		} catch (EmptyResultDataAccessException e) {
			log.debug("Empty result exception has been swallowed", e);
		}

		return null;
	}
}
