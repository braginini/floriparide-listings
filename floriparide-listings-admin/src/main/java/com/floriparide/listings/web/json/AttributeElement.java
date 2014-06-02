package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.Attribute;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class AttributeElement extends MultiLangElement<Attribute> {

    @JsonProperty("group_id")
    Long groupId;

    @JsonProperty("possible_values")
    List<String> possibleValues;

    @JsonProperty("value")
    String currentValue;

    @JsonProperty("input_type")
    String inputType;

    @JsonProperty("filter_type")
    String filterType;

    public AttributeElement() {
    }

    public AttributeElement(@NotNull Attribute attribute) {
        this.id = attribute.getId();
        this.groupId = attribute.getGroupId();
        this.names = attribute.getNames();
        this.possibleValues = attribute.getPossibleValues();
        this.currentValue = attribute.getCurrentValue();

        Attribute.FilterType filterType = attribute.getFilterType();
        if (filterType != null)
            this.filterType = filterType.getType();

        Attribute.InputType inputType = attribute.getInputType();
        if (inputType != null)
            this.inputType = inputType.getType();
    }

    public static List<AttributeElement> attributesToAttributeElements(List<Attribute> attributes) {
        if (attributes == null) return Collections.emptyList();
        List<AttributeElement> attributeElements = new ArrayList<AttributeElement>(attributes.size());

        for (Attribute a : attributes)
            attributeElements.add(new AttributeElement(a));

        return attributeElements;
    }

    public static List<Attribute> attributesElementsToAttribute(List<AttributeElement> attributeElements) {
        if (attributeElements == null) return Collections.emptyList();

        List<Attribute> attributes = new ArrayList<>(attributeElements.size());

        for (AttributeElement a : attributeElements)
            attributes.add(a.getModel());

        return attributes;
    }

    @NotNull
    @JsonIgnore
    public Attribute getModel() {
        return new Attribute(id, groupId, names, possibleValues, currentValue,
                Attribute.InputType.lookup(inputType), Attribute.FilterType.lookup(filterType));
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<String> getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(List<String> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }
}
