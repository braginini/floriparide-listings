package com.floriparide.listings.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Created by Mikhail Bragin
 */
public class Rubric {

	@Nullable
	Long id;

	String name;

	@Nullable
	Long parentId;

	List<Long> childrenIds;

	List<Rubric> children;

	List<AttributesGroup> attributesGroups;

	public Rubric() {
	}

	public Rubric(Long id, String name, Long parentId, List<Long> childrenIds) {
		this(name, parentId, childrenIds);
		this.id = id;
	}

	public Rubric(String name, Long parentId, List<Long> childrenIds) {
		this.name = name;
		this.parentId = parentId;
		this.childrenIds = childrenIds;
	}

	public Rubric(Long id, String name, Long parentId, List<Long> childrenIds, List<AttributesGroup> attributesGroups) {
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		this.childrenIds = childrenIds;
		this.attributesGroups = attributesGroups;
	}

	@Nullable
	public Long getId() {
		return id;
	}

	public void setId(@Nullable Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
