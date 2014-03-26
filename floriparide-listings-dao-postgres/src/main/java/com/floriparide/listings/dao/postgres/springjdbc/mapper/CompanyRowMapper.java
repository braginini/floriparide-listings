package com.floriparide.listings.dao.postgres.springjdbc.mapper;

import com.floriparide.listings.model.Company;
import com.floriparide.listings.model.Schema;
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
    //todo check for values that are not present in ResultSet. Define list of NotNull values
    public Company mapRow(ResultSet rs, int i) throws SQLException {

        Company company = new Company();
        company.setId(rs.getLong(Schema.FIELD_ID));
        company.setName(rs.getString(Schema.FIELD_NAME));
        company.setProjectId(rs.getLong(Schema.TABLE_COMPANY_FIELD_PROJECT_ID));
        company.setPromoText(rs.getString(Schema.TABLE_COMPANY_FIELD_PROMO));
        company.setDescription(rs.getString(Schema.FIELD_DESCRIPTION));

        return company;
    }
}
