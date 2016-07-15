package com.sahaware.resysbni.entity;

public class DataGeneralInformation {

    private String section;
    private String title;
    private String description;

    public DataGeneralInformation() {
    }

    public DataGeneralInformation(String section, String title, String description) {
        this.section = section;
        this.title = title;
        this.description = description;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}