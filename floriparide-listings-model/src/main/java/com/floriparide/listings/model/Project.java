package com.floriparide.listings.model;

import com.floriparide.listings.model.sort.SortField;
import com.floriparide.listings.model.sort.SortModel;
import com.floriparide.listings.model.sort.SortingDictionary;

/**
 * This class describes city and metro area.
 *
 * @author Mikhail Bragin
 */
public class Project extends SortModel {

	Long id;

	String name;

	String code;

	String language;

	//ISO 3166
	String countryCode;

	String timezone;

	//WKT format
	String geometry;

	Point centroid;

	Integer minZoom;

	Integer maxZoom;

	Integer defaultZoom;

	Integer companiesCount;

	Integer branchesCount;

	Integer rubricsCount;

	//number of geo objects
	Integer geosCount;

	static {
		SortingDictionary.registerSortFields(Project.class, SortField.UPDATED, SortField.NAME);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getGeometry() {
		return geometry;
	}

	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}

	public Point getCentroid() {
		return centroid;
	}

	public void setCentroid(Point centroid) {
		this.centroid = centroid;
	}

	public Integer getMinZoom() {
		return minZoom;
	}

	public void setMinZoom(Integer minZoom) {
		this.minZoom = minZoom;
	}

	public Integer getMaxZoom() {
		return maxZoom;
	}

	public void setMaxZoom(Integer maxZoom) {
		this.maxZoom = maxZoom;
	}

	public Integer getDefaultZoom() {
		return defaultZoom;
	}

	public void setDefaultZoom(Integer defaultZoom) {
		this.defaultZoom = defaultZoom;
	}

	public Integer getCompaniesCount() {
		return companiesCount;
	}

	public void setCompaniesCount(Integer companiesCount) {
		this.companiesCount = companiesCount;
	}

	public Integer getBranchesCount() {
		return branchesCount;
	}

	public void setBranchesCount(Integer branchesCount) {
		this.branchesCount = branchesCount;
	}

	public Integer getRubricsCount() {
		return rubricsCount;
	}

	public void setRubricsCount(Integer rubricsCount) {
		this.rubricsCount = rubricsCount;
	}

	public Integer getGeosCount() {
		return geosCount;
	}

	public void setGeosCount(Integer geosCount) {
		this.geosCount = geosCount;
	}

}
