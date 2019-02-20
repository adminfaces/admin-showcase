package com.github.adminfaces.showcase.model;

import java.util.Date;
import java.util.List;

public class Entity {

    private Long id;

    private String firstname;

    private String surname;

    private Integer age;
    
    private Date date;

    private Boolean useSpacesOverTabs;

    private List<String> cities;

    private String talk;
    
    private String bio;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getUseSpacesOverTabs() {
        return useSpacesOverTabs;
    }

    public void setUseSpacesOverTabs(Boolean useSpacesOverTabs) {
        this.useSpacesOverTabs = useSpacesOverTabs;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public String getTalk() {
        return talk;
    }

    public void setTalk(String talk) {
        this.talk = talk;
    }
    
}