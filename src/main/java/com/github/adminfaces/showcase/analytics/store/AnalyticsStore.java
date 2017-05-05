package com.github.adminfaces.showcase.analytics.store;

import com.github.adminfaces.showcase.analytics.model.PageStats;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by rmpestano on 01/05/17.
 */
@Singleton
@Startup
public class AnalyticsStore implements Serializable {

    private Map<String, PageStats> analyticsMap;//viewId by statistics map

    @PostConstruct
    public void initStore() {
        analyticsMap = loadOrCreateStore();
    }

    private Map<String, PageStats> loadOrCreateStore() {

    }
}
