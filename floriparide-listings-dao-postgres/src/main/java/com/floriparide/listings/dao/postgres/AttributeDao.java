package com.floriparide.listings.dao.postgres;

import com.floriparide.listings.dao.IAttributeDao;
import com.floriparide.listings.dao.postgres.springjdbc.AbstractSpringJdbc;
import com.floriparide.listings.model.Attribute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @author Mikhail Bragin
 */
public class AttributeDao extends AbstractSpringJdbc implements IAttributeDao {

	protected AttributeDao(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate) {
		super(namedJdbcTemplate, jdbcTemplate);
	}

	@Override
	public long create(@NotNull Attribute entity) throws Exception {
		return 0;
	}

	@Override
	public void delete(long entityId) throws Exception {

	}

	@Override
	public void update(@NotNull Attribute entity) throws Exception {

	}

	@Nullable
	@Override
	public Attribute get(long entityId) throws Exception {
		return null;
	}
}
