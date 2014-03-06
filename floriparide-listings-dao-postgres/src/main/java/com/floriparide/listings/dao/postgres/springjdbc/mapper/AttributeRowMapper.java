package com.floriparide.listings.dao.postgres.springjdbc.mapper;

import com.floriparide.listings.dao.postgres.json.ModelJsonFactory;
import com.floriparide.listings.model.Attribute;
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
public class AttributeRowMapper implements RowMapper<Attribute> {

	private static final Logger log = LoggerFactory.getLogger(AttributeRowMapper.class);

	@Override
	public Attribute mapRow(ResultSet rs, int rowNum) throws SQLException {
		Attribute attribute = new Attribute();

		attribute.setId(rs.getLong(Schema.FIELD_ID));
		attribute.setGroupId(rs.getLong(Schema.TABLE_ATTRIBUTE_FIELD_GROUP_ID));
		attribute.setCreated(rs.getLong(Schema.FIELD_CREATED));
		attribute.setUpdated(rs.getLong(Schema.FIELD_UPDATED));

		try {
			ModelJsonFactory.populateAttributeDataFromJSON(attribute, rs.getString(Schema.FIELD_DATA));
		} catch (ParseException e) {
			log.error("Error while mapping attribute JSOn data " + rs.getString(Schema.FIELD_DATA), e);
		}

		return attribute;
	}
}
