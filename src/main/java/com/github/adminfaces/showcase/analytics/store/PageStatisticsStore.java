package com.github.adminfaces.showcase.analytics.store;

import com.github.adminfaces.showcase.analytics.model.PageStats;
import com.github.adminfaces.showcase.analytics.model.PageView;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.json.*;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rmpestano on 01/05/17.
 */
@Singleton
@Startup
public class PageStatisticsStore implements Serializable {

    private Map<String, PageStats> pageStatisticsMap; //viewId by statistics map
    private static final Logger log = LoggerFactory.getLogger(PageStatisticsStore.class.getName());
    private final String pagesStatsFilePath = (System.getenv("OPENSHIFT_DATA_DIR") != null ? System.getenv("OPENSHIFT_DATA_DIR") : System.getProperty("user.home")) + "/page-stats.json".replaceAll("//", "/");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @PostConstruct
    public void initStatistics() {
        pageStatisticsMap = new ConcurrentHashMap<>();
        log.info("Using {} as page statistics folder store.", pagesStatsFilePath);
        try {
            File statisticsFile = new File(pagesStatsFilePath);
            if(!statisticsFile.exists()) {
                statisticsFile.createNewFile();
            }
            log.info("Loading page statistics from " + pagesStatsFilePath);
            JsonArray persistedPageStats = Json.createReader(new FileReader(statisticsFile)).readObject().getJsonArray("statistics");
            for (JsonValue jsonValue : persistedPageStats) {
                JsonObject jsonObject = (JsonObject) jsonValue;
                PageStats pageStats = new PageStats(jsonObject.getString("viewId"));
                JsonArray pageViewsJson = jsonObject.getJsonArray("pageViews");
                List<PageView> pageViews = new ArrayList<>();
                for (JsonValue value : pageViewsJson) {
                    JsonObject object = (JsonObject) value;
                    PageView pageView = new PageView(object.getString("ip"));
                    Calendar c = Calendar.getInstance();
                    c.setTime(dateFormat.parse(object.getString("date")));
                    pageView.setDate(c);
                    pageView.setCountry(object.getString("country"));
                    pageView.setLat(object.getString("lat"));
                    pageView.setLon(object.getString("lon"));
                    pageViews.add(pageView);
                }
                pageStats.setPageViews(pageViews);
                pageStatisticsMap.put(pageStats.getViewId(), pageStats);
            }
        } catch (Exception e) {
            log.error("Could not load page statistics", e);
        }
    }


    public PageStats getPageStats(String viewId) {
        return pageStatisticsMap.get(viewId);
    }

    public synchronized void addPageView(String viewId, PageView pageView) {
        PageStats pageStats = pageStatisticsMap.get(viewId);
        if (pageStats == null) {
            pageStats = new PageStats(viewId);
            pageStatisticsMap.put(viewId, pageStats);
        }
        pageStats.addPageView(pageView);
    }

    @Schedule(hour="*/2" , persistent = false)
    public void persistPageStatistics() {
        if(pageStatisticsMap == null) {
            return;//in some situation the schedule is called before post construct
        }
        long initial = System.currentTimeMillis();
        try {
            JsonArrayBuilder pageStatsJsonArray = Json.createArrayBuilder();
            for (PageStats pageStats : pageStatisticsMap.values()) {
                JsonArrayBuilder pageViewsJsonArray = Json.createArrayBuilder();
                for (PageView pageView : pageStats.getPageViews()) {
                    JsonObject pageViewJsonObject = Json.createObjectBuilder()
                            .add("ip", pageView.getIp())
                            .add("date", dateFormat.format(pageView.getDate().getTime()))
                            .add("country", pageView.getCountry() != null ? pageView.getCountry() : "")
                            .add("lat", pageView.getLat() != null ? pageView.getLat() : "")
                            .add("lon", pageView.getLon() != null ? pageView.getLon() : "").build();
                    pageViewsJsonArray.add(pageViewJsonObject);
                }
                JsonObject pageStatsJson = Json.createObjectBuilder()
                        .add("viewId", pageStats.getViewId())
                        .add("pageViews", pageViewsJsonArray.build()).build();
                pageStatsJsonArray.add(pageStatsJson);
            }
            FileUtils.writeStringToFile(new File(pagesStatsFilePath), Json.createObjectBuilder().add("statistics", pageStatsJsonArray.build()).build().toString(), "UTF-8");
        } catch (Exception e) {
            log.error("Could not persist statistics in path " + pagesStatsFilePath, e);
        } finally {
            log.info("Time to persist page statistics: {} seconds.", (System.currentTimeMillis() - initial) / 1000.0d);
        }

    }

    @PreDestroy
    public void onShutDown() {
        persistPageStatistics();
    }
}
