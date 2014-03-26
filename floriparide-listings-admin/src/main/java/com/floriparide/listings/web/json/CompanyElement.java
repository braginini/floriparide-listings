package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.Company;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class CompanyElement extends Element<Company> {
    @JsonProperty("")
    String name;

    @JsonProperty("")
    String description;

    @JsonProperty("")
    String promo;

    @JsonProperty("project_id")
    Long projectId;

    @JsonProperty("branches_count")
    Integer branchesCount;

    public CompanyElement() {
    }

    public CompanyElement(@NotNull Company c) {
        super(c.getId());
        this.name = c.getName();
        this.description = c.getDescription();
        this.promo = c.getPromoText();
        this.projectId = c.getProjectId();
        this.branchesCount = c.getBranchesCount();
    }

    public static List<CompanyElement> companiesToElements(@NotNull List<Company> companies) {
        ArrayList<CompanyElement> companyElements = new ArrayList<CompanyElement>(companies.size());

        for (Company c : companies)
            companyElements.add(new CompanyElement(c));

        return companyElements;
    }

    @NotNull
    @JsonIgnore
    public Company getModel() {
        Company company = new Company();
        company.setName(name);
        company.setDescription(description);
        company.setProjectId(projectId);
        company.setId(id);
        company.setPromoText(promo);

        return company;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getBranchesCount() {
        return branchesCount;
    }

    public void setBranchesCount(Integer branchesCount) {
        this.branchesCount = branchesCount;
    }
}
