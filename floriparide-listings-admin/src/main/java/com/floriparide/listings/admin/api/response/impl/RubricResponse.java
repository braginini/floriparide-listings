package com.floriparide.listings.admin.api.response.impl;

import com.floriparide.listings.admin.api.response.EntityResponse;
import com.floriparide.listings.model.Rubric;
import com.floriparide.listings.web.json.RubricElement;

/**
 * @author Mikhail Bragin
 */
public class RubricResponse extends EntityResponse<RubricElement> {

	public RubricResponse(Rubric rubric) {
		super(new RubricElement(rubric));
	}
}
