package com.github.adminfaces.showcase.analytics.store;

import com.github.adminfaces.showcase.analytics.model.PageStats;
import com.github.adminfaces.showcase.analytics.model.PageView;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.json.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.adminfaces.template.util.Assert.has;

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
        log.info("Using {} as page statistics file store.", pagesStatsFilePath);
        try {
            File statisticsFile = new File(pagesStatsFilePath);
            if(!statisticsFile.exists()) {
                statisticsFile.createNewFile();
            }
            JsonArray persistedPageStats = Json.createReader(new FileReader(statisticsFile)).readObject().getJsonArray("statistics");
            for (JsonValue jsonValue : persistedPageStats) {
                JsonObject jsonObject = (JsonObject) jsonValue;
                PageStats pageStats = new PageStats(jsonObject.getString("viewId"));
                JsonArray pageViewsJson = jsonObject.getJsonArray("pageViews");
                List<PageView> pageViews = new ArrayList<>();
                for (JsonValue value : pageViewsJson) {
                    JsonObject object = (JsonObject) value;
                    if(object == null || object.get("ip") == null){
                        continue;
                    }
                    PageView pageView = new PageView(object.getString("ip"));
                    queryAdditionalPageViewInfo(pageView);
                    Calendar c = Calendar.getInstance();
                    c.setTime(dateFormat.parse(object.getString("date")));
                    pageView.setDate(c);
                    pageView.setCountry(object.getString("country"));
                    pageView.setLat(object.getString("lat"));
                    pageView.setLon(object.getString("lon"));
                    if(object.containsKey("hasIpInfo")) { //backward compat
                        pageView.setHasIpInfo(object.getBoolean("hasIpInfo"));
                    } else {
                        pageView.setHasIpInfo(false);
                    }
                    pageViews.add(pageView);
                }
                pageStats.setPageViews(pageViews);
                pageStatisticsMap.put(pageStats.getViewId(), pageStats);
            }
        } catch (Exception e) {
            log.warn("Could not load page statistics", e);
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

    @Schedule(hour = "*/1" , persistent = false)
    public void persistPageStatistics() {
        if(pageStatisticsMap == null || pageStatisticsMap.isEmpty()) {
            return;//in some situation the schedule is called before statistics is initialized
        }
        long initial = System.currentTimeMillis();
        try {
            JsonArrayBuilder pageStatsJsonArray = Json.createArrayBuilder();
            for (PageStats pageStats : pageStatisticsMap.values()) {
                JsonArrayBuilder pageViewsJsonArray = Json.createArrayBuilder();
                for (PageView pageView : pageStats.getPageViews()) {
                    if(!has(pageView.getIp())){
                        continue;
                    }
                    queryAdditionalPageViewInfo(pageView);
                    JsonObject pageViewJsonObject = Json.createObjectBuilder()
                            .add("ip", pageView.getIp())
                            .add("date", dateFormat.format(pageView.getDate().getTime()))
                            .add("country", pageView.getCountry() != null ? pageView.getCountry() : "")
                            .add("city",pageView.getCity() != null ? pageView.getCity(): "")
                            .add("lat", pageView.getLat() != null ? pageView.getLat() : "")
                            .add("lon", pageView.getLon() != null ? pageView.getLon() : "")
                            .add("hasIpInfo", pageView.getHasIpInfo()).build();


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

    private void queryAdditionalPageViewInfo(PageView pageView) {
        if(pageView.getHasIpInfo() || pageView.getIp().equals("127.0.0.1") || pageView.getIp().contains("localhost")) {
            return;
        }
        String ipApiQuery = new StringBuilder("http://ip-api.com/json/")
                .append(pageView.getIp()).toString();
        HttpURLConnection connection = null;//using url connection to be able to avoid (JavaEE 6) servers conflict
        BufferedReader rd = null;
        try {
            URL url = new URL(ipApiQuery);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type",
                    "application/json");

            connection.setRequestProperty("Accept-Charset", "UTF-8");

            InputStream is = connection.getInputStream();
            rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder json = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                json.append(line);
            }
            JsonObject jsonObject = Json.createReader(new StringReader(json.toString())).readObject();
            pageView.setCountry(jsonObject.getString("country"));
            pageView.setCity(jsonObject.getString("city"));
            pageView.setLat(jsonObject.getJsonNumber("lat").toString());
            pageView.setLon(jsonObject.getJsonNumber("lon").toString());
            pageView.setHasIpInfo(true);
        } catch (Exception e) {
            log.error("Could not get additional info from IP API request:"+ipApiQuery,e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if(rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                    log.error("Problem closing buffered reader",e);
                }
            }
        }
    }

}
