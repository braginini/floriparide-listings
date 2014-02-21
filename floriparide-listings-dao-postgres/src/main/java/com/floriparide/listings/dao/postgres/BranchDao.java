package com.floriparide.listings.dao.postgres;

import com.floriparide.listings.dao.IBranchDao;
import com.floriparide.listings.dao.postgres.springjdbc.AbstractSpringJdbc;
import com.floriparide.listings.dao.postgres.springjdbc.mapper.BranchRowMapper;
import com.floriparide.listings.dao.postgres.springjdbc.mapper.CompanyRowMapper;
import com.floriparide.listings.model.Branch;
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

import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class BranchDao extends AbstractSpringJdbc implements IBranchDao {

	private static final String table = Schema.TABLE_BRANCH;

	private static final Logger log = LoggerFactory.getLogger(BranchDao.class);

	public BranchDao(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate) {
		super(namedJdbcTemplate, jdbcTemplate);
	}


	@NotNull
	@Override
	public List<Branch> getBranches(long companyId, int offset, int limit) throws Exception {



		return null;
	}

	@NotNull
	@Override
	public List<Branch> getBranchesInProject(long projectId, int offset, int limit) throws Exception {
		return null;
	}

	@NotNull
	@Override
	public List<Branch> getBranches(long companyId) throws Exception {
		return null;
	}

	@Override
	public int size(long companyId) throws Exception {
		return 0;
	}

	@Override
	public int sizeInProject(long projectId) throws Exception {
		return 0;
	}

	@NotNull
	@Override
	public List<Branch> getBranches(long companyId, int offset, int limit, @NotNull SortField sortField, @NotNull SortType sortType) throws Exception {
		return null;
	}

	@NotNull
	@Override
	public List<Branch> getBranchesInProject(long projectId, int offset, int limit, @NotNull SortField sortField, @NotNull SortType sortType) throws Exception {
		return null;
	}

	@Override
	public long create(@NotNull Branch entity) throws Exception {
		return 0;
	}

	@Override
	public void delete(long entityId) throws Exception {

	}

	@Override
	public void update(@NotNull Branch entity) throws Exception {

	}

	@Nullable
	@Override
	public Branch get(long branchId) throws Exception {
		try {

			String query = "SELECT * FROM " + table + " WHERE " + Schema.FIELD_ID + " = :id";

			return getNamedJdbcTemplate().queryForObject(query,
					new MapSqlParameterSource("id", branchId),
					new BranchRowMapper());

		} catch (EmptyResultDataAccessException e) {
			log.debug("Empty result exception has been swallowed", e);
		}

		return null;
	}
}
