package com.github.adminfaces.showcase.analytics.store;

import com.github.adminfaces.showcase.analytics.model.PageStats;
import com.github.adminfaces.showcase.analytics.model.PageView;
import com.github.adminfaces.showcase.analytics.model.PageViewCountry;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.json.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


import static com.github.adminfaces.template.util.Assert.has;

/**
 * Created by rmpestano on 01/05/17.
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class PageStatisticsStore implements Serializable {

    private Map<String, PageStats> pageStatisticsMap; //viewId by statistics map
    private static final Logger log = LoggerFactory.getLogger(PageStatisticsStore.class.getName());
    private final String pagesStatsFilePath = (System.getenv("OPENSHIFT_DATA_DIR") != null ? System.getenv("OPENSHIFT_DATA_DIR") : System.getProperty("user.home")) + "/page-stats.json".replaceAll("//", "/");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private List<String> pageViewCountries;
    private Map<Integer, Integer> totalVisitorsByMonth;//key is month and value is total
    private Map<Integer, Integer> uniqueVisitorsByMonth;//key is month and value is total
    private Map<String, Integer> totalVisitorsByCountry;


    @PostConstruct
    public void initStatistics() {
        pageStatisticsMap = new ConcurrentHashMap<>();
        log.info("Using {} as page statistics file store.", pagesStatsFilePath);
        try {
            File statisticsFile = new File(pagesStatsFilePath);
            if (!statisticsFile.exists()) {
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
                    if (object == null || object.get("ip") == null || !viewedInCurrentYear(object.getString("date"))) {
                        continue;
                    }
                    PageView pageView = new PageView(object.getString("ip"));
                    Calendar c = Calendar.getInstance();
                    c.setTime(dateFormat.parse(object.getString("date")));
                    pageView.setDate(c);
                    pageView.setCountry(object.containsKey("country") ? object.getString("country") : "");//backward compat
                    pageView.setCity(object.containsKey("city") ? object.getString("city") : "");//backward compat
                    pageView.setLat(object.containsKey("lat") ? object.getString("lat") : "");//backward compat
                    pageView.setLon(object.containsKey("lon") ? object.getString("lon") : "");//backward compat
                    pageView.setHasIpInfo(object.containsKey("hasIpInfo") ? object.getBoolean("hasIpInfo") : false);//backward compat
                    pageViews.add(pageView);
                }
                pageStats.setPageViews(pageViews);
                pageStatisticsMap.put(pageStats.getViewId(), pageStats);
            }
        } catch (Exception e) {
            log.warn("Could not load page statistics", e);
        } finally {
            log.info("Finished reading page statistics store.");
        }
    }

    private boolean viewedInCurrentYear(String date) {
        if (!has(date)) {
            return false;
        }
        try {
            Calendar viewedOn = Calendar.getInstance();
            viewedOn.setTime(dateFormat.parse(date));
            return viewedOn.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR);
        } catch (ParseException e) {
            log.warn("Could not parse page view date {}", date);
            return false;
        }
    }


    public PageStats getPageStats(String viewId) {
        return pageStatisticsMap.get(viewId);
    }

    public void addPageView(String viewId, PageView pageView) {
        PageStats pageStats = pageStatisticsMap.get(viewId);
        if (pageStats == null) {
            pageStats = new PageStats(viewId);
            pageStatisticsMap.put(viewId, pageStats);
        }
        pageStats.addPageView(pageView);
    }

    @Schedule(hour = "*/1", persistent = false)
    public void persistPageStatistics() {
        if (pageStatisticsMap == null || pageStatisticsMap.isEmpty()) {
            return;//in some situation the schedule is called before statistics is initialized
        }
        long initial = System.currentTimeMillis();

        List<PageStats> pageStatsCopy = null;
        synchronized (pageStatisticsMap) {
            List<PageStats> originalList = new ArrayList<>(pageStatisticsMap.values());
            pageStatsCopy = copyPageStats(originalList);
        }
        int numRecordsUpdated = 0;
        try {
            JsonArrayBuilder pageStatsJsonArray = Json.createArrayBuilder();
            for (PageStats pageStats : pageStatsCopy) {
                JsonArrayBuilder pageViewsJsonArray = Json.createArrayBuilder();
                Iterator<PageView> pageViewIterator = pageStats.getPageViews().iterator();//iterator to avoid concurrent modification exc
                while (pageViewIterator.hasNext()) {
                    PageView pageView = pageViewIterator.next();
                    if (!has(pageView.getIp())) {
                        continue;
                    }

                    boolean infoUpdated = queryAdditionalPageViewInfo(pageView);
                    if (infoUpdated) {
                        numRecordsUpdated++;
                    }
                    JsonObject pageViewJsonObject = Json.createObjectBuilder()
                            .add("ip", pageView.getIp())
                            .add("date", dateFormat.format(pageView.getDate().getTime()))
                            .add("country", pageView.getCountry() != null ? pageView.getCountry() : "")
                            .add("city", pageView.getCity() != null ? pageView.getCity() : "")
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
            loadPageViewCountries();
            resetStatstistics();
        } catch (Exception e) {
            log.error("Could not persist statistics in path " + pagesStatsFilePath, e);
        } finally {
            log.info("{} page statistics updated in {} seconds.", numRecordsUpdated, (System.currentTimeMillis() - initial) / 1000.0d);
        }
    }

    private List<PageStats> copyPageStats(List<PageStats> originalList) {
         List<PageStats> pageStatsCopy = new ArrayList<>(originalList.size());
            for (PageStats stats : originalList) {
                PageStats pageStats = new PageStats(stats.getViewId());
                pageStats.setPageViews(new ArrayList<PageView>());
                for (PageView originalView : stats.getPageViews()) {
                    PageView pageViewCopy = new PageView(originalView.getIp());
                    pageViewCopy.setCity(originalView.getCity());
                    pageViewCopy.setCountry(originalView.getCountry());
                    pageViewCopy.setDate(originalView.getDate());
                    pageViewCopy.setHasIpInfo(originalView.getHasIpInfo());
                    pageViewCopy.setIp(originalView.getIp());
                    pageViewCopy.setLat(originalView.getLat());
                    pageViewCopy.setLon(originalView.getLon());
                    pageStats.addPageView(pageViewCopy);
                }
                pageStatsCopy.add(pageStats);
            }

            return pageStatsCopy;
    }

    //force statistics reload
    private void resetStatstistics() {
        totalVisitorsByCountry = null;
        totalVisitorsByMonth = null;
        uniqueVisitorsByMonth = null;
    }

    private void loadPageViewCountries() {
        if (pageViewCountries == null) {
            pageViewCountries = new ArrayList<>();
        }
        for (PageStats pageStats : pageStatisticsMap.values()) {
            for (PageView pageView : pageStats.getPageViews()) {
                if (has(pageView.getCountry()) && !pageViewCountries.contains(pageView.getCountry())) {
                    pageViewCountries.add(pageView.getCountry());
                }
            }
        }
    }

    /**
     * @param pageView
     * @return boolean representing the info was updated
     */
    private boolean queryAdditionalPageViewInfo(PageView pageView) {
        if (pageView.getHasIpInfo() || pageView.getIp().equals("127.0.0.1") || pageView.getIp().contains("localhost")) {
            return false;
        }
        StringBuilder ipApiQuery = new StringBuilder("http://ip-api.com/json/");
        boolean result = false;
        if (!pageView.getIp().contains(",")) {//only one ip returned
            ipApiQuery.append(pageView.getIp());
            result = callIpApi(ipApiQuery.toString(), pageView);
        } else { //multiple ips
            String[] ips = ipApiQuery.toString().split(",");
            for (String ip : ips) {
                result = callIpApi(ip.toString().replace(":",""), pageView);
                if (result) {
                    pageView.setIp(ip);
                    break;
                }
            }
        }
        return result;
    }

    private boolean callIpApi(String ipApiQuery, PageView pageView) {

        HttpURLConnection connection = null;//using url connection to avoid (JavaEE 6) JAX-RS client api conflicts
        BufferedReader rd = null;
        try {
            URL url = new URL(ipApiQuery);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type",
                    "application/json");

            connection.setRequestProperty("Accept-Charset", "UTF-8");

            InputStream is = connection.getInputStream();
            rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                json.append(line);
            }
            JsonObject jsonObject = Json.createReader(new StringReader(json.toString())).readObject();
            if (jsonObject.containsKey("status") && !jsonObject.getString("status").equals("fail")) {
                pageView.setCountry(jsonObject.getString("country"));
                pageView.setCity(jsonObject.getString("city"));
                pageView.setLat(jsonObject.getJsonNumber("lat").toString());
                pageView.setLon(jsonObject.getJsonNumber("lon").toString());
                pageView.setHasIpInfo(true);
                Thread.sleep(250);//sleep to not exceed query limits (150 per minute)
                return true;
            }

        } catch (Exception e) {
            log.error("Could not get additional info from IP API request:" + ipApiQuery, e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                    log.error("Problem closing buffered reader", e);
                }
            }
        }
        return false;
    }


    public List<PageStats> allPageStats() {
        return new ArrayList<>(pageStatisticsMap.values());
    }

    public List<String> getPageViewCountries() {
        if (pageViewCountries == null) {
            loadPageViewCountries();
        }
        return pageViewCountries;
    }

    public List<PageStats> getPageStatsWithCountries() {
        List<PageStats> pageStatsWithCountries = new ArrayList<>();
        for (PageStats stats : allPageStats()) {
            PageStats pageStats = new PageStats(stats.getViewId());
            List<PageView> pageViews = new ArrayList<>();
            for (PageView pageView : stats.getPageViews()) {
                if (has(pageView.getCountry())) {
                    pageViews.add(pageView);
                }
            }
            pageStats.setPageViews(pageViews);
            pageStatsWithCountries.add(pageStats);
        }
        return pageStatsWithCountries;
    }

    public Map<Integer, Integer> getTotalVisitorsByMonth() {
        if (totalVisitorsByMonth == null) {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            totalVisitorsByMonth = new HashMap<>();
            for (int i = 0; i <= 11; i++) {
                totalVisitorsByMonth.put(i, 0);
            }
            for (PageStats pageStats : pageStatisticsMap.values()) {
                for (PageView pageView : pageStats.getPageViews()) {
                    if (pageView.getDate().get(Calendar.YEAR) != currentYear || !has(pageView.getIp())) {
                        continue;
                    }
                    int pageViewMonth = pageView.getDate().get(Calendar.MONTH);
                    totalVisitorsByMonth.put(pageViewMonth, totalVisitorsByMonth.get(pageViewMonth) + 1);
                }
            }
        }

        return totalVisitorsByMonth;
    }

    public Map<Integer, Integer> getUniqueVisitorsByMonth() {
        if (uniqueVisitorsByMonth == null) {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            List<String> ipList = new ArrayList<>();
            uniqueVisitorsByMonth = new HashMap<>();
            for (int i = 0; i <= 11; i++) {
                uniqueVisitorsByMonth.put(i, 0);
            }
            for (PageStats pageStats : pageStatisticsMap.values()) {
                for (PageView pageView : pageStats.getPageViews()) {
                    if (pageView.getDate().get(Calendar.YEAR) != currentYear || !has(pageView.getIp()) || ipList.contains(pageView.getIp())) {
                        continue;
                    }
                    int pageViewMonth = pageView.getDate().get(Calendar.MONTH);
                    ipList.add(pageView.getIp());
                    uniqueVisitorsByMonth.put(pageViewMonth, uniqueVisitorsByMonth.get(pageViewMonth) + 1);
                }
            }
        }
        return uniqueVisitorsByMonth;
    }

    public Map<String, Integer> getTotalVisitorsByCountry() {
        if (totalVisitorsByCountry == null) {
            totalVisitorsByCountry = new HashMap<>();
            for (PageStats pageStats : pageStatisticsMap.values()) {
                List<PageViewCountry> pageViewCountryList = pageStats.getPageViewCountryList();
                for (PageViewCountry pageViewCountry : pageViewCountryList) {
                    String country = pageViewCountry.getCountry();
                    if (!totalVisitorsByCountry.containsKey(country)) {
                        totalVisitorsByCountry.put(country, 0);
                    }
                    totalVisitorsByCountry.put(country, totalVisitorsByCountry.get(country) + pageViewCountry.getViewCount());
                }
            }
        }
        return totalVisitorsByCountry;
    }
}
