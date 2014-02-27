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
public class BranchDao extends AbstractSpringJdbc implements IBranchDao {

	private static final String table = Schema.TABLE_BRANCH;

	private static final Logger log = LoggerFactory.getLogger(BranchDao.class);

	AttributeDao attributeDao;

	public BranchDao(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate, AttributeDao attributeDao) {
		super(namedJdbcTemplate, jdbcTemplate);
		this.attributeDao = attributeDao;
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
	@Transactional(propagation = Propagation.REQUIRED)
	public long create(@NotNull Branch branch) throws Exception {

		String query = "INSERT INTO " + table + " ("
				+ Schema.FIELD_NAME +
				"," + Schema.FIELD_DESCRIPTION +
				"," + Schema.TABLE_BRANCH_FIELD_ARTICLE +
				"," + Schema.TABLE_BRANCH_FIELD_COMPANY_ID +
				"," + Schema.TABLE_BRANCH_FIELD_ADDRESS +
				"," + Schema.TABLE_BRANCH_FIELD_LAT +
				"," + Schema.TABLE_BRANCH_FIELD_LON +
				"," + Schema.TABLE_BRANCH_FIELD_OFFICE +
				"," + Schema.TABLE_BRANCH_FIELD_CURRENCY +
				"," + Schema.FIELD_CREATED +
				"," + Schema.FIELD_UPDATED +
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
		String query = "DELETE FROM " + table + " WHERE " + Schema.FIELD_ID + " = :id";

		getNamedJdbcTemplate().update(query,
				new MapSqlParameterSource("id", entityId));
	}

	@Override
	public void update(@NotNull Branch branch) throws Exception {
		String query = "UPDATE " + table + " SET " +
				Schema.FIELD_NAME + " = :name" +
				"," + Schema.FIELD_DESCRIPTION + " = :description" +
				"," + Schema.TABLE_BRANCH_FIELD_ARTICLE + " = :article" +
				"," + Schema.TABLE_BRANCH_FIELD_COMPANY_ID + " = :company_id" +
				"," + Schema.TABLE_BRANCH_FIELD_ADDRESS + " = :address" +
				"," + Schema.TABLE_BRANCH_FIELD_LAT + " = :lat" +
				"," + Schema.TABLE_BRANCH_FIELD_LON + " = :lon" +
				"," + Schema.TABLE_BRANCH_FIELD_OFFICE + " = :office" +
				"," + Schema.TABLE_BRANCH_FIELD_CURRENCY + " = :currency" +
				"," + Schema.FIELD_CREATED + " = :created" +
				"," + Schema.FIELD_UPDATED + " = :updated" +
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

		updateAttributes(branch);
		updateRubrics(branch);
		updateContacts(branch);

	}

	private void updateContacts(@NotNull Branch branch) {

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
				for (Rubric r : branch.getRubrics()) {
					try {
						addRubric(branch.getId(), r);
					} catch (DuplicateKeyException e1) {
						log.debug("Swallowed", e1);
					}
				}
			}
		}
	}

	//DuplicateKeyException is thrown when we already have a branch-attribute reference,
	//so only thing needed is to get the full list of branch attributes and update their values
	//and insert non existent references
	private void updateAttributes(@NotNull Branch branch) throws Exception {
		if (branch.getAttributes() != null && !branch.getAttributes().isEmpty()) {

			try {
				addAttributes(branch.getId(), branch.getAttributes());

			} catch (DuplicateKeyException e) {
				log.debug("Duplicate key detected while updating attributes. Don't worry I'll handle this");
				List<Attribute> attributesFromDb = new ArrayList<>(); //todo reference attributeDao
				List<Attribute> referencesToCreate = new ArrayList<>(); //references to create
				List<Attribute> referencesToUpdate = new ArrayList<>(); //references to create

				for (Attribute a : branch.getAttributes()) {
					if (!attributesFromDb.contains(a))
						referencesToCreate.add(a);
					else
						referencesToUpdate.add(a);
				}

				addAttributes(branch.getId(), referencesToCreate);
				updateAttributes(branch.getId(), referencesToUpdate);

			}
		}
	}

	private void updateAttributes(final long branchId, @NotNull final List<Attribute> toUpdate) {

		String query = "UPDATE " + Schema.TABLE_BRANCH_ATTRIBUTES + " SET " + Schema.TABLE_BRANCH_ATTRIBUTES_FIELD_VALUE +
				" = ? WHERE " + Schema.TABLE_BRANCH_ATTRIBUTES_FIELD_BRANCH_ID + " = ? AND "
				+ Schema.TABLE_BRANCH_ATTRIBUTES_FIELD_ATTRIBUTE_ID + " = ?";

		getJdbcTemplate().batchUpdate(query, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Attribute attribute = toUpdate.get(i);
				ps.setString(1, attribute.getCurrentValue().toString());
				ps.setLong(2, branchId);
				ps.setLong(3, attribute.getId());
			}

			@Override
			public int getBatchSize() {
				return toUpdate.size();
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
