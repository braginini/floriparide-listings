package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.AttributesGroup;
import com.floriparide.listings.model.Rubric;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class RubricElement implements Element<Rubric> {

	@JsonProperty("")
	Long id;

	@JsonProperty("")
	String name;

	@JsonProperty("")
	Long parentId;

	@JsonProperty("")
	List<RubricElement> children;

	@JsonProperty("attrs_groups")
	List<AttributesGroupElement> attributesGroups;

	public RubricElement() {
	}

	public RubricElement(@NotNull Rubric rubric) {
		this.id = rubric.getId();
		this.name = rubric.getName();
		this.parentId = rubric.getParentId();
		this.children = RubricElement.rubricsToRubricElements(rubric.getChildren());
		this.attributesGroups = AttributesGroupElement.attributesGroupsToElements(rubric.getAttributesGroups());
	}

	public static List<RubricElement> rubricsToRubricElements(List<Rubric> rubrics) {
		if (rubrics == null) return Collections.emptyList();

		List<RubricElement> rubricElements = new ArrayList<RubricElement>(rubrics.size());

		for (Rubric r : rubrics)
			rubricElements.add(new RubricElement(r));

		return rubricElements;
	}

	public static List<Rubric> rubricsElementsToRubrics(List<RubricElement> rubricElements) {
        if (rubricElements == null) return Collections.emptyList();

		List<Rubric> rubrics = new ArrayList<Rubric>(rubricElements.size());

		for (RubricElement r : rubricElements)
			rubrics.add(r.getModel());

		return rubrics;
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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public List<RubricElement> getChildren() {
		return children;
	}

	public void setChildren(List<RubricElement> children) {
		this.children = children;
	}

	public List<AttributesGroupElement> getAttributesGroups() {
		return attributesGroups;
	}

	public void setAttributesGroups(List<AttributesGroupElement> attributesGroups) {
		this.attributesGroups = attributesGroups;
	}

	@Override
	public Rubric getModel() {
		return new Rubric(id, name, parentId, RubricElement.rubricsElementsToRubrics(children));
	}
}
