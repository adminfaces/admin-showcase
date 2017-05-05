package com.github.adminfaces.showcase.analytics.service;

import com.github.adminfaces.showcase.analytics.model.PageStats;
import com.github.adminfaces.showcase.analytics.store.AnalyticsStore;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created by rmpestano on 01/05/17.
 */
@Stateless
public class AnalyticsService {

    @Inject
    AnalyticsStore analyticsStore;


    public PageStats getPageStats(String viewId) {

    }


}
