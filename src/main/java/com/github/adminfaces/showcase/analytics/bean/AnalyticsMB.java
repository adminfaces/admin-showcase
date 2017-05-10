package com.github.adminfaces.showcase.analytics.bean;

import com.github.adminfaces.showcase.analytics.model.PageStats;
import com.github.adminfaces.showcase.analytics.model.PageView;
import com.github.adminfaces.showcase.analytics.store.PageStatisticsStore;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

import static com.github.adminfaces.template.util.Assert.has;

/**
 * Created by rmpestano on 01/05/17.
 */
@Named
@ViewScoped
public class AnalyticsMB implements Serializable {

    private String viewId;
    private PageStats pageStats;
    private List<PageStats> pageStatsList;
    private List<PageStats> filteredStats;

    @Inject
    private PageStatisticsStore analyticsStore;

    @PostConstruct
    public void onPageVisited() {
        viewId = Faces.getViewId();
        ExternalContext ec = Faces.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        analyticsStore.addPageView(viewId, new PageView(request.getRemoteAddr()));
        pageStats = analyticsStore.getPageStats(viewId);
    }


    public PageStats getPageStats() {
        return pageStats;
    }

    public List<PageStats> getPageStatsList() {
        if (pageStatsList == null) {
            initPageStatsList();
        }
        return pageStatsList;
    }

    private void initPageStatsList() {
        pageStatsList = analyticsStore.getPageStatsWithCountries();
        for (PageStats stats : pageStatsList) {
            Collections.sort(stats.getPageViews(), new Comparator<PageView>() {
                @Override
                public int compare(PageView pageView1, PageView pageView2) {
                    return pageView1.getCountry() != null && pageView2.getCountry() != null ? pageView1.getCountry().compareTo(pageView2.getCountry()) : -1;
                }
            });
            stats.setShowVisitorsInfo(false);
        }

    }

    public void clearFilter() {
        initPageStatsList();
    }

    public List<String> completeCountry(String query) {
        List<String> results = new ArrayList<>();

        if (has(query) && query.length() >= 2) {
            List<String> pageViewCountries = analyticsStore.getPageViewCountries();
            for (String pageViewCountry : pageViewCountries) {
                if (pageViewCountry.toLowerCase().contains(query.toLowerCase())) {
                    results.add(pageViewCountry);
                }
            }
        }

        return results;
    }

    public void onCountrySelect(SelectEvent event) {
        String selectedCountry = event.getObject().toString();
        List<PageStats> pageStatsByCountry = new ArrayList<>();
        for (PageStats stats : pageStatsList) {
            List<PageView> filteredPageViews = new ArrayList<>();
            for (PageView view : stats.getPageViews()) {
                if (has(view.getCountry()) && view.getCountry().toLowerCase().contains(selectedCountry.toLowerCase())) {
                    filteredPageViews.add(view);
                }
            }
            if (!filteredPageViews.isEmpty()) {
                PageStats pageStats = new PageStats(stats.getViewId());
                pageStats.setPageViews(filteredPageViews);
                pageStatsByCountry.add(pageStats);
            }
        }

        pageStatsList = pageStatsByCountry;
    }

    public void renderVisitorsInfo(PageStats pageStats) {
        for (PageStats stats : pageStatsList) {
            //let only one visitors info at a time
            stats.setShowVisitorsInfo(false);
        }
        pageStats.setShowVisitorsInfo(true);
    }

    public void setPageStatsList(List<PageStats> pageStatsList) {
        this.pageStatsList = pageStatsList;
    }

    public List<PageStats> getFilteredStats() {
        return filteredStats;
    }

    public void setFilteredStats(List<PageStats> filteredStats) {
        this.filteredStats = filteredStats;
    }


}
