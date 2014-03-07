package com.floriparide.listings.dao.postgres.springjdbc.mapper;

import com.floriparide.listings.dao.postgres.json.ModelJsonFactory;
import com.floriparide.listings.model.Rubric;
import com.floriparide.listings.model.Schema;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Mikhail Bragin
 */
public class RubricRowMapper implements RowMapper<Rubric> {

	private static final Logger log = LoggerFactory.getLogger(RubricRowMapper.class);

	@Override
	public Rubric mapRow(ResultSet rs, int rowNum) throws SQLException {

		Rubric rubric = new Rubric();

		rubric.setId(rs.getLong(Schema.FIELD_ID));
		rubric.setCreated(rs.getLong(Schema.FIELD_CREATED));
		rubric.setUpdated(rs.getLong(Schema.FIELD_UPDATED));
		String data = rs.getString(Schema.FIELD_DATA);
		try {
			ModelJsonFactory.populateRubricDataFromJSON(rubric, data);
		} catch (ParseException e) {
			log.error("Error while mapping Rubric JSOn data " + data, e);
		}

		return rubric;
	}
}
