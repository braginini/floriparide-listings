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

	List<Rubric> children;

	public Rubric(Long id, String name, Long parentId, List<Rubric> children) {
		this(name, parentId, children);
		this.id = id;
	}

	public Rubric(String name, Long parentId, List<Rubric> children) {
		this.name = name;
		this.parentId = parentId;
		this.children = children;
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
	public List<Rubric> getChildren() {
		return (this.children != null) ? children : Collections.<Rubric>emptyList();
	}
}
