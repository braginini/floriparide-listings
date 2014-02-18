package com.floriparide.listings.model;

/**
 * Defines basic fields for all branches in this entity.
 *
 * @author Mikhail Bragin
 */
public class Company {

	Long id;

	Long projectId;

	String name;

	String description;

	String promoText;

	Integer branchesCount;

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

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getPromoText() {
		return promoText;
	}

	public Integer getBranchesCount() {
		return branchesCount;
	}

	public Long getProjectId() {
		return projectId;
	}
}
