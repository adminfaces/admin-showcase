package com.github.adminfaces.showcase.analytics.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by rmpestano on 01/05/17.
 *
 * Informations of a page view
 */
public class PageView implements Serializable {

    private String ip;
    private Calendar date;
    private String country;
    private String lat;
    private String lon;

    public PageView(String ip) {
        this.ip = ip;
        this.date = Calendar.getInstance();
    }

    public String getIp() {
        return ip;
    }

    public Calendar getDate() {
        return date;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
