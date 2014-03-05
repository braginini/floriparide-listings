package com.floriparide.listings.dao.postgres;

import com.floriparide.listings.dao.IAttributeDao;
import com.floriparide.listings.dao.postgres.springjdbc.AbstractSpringJdbc;
import com.floriparide.listings.model.Attribute;

import com.floriparide.listings.model.Schema;
import com.floriparide.listings.model.sort.SortField;
import com.floriparide.listings.model.sort.SortType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class AttributeDao extends AbstractSpringJdbc implements IAttributeDao {

    private static final String table = Schema.TABLE_ATTRIBUTE;

    private static final Logger log = LoggerFactory.getLogger(AttributeDao.class);


    protected AttributeDao(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate) {
		super(namedJdbcTemplate, jdbcTemplate);
	}

    @Override
    public long create(@NotNull Attribute attribute) throws Exception {

        Assert.notNull(attribute, "Parameter attribute must not be null");

        String query = "INSERT INTO " + table + " (" +
                Schema.FIELD_NAME + ", " +
                Schema.TABLE_ATTRIBUTE_FIELD_GROUP_ID + ", " +
                Schema.TABLE_ATTRIBUTE_FIELD_POSSIBLE_VALUES +
                ") VALUES (:name, :group_id, :possible_values)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        getNamedJdbcTemplate().update(query,
                new MapSqlParameterSource()
                    .addValue("name", attribute.getName())
                    .addValue("group_id", attribute.getGroupId())
                    .addValue("possible_values", Attribute.asCommaSeparated(attribute.getPossibleValues())),
                keyHolder);

        Long id = (Long) keyHolder.getKeys().get(Schema.FIELD_ID);

        return id;
    }

	@Override
	public void delete(long entityId) throws Exception {
        String query = "DELETE FROM " + table + " WHERE " + Schema.FIELD_ID + " = :id";

        getNamedJdbcTemplate().update(query,
                new MapSqlParameterSource("id", entityId));
	}

	@Override
	public void update(@NotNull Attribute entity) throws Exception {

	}

	@Nullable
	@Override
	public Attribute get(long entityId) throws Exception {
		return null;
	}

    @Override
    public int size() throws Exception {
        return 0;
    }

    @NotNull
    @Override
    public List<Attribute> getAttributes(int offset, int limit) throws Exception {
        return null;
    }

    @NotNull
    @Override
    public List<Attribute> getAttributes(int offset, int limit, @NotNull SortField sortField, @NotNull SortType sortType) throws Exception {
        return null;
    }
}
