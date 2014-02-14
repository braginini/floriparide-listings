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

	List<Long> children;

	List<AttributesGroup> attributesGroups;

	public Rubric(Long id, String name, Long parentId, List<Long> children) {
		this(name, parentId, children);
		this.id = id;
	}

	public Rubric(String name, Long parentId, List<Long> children) {
		this.name = name;
		this.parentId = parentId;
		this.children = children;
	}

	public Rubric(Long id, String name, Long parentId, List<Long> children, List<AttributesGroup> attributesGroups) {
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		this.children = children;
		this.attributesGroups = attributesGroups;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getParentId() {
		return parentId;
	}

	@NotNull
	public List<AttributesGroup> getAttributesGroups() {
		return (this.attributesGroups != null) ? this.attributesGroups : Collections.<AttributesGroup>emptyList();
	}

	@NotNull
	public List<Long> getChildren() {
		return (this.children != null) ? this.children : Collections.<Long>emptyList();
	}
}
