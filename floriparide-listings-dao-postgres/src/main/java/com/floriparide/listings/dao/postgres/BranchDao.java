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
    //TODO make transactional
    @Transactional(propagation = Propagation.REQUIRED)
    public long create(@NotNull Branch branch) throws Exception {

        String query = "INSERT INTO " + table + " ("
                + Schema.FIELD_NAME +
                "," + Schema.TABLE_BRANCH_FIELD_COMPANY_ID +
                "," + Schema.FIELD_CREATED +
                "," + Schema.FIELD_UPDATED +
                "," + Schema.FIELD_DATA +
                ") VALUES (:name, :company_id, :created, :updated, :data::json)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        getNamedJdbcTemplate().update(query,
                new MapSqlParameterSource()
                        .addValue("name", branch.getName())
                        .addValue("created", System.currentTimeMillis())
                        .addValue("updated", System.currentTimeMillis())
                        .addValue("company_id", branch.getCompanyId())
                        .addValue("data", ModelJsonFactory.getBranchJSONData(branch)),
                keyHolder);

        Long id = (Long) keyHolder.getKeys().get(Schema.FIELD_ID);

        branch.setId(id);

        if (branch.getRubrics() != null && !branch.getRubrics().isEmpty())
            addRubrics(branch.getId(), branch.getRubrics());

        return id;
    }

    @Override
    public void update(@NotNull Branch branch) throws Exception {
        String query = "UPDATE " + table + " SET " +
                Schema.FIELD_NAME + " = :name" +
                "," + Schema.TABLE_BRANCH_FIELD_COMPANY_ID + " = :company_id" +
                "," + Schema.FIELD_UPDATED + " = :updated" +
                "," + Schema.FIELD_DATA + " = :data::json" +
                " WHERE " + Schema.FIELD_ID + " = :id";

        getNamedJdbcTemplate().update(query,
                new MapSqlParameterSource()
                        .addValue("name", branch.getName())
                        .addValue("updated", System.currentTimeMillis())
                        .addValue("company_id", branch.getCompanyId())
                        .addValue("id", branch.getId())
                        .addValue("data", ModelJsonFactory.getBranchJSONData(branch)));

        updateRubrics(branch);

    }

    //DuplicateKeyException is thrown when we already have a branch-rubric reference
    //so we insert the batch one by one ignoring DuplicateKeyException
    //todo maybe get all rubrics and insert that ones that does not exist? it is more elegant
    private void updateRubrics(@NotNull Branch branch) throws Exception {

        if (branch.getRubrics() != null && !branch.getRubrics().isEmpty()) {
            try {
                addRubrics(branch.getId(), branch.getRubrics());
            } catch (DuplicateKeyException e) {
                log.debug("Duplicate key detected while updating rubrics. Don't worry I'll handle this");

                List<Rubric> fromDb = new ArrayList<>(); //todo reference rubricDao
                List<Rubric> referencesToCreate = new ArrayList<>();

                for (Rubric r : branch.getRubrics()) {
                    if (!fromDb.contains(r))
                        referencesToCreate.add(r);

                    fromDb.remove(r);
                }

                addRubrics(branch.getId(), referencesToCreate);
                deleteRubrics(branch.getId(), fromDb);
            }
        }
    }

    private void deleteRubrics(final long branchId, final @NotNull List<Rubric> toDelete) throws Exception {
        String query = "DELETE FROM " + Schema.TABLE_BRANCH_RUBRICS + " WHERE " +
                Schema.FIELD_BRANCH_ID + " = ? AND " + Schema.FIELD_RUBRIC_ID + " = ?";

        getJdbcTemplate().batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Rubric rubric = toDelete.get(i);
                ps.setLong(1, branchId);
                ps.setLong(2, rubric.getId());
            }

            @Override
            public int getBatchSize() {
                return toDelete.size();
            }
        });
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

    private void addRubric(long branchId, @NotNull Rubric rubric) throws Exception {
        addRubrics(branchId, Arrays.asList(rubric));
    }

    private void addRubrics(final long branchId, @NotNull final List<Rubric> rubrics) throws Exception {

        String query = "INSERT INTO " + Schema.TABLE_BRANCH_RUBRICS + " (" + Schema.FIELD_BRANCH_ID +
                "," + Schema.FIELD_RUBRIC_ID + ") VALUES (?, ?);";

        getJdbcTemplate().batchUpdate(query, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Rubric rubric = rubrics.get(i);
                ps.setLong(1, branchId);
                ps.setLong(2, rubric.getId());
            }

            @Override
            public int getBatchSize() {
                return rubrics.size();
            }
        });
    }
}
