package com.floriparide.listings.admin.api.request.impl;

import com.floriparide.listings.admin.api.request.CreateEntityRequest;
import com.floriparide.listings.web.json.ProjectElement;
import org.springframework.util.Assert;

/**
 * @author Andrey Parfenov
 */
public class CreateProjectRequest extends CreateEntityRequest<ProjectElement> {

    @Override
    public void validate() throws Exception {
        Assert.notNull(entity.getName(), "Field name must not be null");
    }
}
