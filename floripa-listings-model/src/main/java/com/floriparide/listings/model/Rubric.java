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

	List<RubricAttribute> attributes;

	public Rubric(Long id, String name, Long parentId, List<Long> children) {
		this(name, parentId, children);
		this.id = id;
	}

	public Rubric(String name, Long parentId, List<Long> children) {
		this.name = name;
		this.parentId = parentId;
		this.children = children;
	}

	public Rubric(Long id, String name, Long parentId, List<Long> children, List<RubricAttribute> attributes) {
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		this.children = children;
		this.attributes = attributes;
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
	public List<RubricAttribute> getAttributes() {
		return (this.attributes != null) ? this.attributes : Collections.<RubricAttribute>emptyList();
	}

	@NotNull
	public List<Long> getChildren() {
		return (this.children != null) ? this.children : Collections.<Long>emptyList();
	}
}
