package com.floriparide.listings.dao.postgres;

import com.floriparide.listings.dao.IAttributesGroupDao;
import com.floriparide.listings.dao.postgres.json.ModelJsonFactory;
import com.floriparide.listings.dao.postgres.springjdbc.CrudDao;
import com.floriparide.listings.dao.postgres.springjdbc.mapper.AttributesGroupRowMapper;
import com.floriparide.listings.model.AttributesGroup;
import com.floriparide.listings.model.Schema;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * @author Mikhail Bragin
 */
public class AttributesGroupDao extends CrudDao<AttributesGroup> implements IAttributesGroupDao {

    protected AttributesGroupDao(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate) {
        super(namedJdbcTemplate, jdbcTemplate);
        rowMapper = new AttributesGroupRowMapper();
    }

    @Override
    public long create(@NotNull AttributesGroup entity) throws Exception {

        String query = "INSERT INTO " + table + " ("
                + Schema.FIELD_CREATED +
                "," + Schema.FIELD_UPDATED +
                "," + Schema.FIELD_DATA +
                ") VALUES (:created, :updated, :data::json)" +
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

        return id;
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

	@Override
	protected RowMapper<AttributesGroup> getRowMapper() {
		return new AttributesGroupRowMapper();
	}
}
