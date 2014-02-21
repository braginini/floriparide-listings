package com.floriparide.listings.model;

import com.floriparide.listings.model.sort.SortField;
import com.floriparide.listings.model.sort.SortModel;
import com.floriparide.listings.model.sort.SortingDictionary;

import java.util.HashSet;

/**
 * Defines basic fields for all branches in this entity.
 *
 * @author Mikhail Bragin
 */
public class Company extends SortModel {

	Long id;

	Long projectId;

	String name;

	String description;

	String promoText;

	Integer branchesCount;

	static {
		SortingDictionary.registerSortFields(Company.class, SortField.CREATED, SortField.UPDATED, SortField.NAME);
	}

	public Company() {
	}

	public Company(Long projectId, String name, String description, String promoText, Integer branchesCount) {
		this.projectId = projectId;
		this.name = name;
		this.description = description;
		this.promoText = promoText;
		this.branchesCount = branchesCount;
	}

	public Company(Long id, Long projectId, String name, String description, String promoText, Integer branchesCount) {
		this(projectId, name, description, promoText, branchesCount);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPromoText() {
		return promoText;
	}

	public void setPromoText(String promoText) {
		this.promoText = promoText;
	}

	public Integer getBranchesCount() {
		return branchesCount;
	}

	public void setBranchesCount(Integer branchesCount) {
		this.branchesCount = branchesCount;
	}
}
