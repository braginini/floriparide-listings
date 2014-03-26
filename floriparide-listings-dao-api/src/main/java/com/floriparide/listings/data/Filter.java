package com.floriparide.listings.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrei Tupitcyn
 */
public class Filter {

    protected String filter;

    protected List binds;

    public Filter() {
        binds = new ArrayList();
    }

    public Filter(String filter) {
        this.filter = filter;
        binds = new ArrayList();
    }

    public Filter(String filter, List binds) {
        this.filter = filter;
        this.binds = binds;
    }

    public String getFilter() {
        return filter;
    }

    public List getBinds() {
        return binds;
    }
}
