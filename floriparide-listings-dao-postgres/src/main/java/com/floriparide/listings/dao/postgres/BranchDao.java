package com.floriparide.listings.dao.postgres;

import com.floriparide.listings.dao.IBranchDao;
import com.floriparide.listings.dao.postgres.springjdbc.AbstractSpringJdbc;
import com.floriparide.listings.dao.postgres.springjdbc.mapper.BranchRowMapper;
import com.floriparide.listings.model.Attribute;
import com.floriparide.listings.model.Branch;
import com.floriparide.listings.model.Contact;
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

		branch.setId(id);

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
		updatePaymentOption(branch);

	}

	private void updatePaymentOption(@NotNull Branch branch) throws Exception {

		if (branch.getPaymentOptions() != null && !branch.getPaymentOptions().isEmpty()) {

			try {
				addPaymentOptions(branch.getId(), branch.getPaymentOptions());
			} catch (DuplicateKeyException e) {
				log.debug("Duplicate key detected while updating attributes. Don't worry I'll handle this");
				List<PaymentOption> fromDb = new ArrayList<>(); //todo reference getAttributes
				List<PaymentOption> referencesToCreate = new ArrayList<>(); //references to create
				List<PaymentOption> referencesToUpdate = new ArrayList<>(); //references to create

				for (PaymentOption p : branch.getPaymentOptions()) {
					if (!fromDb.contains(p))
						referencesToCreate.add(p);

					fromDb.remove(p);
				}

				addPaymentOptions(branch.getId(), referencesToCreate);
				deletePaymentOptions(branch.getId(), fromDb);
			}
		}

	}

	private void deletePaymentOptions(final long branchId, @NotNull final List<PaymentOption> toDelete) throws Exception {
		String query = "DELETE FROM " + Schema.TABLE_BRANCH_PAYMENT_OPTIONS + " WHERE " +
				Schema.FIELD_BRANCH_ID + " = ? AND " + Schema.TABLE_BRANCH_PAYMENT_OPTIONS_FIELD_PAYMENT_OPTION + " = ?";

		getJdbcTemplate().batchUpdate(query, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				PaymentOption p = toDelete.get(i);
				ps.setLong(1, branchId);
				ps.setString(2, p.name());
			}

			@Override
			public int getBatchSize() {
				return toDelete.size();
			}
		});
	}

	private void updateContacts(@NotNull Branch branch) throws Exception {
		if (branch.getContacts() != null && !branch.getContacts().isEmpty()) {

			try {
				addContacts(branch.getId(), branch.getContacts());
			} catch (DuplicateKeyException e) {
				log.debug("Duplicate key detected while updating attributes. Don't worry I'll handle this");
				List<Contact> fromDb = new ArrayList<>(); //todo reference getAttributes
				List<Contact> referencesToCreate = new ArrayList<>(); //references to create
				List<Contact> referencesToUpdate = new ArrayList<>(); //references to create

				for (Contact c : branch.getContacts()) {
					if (!fromDb.contains(c))
						referencesToCreate.add(c);
					else
						referencesToUpdate.add(c);

					fromDb.remove(c);
				}

				addContacts(branch.getId(), referencesToCreate);
				updateContacts(branch.getId(), referencesToUpdate);
				deleteContacts(branch.getId(), fromDb);
			}
		}
	}

	private void deleteContacts(final long branchId, @NotNull final List<Contact> toDelete) throws Exception {
		String query = "DELETE FROM " + Schema.TABLE_BRANCH_CONTACTS + " WHERE " +
				Schema.FIELD_BRANCH_ID + " = ? AND " + Schema.FIELD_ID + " = ?";

		getJdbcTemplate().batchUpdate(query, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Contact contact = toDelete.get(i);
				ps.setLong(1, branchId);
				ps.setLong(2, contact.getId());
			}

			@Override
			public int getBatchSize() {
				return toDelete.size();
			}
		});
	}

	private void updateContacts(final long branchId, @NotNull final List<Contact> toUpdate) {

		String query = "UPDATE " + Schema.TABLE_BRANCH_CONTACTS + " SET " + Schema.FIELD_VALUE +
				" = ? WHERE " + Schema.TABLE_BRANCH_ATTRIBUTES_FIELD_BRANCH_ID + " = ? AND "
				+ Schema.FIELD_ID + " = ?";

		getJdbcTemplate().batchUpdate(query, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Contact contact = toUpdate.get(i);
				ps.setString(1, contact.getValue());
				ps.setLong(2, branchId);
				ps.setLong(3, contact.getId());
			}

			@Override
			public int getBatchSize() {
				return toUpdate.size();
			}
		});
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
				List<Attribute> referencesToCreate = new ArrayList<>();
				List<Attribute> referencesToUpdate = new ArrayList<>();

				for (Attribute a : branch.getAttributes()) {
					if (!attributesFromDb.contains(a))
						referencesToCreate.add(a);
					else
						referencesToUpdate.add(a);

					attributesFromDb.remove(a);
				}

				addAttributes(branch.getId(), referencesToCreate);
				updateAttributes(branch.getId(), referencesToUpdate);
				deleteAttributes(branch.getId(), attributesFromDb);

			}
		}
	}

	private void deleteAttributes(final long branchId, @NotNull final List<Attribute> toDelete) {

		String query = "DELETE FROM " + Schema.TABLE_BRANCH_ATTRIBUTES + " WHERE " +
				Schema.FIELD_BRANCH_ID + " = ? AND " + Schema.TABLE_BRANCH_ATTRIBUTES_FIELD_ATTRIBUTE_ID + " = ?";

		getJdbcTemplate().batchUpdate(query, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Attribute attribute = toDelete.get(i);
				ps.setLong(1, branchId);
				ps.setLong(2, attribute.getId());
			}

			@Override
			public int getBatchSize() {
				return toDelete.size();
			}
		});

	}

	private void updateAttributes(final long branchId, @NotNull final List<Attribute> toUpdate) {

		String query = "UPDATE " + Schema.TABLE_BRANCH_ATTRIBUTES + " SET " + Schema.FIELD_VALUE +
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
				"," + Schema.TABLE_BRANCH_ATTRIBUTES_FIELD_ATTRIBUTE_ID + "," + Schema.FIELD_VALUE +
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
				"," + Schema.TABLE_BRANCH_PAYMENT_OPTIONS_FIELD_PAYMENT_OPTION + ") VALUES (?, ?::payment_option);";

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

	private void addContact(long branchId, Contact contact) throws Exception {
		addContacts(branchId, Arrays.asList(contact));
	}

	private void addContacts(final long branchId, @NotNull final List<Contact> contacts) throws Exception {
		String query = "INSERT INTO " + Schema.TABLE_BRANCH_CONTACTS + " (" + Schema.FIELD_BRANCH_ID +
				"," + Schema.FIELD_VALUE + "," + Schema.FIELD_COMMENT + "," + Schema.TABLE_BRANCH_CONTACTS_FIELD_CONTACT +
				") VALUES (?, ?, ?, ?);";

		getJdbcTemplate().batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Contact contact = contacts.get(i);
				ps.setLong(1, branchId);
				ps.setString(2, contact.getValue());
				ps.setString(2, contact.getComment());
				ps.setString(3, contact.getType().name());
			}

			@Override
			public int getBatchSize() {
				return contacts.size();
			}
		});
	}
}
