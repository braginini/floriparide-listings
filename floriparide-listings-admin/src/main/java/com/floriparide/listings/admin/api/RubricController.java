package com.floriparide.listings.admin.api;

import com.floriparide.listings.dao.IRubricDao;
import com.floriparide.listings.model.Rubric;
import com.floriparide.listings.web.json.RubricElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Mikhail Bragin
 */
@Controller
@RequestMapping("/admin/v1/rubric")
public class RubricController extends SimpleCrudController<RubricElement, Rubric, IRubricDao> {

    @Autowired
    public void setDao(IRubricDao dao) {
        super.setDao(dao);
    }
}
