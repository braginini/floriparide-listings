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

	//common fields
	public static final String FIELD_ID = "id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_DESCRIPTION = "description";

	//specific fields
	//company
	public static final String TABLE_COMPANY_FIELD_PROMO = "promo";
	public static final String TABLE_COMPANY_FIELD_PROJECT_ID = "project_id";

	//branch
	public static final String TABLE_BRANCH_FIELD_COMPANY_ID = "company_id";
	public static final String TABLE_BRANCH_FIELD_LAT = "lat";
	public static final String TABLE_BRANCH_FIELD_LON = "lon";
	public static final String TABLE_BRANCH_FIELD_ADDRESS = "address";
	public static final String TABLE_BRANCH_FIELD_OFFICE = "office";
	public static final String TABLE_BRANCH_FIELD_CURRENCY = "currency"; //ISO 4217 format
	public static final String TABLE_BRANCH_FIELD_ARTICLE = "article";
}
