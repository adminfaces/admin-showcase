package com.github.adminfaces.showcase.analytics.bean;

import com.github.adminfaces.showcase.analytics.model.PageStats;
import com.github.adminfaces.showcase.analytics.model.PageView;
import com.github.adminfaces.showcase.analytics.store.PageStatisticsStore;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Created by rmpestano on 01/05/17.
 */
@Named
@ViewScoped
public class AnalyticsMB implements Serializable {

    private String viewId;
    private PageStats pageStats;

    @Inject
    private PageStatisticsStore analyticsStore;

    @PostConstruct
    public void onPageVisited() {
        viewId = Faces.getViewId();
        ExternalContext ec = Faces.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        analyticsStore.addPageView(viewId,new PageView(request.getRemoteAddr()));
        pageStats = analyticsStore.getPageStats(viewId);
    }


    public PageStats getPageStats() {
        return pageStats;
    }
}
