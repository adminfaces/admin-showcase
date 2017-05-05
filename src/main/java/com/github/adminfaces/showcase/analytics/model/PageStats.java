package com.github.adminfaces.showcase.analytics.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmpestano on 01/05/17.
 */
public class PageStats {

    private String viewId;
    private List<PageView> pageViews = new ArrayList<>();
    private boolean persisted;

    public PageStats(String viewId) {
        this.viewId = viewId;
    }

    public void addPageView(PageView pageView) {
        pageViews.add(pageView);
    }

    public long getTotalPageViews() {
        return pageViews.size();
    }

    public Long getUniquePageViews() {
        pageViews.stream().d
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

    public boolean isPersisted() {
        return persisted;
    }

    public void setPersisted(boolean persisted) {
        this.persisted = persisted;
    }
}
