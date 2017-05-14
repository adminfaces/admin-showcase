package com.github.adminfaces.showcase.analytics.model;

/**
 * Created by rafael-pestano on 10/05/17.
 */
public class PageViewCountry implements Comparable<PageViewCountry>{
    private String country;
    private String city;
    private Integer viewCount;

    public PageViewCountry(String country, String city, Integer count) {
        this.country = country;
        this.city = city;
        this.viewCount = count;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void addviewCount() {
        viewCount++;
    }


    @Override
    public int compareTo(PageViewCountry o) {
        int i = getCountry().compareTo(o.getCountry());
        if(i==0) {
            i = o.getViewCount().compareTo(getViewCount());
        }
        return i;
    }
}
