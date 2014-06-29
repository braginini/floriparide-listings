package com.floriparide.listings.web.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriparide.listings.model.Branch;
import com.floriparide.listings.model.PaymentOption;
import com.floriparide.listings.model.Point;
import com.floriparide.listings.model.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
//todo Schedule
public class BranchElement extends Element<Branch> {

    @JsonProperty("")
    String name;

    @JsonProperty("")
    String description;

    @JsonProperty("")
    List<ContactElement> contacts;

    @JsonProperty("")
    List<RubricElement> rubrics;

    @JsonProperty("")
    List<AttributeElement> attributes;

    @JsonProperty("company_id")
    Long companyId;

    @JsonProperty("")
    Double lat;

    @JsonProperty("")
    Double lon;

    @JsonProperty("")
    String address;

    @JsonProperty("")
    String office;

    @JsonProperty("")
    String currency;

    @JsonProperty("")
    String article;

    @JsonProperty("payment_options")
    List<String> paymentOptions;

    @JsonProperty("raw_schedule")
    String rawSchedule;

    @JsonProperty("raw_address")
    String rawAddress;

    @JsonProperty("")
    ScheduleElement schedule;

    public BranchElement() {
    }

    public BranchElement(Branch branch) {
        super(branch.getId());
        this.name = branch.getName();
        this.description = branch.getDescription();
        this.contacts = ContactElement.contactsToContactElements(branch.getContacts());
        this.rubrics = RubricElement.rubricsToRubricElements(branch.getRubrics());
        this.attributes = AttributeElement.attributesToAttributeElements(branch.getAttributes());
        this.companyId = branch.getCompanyId();
        this.lat = branch.getPoint().getLat();
        this.lon = branch.getPoint().getLon();
        this.address = branch.getAddress();
        this.office = branch.getOffice();
        this.currency = branch.getCurrency();
        this.article = branch.getArticle();
        this.rawSchedule = branch.getRawSchedule();
        this.rawAddress = branch.getRawAddress();
        this.schedule = new ScheduleElement(branch.getSchedule());

        this.paymentOptions = branch.getPaymentOptions();
    }

    public static List<BranchElement> branchesToBranchElements(List<Branch> branches) {
        ArrayList<BranchElement> branchElements = new ArrayList<BranchElement>(branches.size());

        for (Branch b : branches)
            branchElements.add(new BranchElement(b));

        return branchElements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ContactElement> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactElement> contacts) {
        this.contacts = contacts;
    }

    public List<RubricElement> getRubrics() {
        return rubrics;
    }

    public void setRubrics(List<RubricElement> rubrics) {
        this.rubrics = rubrics;
    }

    public List<AttributeElement> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeElement> attributes) {
        this.attributes = attributes;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public List<String> getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(List<String> paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public ScheduleElement getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleElement schedule) {
        this.schedule = schedule;
    }

    public String getRawSchedule() {
        return rawSchedule;
    }

    public void setRawSchedule(String rawSchedule) {
        this.rawSchedule = rawSchedule;
    }

    public String getRawAddress() {
        return rawAddress;
    }

    public void setRawAddress(String rawAddress) {
        this.rawAddress = rawAddress;
    }

    @Override
    @JsonIgnore
    public Branch getModel() {
        Branch branch = new Branch();
        branch.setId(id);
        branch.setName(name);
        branch.setAddress(address);
        branch.setArticle(article);
        branch.setDescription(description);
        branch.setAttributes(AttributeElement.attributesElementsToAttribute(attributes));
        branch.setCompanyId(companyId);
        branch.setContacts(ContactElement.contactsElementsToToContacts(contacts));
        branch.setCurrency(currency);
        branch.setOffice(office);
        if (paymentOptions != null)
            branch.setPaymentOptions(paymentOptions);
        branch.setPoint(new Point(lat, lon));
        branch.setRubrics(RubricElement.rubricsElementsToRubrics(rubrics));
        if (rawSchedule != null)
            branch.setRawSchedule(rawSchedule);
        if (schedule != null)
            branch.setSchedule(schedule.getModel());
        if (rawAddress != null)
            branch.setRawAddress(rawAddress);

        return branch;
    }
}
