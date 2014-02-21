package com.floriparide.listings.admin.api.request.impl;

import org.springframework.util.Assert;

/**
 * @author Andrey Parfenov
 */
public class UpdateProjectRequest extends CreateProjectRequest {

    @Override
    public void validate() throws Exception {
        super.validate();
        Assert.isTrue(false, "Nothing to update yet");
    }
}
