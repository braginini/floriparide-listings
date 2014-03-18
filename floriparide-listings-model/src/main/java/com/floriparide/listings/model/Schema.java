package com.floriparide.listings.model;

/**
 * @author Mikhail Bragin
 */
public class Schema {

	//tables
	public static final String TABLE_COMPANY = "company";
	public static final String TABLE_PROJECT = "project";
	public static final String TABLE_BRANCH = "branch";
	public static final String TABLE_RUBRIC= "rubric";
	public static final String TABLE_BRANCH_ATTRIBUTES= "branch_attributes";
	public static final String TABLE_BRANCH_RUBRICS= "branch_rubrics";
    public static final String TABLE_ATTRIBUTE = "attribute";
    public static final String TABLE_ATTRIBUTES_GROUP = "attributes_group";
    public static final String TABLE_RAW_DATA_DATA = "raw_data.data";

	//common fields
	public static final String FIELD_ID = "id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_NAMES = "names";
	public static final String FIELD_META = "meta";
	public static final String FIELD_VALUES = "values";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_RUBRIC_ID = "rubric_id";
	public static final String FIELD_BRANCH_ID = "branch_id";
	public static final String FIELD_CREATED = "created";
	public static final String FIELD_UPDATED = "updated";
	public static final String FIELD_VALUE = "value";
	public static final String FIELD_COMMENT = "comment";
	public static final String FIELD_DATA = "data";


	//specific fields
	//company
	public static final String TABLE_COMPANY_FIELD_PROMO = "promo";
	public static final String TABLE_COMPANY_FIELD_PROJECT_ID = "project_id";

	//branch
	public static final String TABLE_BRANCH_FIELD_COMPANY_ID = "company_id";

	//rubric
	public static final String TABLE_RUBRIC_FIELD_PARENT_ID = "parent_id";

	//attribute
	public static final String TABLE_ATTRIBUTE_FIELD_GROUP_ID = "group_id";
	public static final String TABLE_ATTRIBUTE_FIELD_POSSIBLE_VALUES = "possible_values";

	//raw_data.data
	public static final String TABLE_RAW_DATA_DATA_FIELD_SOURCE = "source";

}
