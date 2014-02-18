package com.floriparide.listings.dao.postgres.springjdbc.mapper;

import com.floriparide.listings.model.Company;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Mikhail Bragin
 */
public class CompanyRowMapper implements RowMapper<Company> {

	@Override
	@NotNull
	public Company mapRow(ResultSet rs, int i) throws SQLException {

		Company company = new Company();
		company.setId(rs.getLong("id"));
		company.setName(rs.getString("name"));
		company.setProjectId(rs.getLong("project_id"));
		company.setPromoText(rs.getString("promo"));
		company.setDescription(rs.getString("description"));

		return company;
	}
}
