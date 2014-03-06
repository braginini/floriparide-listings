package com.floriparide.listings.dao.postgres.springjdbc.mapper;

import com.floriparide.listings.dao.postgres.json.ModelJsonFactory;
import com.floriparide.listings.model.Branch;
import com.floriparide.listings.model.Schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Mikhail Bragin
 */
public class BranchRowMapper implements RowMapper<Branch> {

	private static final Logger log = LoggerFactory.getLogger(BranchRowMapper.class);

	@Override
	public Branch mapRow(ResultSet rs, int rowNum) throws SQLException {
		Branch branch = new Branch();
		branch.setId(rs.getLong(Schema.FIELD_ID));
		branch.setName(rs.getString(Schema.FIELD_NAME));
		branch.setCompanyId(rs.getLong(Schema.TABLE_BRANCH_FIELD_COMPANY_ID));
		branch.setCreated(rs.getLong(Schema.FIELD_CREATED));
		branch.setUpdated(rs.getLong(Schema.FIELD_UPDATED));
		try {
			ModelJsonFactory.populateBranchDataFromJSON(branch, rs.getString(Schema.FIELD_DATA));
		} catch (org.json.simple.parser.ParseException e) {
			log.error("Error while mapping Branch model", e);
		}

		return branch;
	}
}
