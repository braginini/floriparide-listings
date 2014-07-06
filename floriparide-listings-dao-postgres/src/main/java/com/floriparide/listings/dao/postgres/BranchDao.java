package com.floriparide.listings.dao.postgres;

import com.floriparide.listings.dao.IBranchDao;
import com.floriparide.listings.dao.postgres.json.ModelJsonFactory;
import com.floriparide.listings.dao.postgres.springjdbc.CrudDao;
import com.floriparide.listings.dao.postgres.springjdbc.mapper.BranchRowMapper;
import com.floriparide.listings.model.Branch;
import com.floriparide.listings.model.Rubric;
import com.floriparide.listings.model.Schema;
import com.floriparide.listings.model.sort.SortField;
import com.floriparide.listings.model.sort.SortType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class BranchDao extends CrudDao<Branch> implements IBranchDao {

    public BranchDao(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate) {
        super(namedJdbcTemplate, jdbcTemplate);
        rowMapper = new BranchRowMapper();
    }

    @NotNull
    @Override
    public List<Branch> getBranches(long companyId, int offset, int limit) throws Exception {
        String query = "SELECT * FROM " + table + " WHERE " + Schema.TABLE_BRANCH_FIELD_COMPANY_ID + " = :company_id LIMIT :limit OFFSET :offset";

        return getNamedJdbcTemplate().query(query,
                new MapSqlParameterSource("company_id", companyId)
                        .addValue("limit", limit)
                        .addValue("offset", offset),
                new BranchRowMapper());
    }

    @NotNull
    @Override
    public List<Branch> getBranchesInProject(long projectId, int offset, int limit) throws Exception {
        return null;
    }

    @NotNull
    @Override
    public List<Branch> getBranches(long companyId) throws Exception {
        String query = "SELECT * FROM " + table + " WHERE " + Schema.TABLE_BRANCH_FIELD_COMPANY_ID + " = :company_id";

        return getNamedJdbcTemplate().query(query,
                new MapSqlParameterSource("id", companyId),
                new BranchRowMapper());
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
    public long create(@NotNull Branch branch) throws Exception {

        String query = "INSERT INTO " + table + " ("
                + Schema.FIELD_NAME +
                "," + Schema.TABLE_BRANCH_FIELD_COMPANY_ID +
                "," + Schema.FIELD_DATA +
                ") VALUES (:name, :company_id, :data::json)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        getNamedJdbcTemplate().update(query,
                new MapSqlParameterSource()
                        .addValue("name", branch.getName())
                        .addValue("company_id", branch.getCompanyId())
                        .addValue("data", ModelJsonFactory.getBranchJSONData(branch)),
                keyHolder);

        return (Long) keyHolder.getKeys().get(Schema.FIELD_ID);
    }

    @Override
    public void update(@NotNull Branch branch) throws Exception {
        String query = "UPDATE " + table + " SET " +
                Schema.FIELD_NAME + " = :name" +
                "," + Schema.TABLE_BRANCH_FIELD_COMPANY_ID + " = :company_id" +
                "," + Schema.FIELD_DATA + " = :data::json" +
                " WHERE " + Schema.FIELD_ID + " = :id";

        getNamedJdbcTemplate().update(query,
                new MapSqlParameterSource()
                        .addValue("name", branch.getName())
                        .addValue("company_id", branch.getCompanyId())
                        .addValue("id", branch.getId())
                        .addValue("data", ModelJsonFactory.getBranchJSONData(branch)));

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

	@Override
	protected RowMapper<Branch> getRowMapper() {
		return new BranchRowMapper();
	}
}
