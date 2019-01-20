package com.akvasoft.atlenta.modal;

import javax.persistence.*;

@Entity
@Table(name = "Atlanta")
public class Atlanta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name = "date", length = 100)
    String date;
    @Column(name = "record_no", length = 100)
    String recordNo;
    @Column(name = "record_type", length = 100)
    String record_type;
    @Column(name = "address", length = 1000)
    String address;
    @Column(name = "city", length = 1000)
    String city;
    @Column(name = "state", length = 1000)
    String state;
    @Column(name = "zip", length = 1000)
    String zip;
    @Column(name = "description", length = 3000)
    String description;
    @Column(name = "permit", length = 100)
    String permit;
    @Column(name = "status", length = 100)
    String status;
    @Column(name = "url", length = 4000)
    String url;
    @Column(name = "contact", length = 1000)
    String contact;
    @Column(name = "applicant", length = 2000)
    String applicant;
    @Column(name = "professional", length = 2000)
    String professional;
    @Column(name = "project_desc", length = 2000)
    String project_desc;
    @Column(name = "owner", length = 2000)
    String owner;
    @Column(name = "residential", length = 500)
    String residential;

    public String getResidential() {
        return residential;
    }

    public void setResidential(String residential) {
        if (this.getResidential() == null) {
            this.residential = "--";
        }
        this.residential = residential;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRecord_no() {
        return recordNo;
    }

    public void setRecord_no(String record_no) {
        this.recordNo = record_no;
    }

    public String getRecord_type() {
        return record_type;
    }

    public void setRecord_type(String record_type) {
        this.record_type = record_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPermit() {
        return permit;
    }

    public void setPermit(String permit) {
        this.permit = permit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getProject_desc() {
        return project_desc;
    }

    public void setProject_desc(String project_desc) {
        this.project_desc = project_desc;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
