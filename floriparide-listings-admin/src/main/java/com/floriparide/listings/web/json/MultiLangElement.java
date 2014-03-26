package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public abstract class MultiLangElement<T> extends Element<T> {

    @JsonProperty("")
    Map<String, String> names;

    protected MultiLangElement() {
    }

    protected MultiLangElement(Long id, Map<String, String> names) {
        super(id);
        this.names = names;
    }

    protected MultiLangElement(Map<String, String> names) {
        this.names = names;
    }

    public Map<String, String> getNames() {
        return names;
    }

    public void setNames(Map<String, String> names) {
        this.names = names;
    }
}
