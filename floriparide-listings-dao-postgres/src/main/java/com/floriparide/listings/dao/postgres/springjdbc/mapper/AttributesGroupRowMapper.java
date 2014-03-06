package com.floriparide.listings.dao.postgres.springjdbc.mapper;

import com.floriparide.listings.dao.postgres.json.ModelJsonFactory;
import com.floriparide.listings.model.AttributesGroup;
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
public class AttributesGroupRowMapper implements RowMapper<AttributesGroup> {

	private static final Logger log = LoggerFactory.getLogger(AttributesGroupRowMapper.class);

	@Override
	public AttributesGroup mapRow(ResultSet rs, int rowNum) throws SQLException {

		AttributesGroup attributesGroup = new AttributesGroup();

		attributesGroup.setId(rs.getLong(Schema.FIELD_ID));
		attributesGroup.setCreated(rs.getLong(Schema.FIELD_CREATED));
		attributesGroup.setUpdated(rs.getLong(Schema.FIELD_UPDATED));
		try {
			ModelJsonFactory.populateAttributesGroupDataFromJSON(attributesGroup, rs.getString(Schema.FIELD_DATA));
		} catch (ParseException e) {
			log.error("error while mapping data field", e);
		}

		return attributesGroup;
	}
}
