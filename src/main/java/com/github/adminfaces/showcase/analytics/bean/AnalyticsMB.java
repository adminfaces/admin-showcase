package com.github.adminfaces.showcase.analytics.bean;

import com.github.adminfaces.showcase.analytics.model.PageStats;
import com.github.adminfaces.showcase.analytics.model.PageView;
import com.github.adminfaces.showcase.analytics.store.PageStatisticsStore;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.primefaces.model.LazyDataModel;

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
        pageStatsList = analyticsStore.allPageStats();
            for (PageStats stats : analyticsStore.allPageStats()) {
                  Iterator<PageView> pageViewIterator = stats.getPageViews().iterator();
                  while(pageViewIterator.hasNext()) {
                      PageView pageView = pageViewIterator.next();
                      if(!has(pageView.getCountry())) {
                          pageViewIterator.remove();
                      }
                  }
                  Collections.sort(stats.getPageViews(), new Comparator<PageView>() {
                            @Override
                            public int compare(PageView pageView1, PageView pageView2) {
                                return pageView1.getCountry() != null && pageView2.getCountry() != null ? pageView1.getCountry().compareTo(pageView2.getCountry()):-1;
                            }
                 });
                stats.setShowVisitorsInfo(false);
            }

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
