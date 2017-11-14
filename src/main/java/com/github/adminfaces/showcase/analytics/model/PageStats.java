package com.github.adminfaces.showcase.analytics.model;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.adminfaces.template.util.Assert.has;

/**
 * Created by rmpestano on 01/05/17.
 */
public class PageStats {

    private String viewId;
    private List<PageView> pageViews = new ArrayList<>();
    private boolean showVisitorsInfo;
    private Map<String, Map<String, List<PageView>>> pageViewsByCountryAndCity;//first map key is country, second key is city
    private List<PageViewCountry> pageViewCountryList;
    private Integer uniquePageViews;

    public PageStats(String viewId) {
        this.viewId = viewId;
    }

    public void addPageView(PageView pageView) {
        pageViews.add(pageView);
    }

    public long getTotalPageViews() {
        return pageViews.size();
    }

    public Integer getUniquePageViews() {
        if (uniquePageViews == null) {
            initPageViewsCount();
        }

        return uniquePageViews;
    }

    public void initPageViewsCount() {
        List<PageView> pageViewsCopy = new ArrayList<>(pageViews.size());
        pageViewsCopy.addAll(pageViews);
        Map<String, String> pageIps = new HashMap<>();
        for (PageView pageView : pageViewsCopy) {
            if (!pageIps.containsKey(pageView.getIp())) {
                pageIps.put(pageView.getIp(), "");
            }
        }
        uniquePageViews = pageIps.size();
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public List<PageView> getPageViews() {
        return pageViews;
    }

    public void setPageViews(List<PageView> pageViews) {
        this.pageViews = pageViews;
    }

    public boolean isShowVisitorsInfo() {
        return showVisitorsInfo;
    }

    public void setShowVisitorsInfo(boolean showVisitorsInfo) {
        this.showVisitorsInfo = showVisitorsInfo;
    }

    public Integer totalByCountryAndCity(String country, String city) {
        return getPageViewsByCountryAndCity().get(country).get(city).size();
    }

    public Map<String, Map<String, List<PageView>>> getPageViewsByCountryAndCity() {
        if (pageViewsByCountryAndCity == null) {
            pageViewsByCountryAndCity = new HashMap<>();
            for (PageView pageView : pageViews) {
                if (!has(pageView.getCountry()) || !has(pageView.getCity())) {
                    continue;
                }
                if (!pageViewsByCountryAndCity.containsKey(pageView.getCountry())) {
                    Map<String, List<PageView>> cityPageViews = new HashMap<>();
                    pageViewsByCountryAndCity.put(pageView.getCountry(), cityPageViews);
                }
                List<PageView> cityPageViews = pageViewsByCountryAndCity.get(pageView.getCountry()).get(pageView.getCity());
                if (cityPageViews == null) {
                    cityPageViews = new ArrayList<>();
                    pageViewsByCountryAndCity.get(pageView.getCountry()).put(pageView.getCity(), cityPageViews);
                }
                cityPageViews.add(pageView);

            }
        }
        return pageViewsByCountryAndCity;
    }

    public List<PageViewCountry> getPageViewCountryList() {
        if (pageViewCountryList == null) {
            pageViewCountryList = new ArrayList<>();
            //for each country
            for (Map.Entry<String, Map<String, List<PageView>>> countryPageView : getPageViewsByCountryAndCity().entrySet()) {
                //fore each city
                for (Map.Entry<String, List<PageView>> cityView : countryPageView.getValue().entrySet()) {
                    PageViewCountry pageViewCountry = new PageViewCountry(countryPageView.getKey(), cityView.getKey(), cityView.getValue().size());
                    pageViewCountryList.add(pageViewCountry);
                }

            }
            Collections.sort(pageViewCountryList);
        }
        return pageViewCountryList;
    }
}
