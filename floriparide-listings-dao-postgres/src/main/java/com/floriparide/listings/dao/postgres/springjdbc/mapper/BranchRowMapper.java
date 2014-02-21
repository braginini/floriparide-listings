package com.floriparide.listings.dao.postgres.springjdbc.mapper;

import com.floriparide.listings.model.Branch;
import com.floriparide.listings.model.Point;
import com.floriparide.listings.model.Schema;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Mikhail Bragin
 */
public class BranchRowMapper implements RowMapper<Branch> {

	@Override
	public Branch mapRow(ResultSet rs, int rowNum) throws SQLException {
		Branch branch = new Branch();
		branch.setId(rs.getLong(Schema.FIELD_ID));
		branch.setName(rs.getString(Schema.FIELD_NAME));
		branch.setAddress(rs.getString(Schema.TABLE_BRANCH_FIELD_ADDRESS));
		branch.setArticle(rs.getString(Schema.TABLE_BRANCH_FIELD_ARTICLE));
		branch.setDescription(rs.getString(Schema.FIELD_DESCRIPTION));
		branch.setPoint(new Point(rs.getDouble(Schema.TABLE_BRANCH_FIELD_LAT), rs.getDouble(Schema.TABLE_BRANCH_FIELD_LON)));
		return null;
	}
}
