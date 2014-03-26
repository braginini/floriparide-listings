package com.floriparide.listings.admin.api.request.impl;

import com.floriparide.listings.web.json.*;
import org.springframework.util.Assert;

/**
 * @author Mikhail Bragin
 */
public class UpdateEntityRequest<E extends Element> extends CreateEntityRequest<E> {

    @Override
    public void validate() throws Exception {
        super.validate();
        if (entity instanceof Element) {
            Assert.notNull((entity).getId(), "Field id must not be null");
        } else if (entity instanceof CompanyElement) {
            Assert.notNull(((CompanyElement) entity).getPromo(), "Field promo must not be null");
        } else if (entity instanceof BranchElement) {
        } else if (entity instanceof ProjectElement) {
        } else if (entity instanceof AttributeElement) {
            Assert.notNull(((AttributeElement) entity).getGroupId(), "Field group_id must not be null");
        } else if (entity instanceof AttributesGroupElement) {

        }
    }
}
