package com.floriparide.listings.admin.api;

import com.floriparide.listings.dao.IAttributesGroupDao;
import com.floriparide.listings.model.AttributesGroup;
import com.floriparide.listings.web.json.AttributesGroupElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Mikhail Bragin
 */
@Controller
@RequestMapping("/admin/v1/attributesgroup")
public class AttributesGroupController extends SimpleCrudController<AttributesGroupElement, AttributesGroup, IAttributesGroupDao> {

    @Autowired
    public void setDao(IAttributesGroupDao dao) {
        super.setDao(dao);
    }
}
