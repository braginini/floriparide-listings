package com.floriparide.listings.dao.postgres;

import com.floriparide.listings.dao.IRubricDao;
import com.floriparide.listings.dao.postgres.json.ModelJsonFactory;
import com.floriparide.listings.dao.postgres.springjdbc.CrudDao;
import com.floriparide.listings.dao.postgres.springjdbc.mapper.RubricRowMapper;
import com.floriparide.listings.model.Rubric;
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
public class RubricDao extends CrudDao<Rubric> implements IRubricDao {

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

	@Override
	protected RowMapper<Rubric> getRowMapper() {
		return new RubricRowMapper();
	}


}
