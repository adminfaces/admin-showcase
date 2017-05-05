package com.github.adminfaces.showcase.analytics.bean;

import com.github.adminfaces.showcase.analytics.model.PageStats;
import com.github.adminfaces.showcase.analytics.service.AnalyticsService;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
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
    private AnalyticsService analyticsService;

    @PostConstruct
    public void onPageVisited() {
        viewId = Faces.getViewId();
        pageStats = analyticsService.getPageStats(viewId);
    }


    public void pageViews() {

    }
}
