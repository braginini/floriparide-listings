package com.floriparide.listings.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikhail Bragin
 */
public class Rubric extends MultiLangMetaModel {

	@Nullable
	Long id;

	@Nullable
	Long parentId;

	List<Long> childrenIds;

	List<Rubric> children;

	List<AttributesGroup> attributesGroups;

	public Rubric() {
	}

	public Rubric(Long id, Long parentId, List<Rubric> children, List<AttributesGroup> attributesGroups, Map<String, String> names) {
		super(names);
		this.id = id;
		this.parentId = parentId;
		this.children = children;
		this.attributesGroups = attributesGroups;
	}

    @Nullable
	public Long getId() {
		return id;
	}

	public void setId(@Nullable Long id) {
		this.id = id;
	}

	@Nullable
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(@Nullable Long parentId) {
		this.parentId = parentId;
	}

	public List<Rubric> getChildren() {
		return children;
	}

	public void setChildren(List<Rubric> children) {
		this.children = children;
	}

	public void setAttributesGroups(List<AttributesGroup> attributesGroups) {
		this.attributesGroups = attributesGroups;
	}

	@NotNull
	public List<AttributesGroup> getAttributesGroups() {
		return (this.attributesGroups != null) ? this.attributesGroups : Collections.<AttributesGroup>emptyList();
	}

	@NotNull
	public List<Long> getChildrenIds() {
		return (this.childrenIds != null) ? this.childrenIds : Collections.<Long>emptyList();
	}
}
