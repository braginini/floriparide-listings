package com.floriparide.listings.dao.postgres;

import com.floriparide.listings.dao.IBranchDao;
import com.floriparide.listings.dao.postgres.springjdbc.AbstractSpringJdbc;
import com.floriparide.listings.dao.postgres.springjdbc.mapper.BranchRowMapper;
import com.floriparide.listings.model.Attribute;
import com.floriparide.listings.model.Branch;
import com.floriparide.listings.model.PaymentOption;
import com.floriparide.listings.model.Rubric;
import com.floriparide.listings.model.Schema;
import com.floriparide.listings.model.sort.SortField;
import com.floriparide.listings.model.sort.SortType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
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
	//TODO make transactional
	public long create(@NotNull Branch branch) throws Exception {

		String query = "INSERT INTO " + table + " ("
				+ Schema.FIELD_NAME +
				"," + Schema.FIELD_DESCRIPTION +
				"," + Schema.TABLE_BRANCH_FIELD_ARTICLE +
				"," + Schema.TABLE_BRANCH_FIELD_COMPANY_ID +
				") VALUES (:name, :description, :article, :company_id, :address, :lat, :lon, :office, " +
				":currency, :created, :updated)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		getNamedJdbcTemplate().update(query,
				new MapSqlParameterSource()
						.addValue("name", branch.getName())
						.addValue("description", branch.getDescription())
						.addValue("article", branch.getArticle())
						.addValue("address", branch.getAddress())
						.addValue("lat", (branch.getPoint() != null) ? branch.getPoint().getLat() : null)
						.addValue("lon", (branch.getPoint() != null) ? branch.getPoint().getLon() : null)
						.addValue("office", branch.getOffice())
						.addValue("currency", branch.getCurrency())
						.addValue("created", System.currentTimeMillis())
						.addValue("updated", System.currentTimeMillis())
						.addValue("company_id", branch.getCompanyId()),
				keyHolder);

		Long id = (Long) keyHolder.getKeys().get(Schema.FIELD_ID);

		if (branch.getAttributes() != null && !branch.getAttributes().isEmpty())
			addAttributes(branch.getId(), branch.getAttributes());

		if (branch.getRubrics() != null && !branch.getRubrics().isEmpty())
			addRubrics(branch.getId(), branch.getRubrics());

		if (branch.getPaymentOptions() != null && !branch.getPaymentOptions().isEmpty())
			addPaymentOptions(branch.getId(), branch.getPaymentOptions());

		return id;
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

	private void addAttribute(long branchId, @NotNull Attribute attribute) throws Exception {
		addAttributes(branchId, Arrays.asList(attribute));
	}

	private void addAttributes(final long branchId, @NotNull final List<Attribute> attributes) throws Exception {

		String query = "INSERT INTO " + Schema.TABLE_BRANCH_ATTRIBUTES + " (" + Schema.FIELD_BRANCH_ID +
				"," + Schema.TABLE_BRANCH_ATTRIBUTES_FIELD_ATTRIBUTE_ID + "," + Schema.TABLE_BRANCH_ATTRIBUTES_FIELD_VALUE +
				") VALUES (?, ?, ?);";

		getJdbcTemplate().batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Attribute attribute = attributes.get(i);
				ps.setLong(1, branchId);
				ps.setLong(2, attribute.getId());
				ps.setString(3, attribute.getCurrentValue().toString());
			}

			@Override
			public int getBatchSize() {
				return attributes.size();
			}
		});
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

	private void addPaymentOption(long branchId, @NotNull PaymentOption paymentOption) throws Exception {
		addPaymentOptions(branchId, Arrays.asList(paymentOption));
	}

	private void addPaymentOptions(final long branchId, @NotNull final List<PaymentOption> paymentOptions) throws Exception {

		String query = "INSERT INTO " + Schema.TABLE_BRANCH_PAYMENT_OPTIONS + " (" + Schema.FIELD_BRANCH_ID +
				"," + Schema.TABLE_BRANCH_PAYMENT_OPTIONS_FIELD_PAYMENT_OPTION + ") VALUES (?, ?);";

		getJdbcTemplate().batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				PaymentOption paymentOption = paymentOptions.get(i);
				ps.setLong(1, branchId);
				ps.setString(2, paymentOption.name().toLowerCase());
			}

			@Override
			public int getBatchSize() {
				return paymentOptions.size();
			}
		});
	}
}
