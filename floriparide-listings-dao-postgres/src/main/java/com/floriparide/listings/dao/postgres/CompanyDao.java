package com.floriparide.listings.dao.postgres;

import com.floriparide.listings.dao.ICompanyDao;
import com.floriparide.listings.dao.postgres.springjdbc.AbstractSpringJdbc;
import com.floriparide.listings.model.Company;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.IOException;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class CompanyDao extends AbstractSpringJdbc implements ICompanyDao {

	private static final Logger log = LoggerFactory.getLogger(CompanyDao.class);

	public CompanyDao(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate) {
		super(namedJdbcTemplate, jdbcTemplate);
	}

	@Override
	public long create(long projectId, @NotNull Company company) throws IOException {
		return 0;
	}

	@Override
	public void delete(long projectId, long companyId) throws IOException {

	}

	@Override
	public long update(long projectId, @NotNull Company company) throws IOException {
		return 0;
	}

	@NotNull
	@Override
	public Company get(long projectId, long companyId) throws IOException {
		return null;
	}

	@NotNull
	@Override
	public List<Company> getCompanies(long projectId, int start, int end) throws IOException {
		return null;
	}

	@NotNull
	@Override
	public List<Company> getCompanies(long projectId) throws IOException {
		return null;
	}
}
