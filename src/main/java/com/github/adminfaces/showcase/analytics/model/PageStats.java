package com.github.adminfaces.showcase.analytics.model;

import java.util.*;

/**
 * Created by rmpestano on 01/05/17.
 */
public class PageStats {

    private String viewId;
    private List<PageView> pageViews = new ArrayList<>();
    private boolean showVisitorsInfo;

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
        Map<String,String> pageIps = new HashMap<>();
        for (PageView pageView : pageViews) {
            if(!pageIps.containsKey(pageView.getIp())){
                pageIps.put(pageView.getIp(),"");
            }
        }
        return pageIps.size();
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
}
