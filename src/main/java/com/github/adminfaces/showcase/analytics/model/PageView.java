package com.github.adminfaces.showcase.analytics.model;

import java.util.Calendar;

/**
 * Created by rmpestano on 01/05/17.
 *
 * Informations of a page view
 */
public class PageView {

    private String ip;
    private Calendar date;
    private String country;
    private String lat;
    private String lon;

    public PageView(String ip, Calendar date, String country, String lat, String lon) {
        this.ip = ip;
        this.date = date;
        this.country = country;
        this.lat = lat;
        this.lon = lon;
    }

    public String getIp() {
        return ip;
    }

    public Calendar getDate() {
        return date;
    }

    public String getCountry() {
        return country;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
