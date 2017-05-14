package com.github.adminfaces.showcase.analytics.bean;

import com.github.adminfaces.showcase.analytics.model.PageStats;
import com.github.adminfaces.showcase.analytics.model.PageView;
import com.github.adminfaces.showcase.analytics.store.PageStatisticsStore;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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

    private static final List<String> PIE_CHART_COLORS = Arrays.asList("#f56954", "#00a65a", "#f39c12", "#00c0ef", "#3c8dbc", "#d2d6de", "#d81b60", "#01FF70",
            "#444", "#001F3F", "#B13C2E", "#009688", "#111", "#696969", "#0088cc", "#39CCCC", "#7FB77D", "#F012BE", "#3D9970", "#FF851B", "#1C28B7", "#FF495A", "#31FFB0",
            "#B1CC97", "#3F2A29");

    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private String viewId;
    private PageStats pageStats;
    private List<PageStats> pageStatsList;
    private List<PageStats> filteredStats;

    @Inject
    private PageStatisticsStore analyticsStore;
    private String totalVisitorsByMonth;
    private String uniqueVisitorsByMonth;
    private String visitorsByPage;
    private String visitorsByCountry;
    private String pageViewsGeoJson;

    @PostConstruct
    public void onPageVisited() {
        viewId = Faces.getViewId();
        HttpServletRequest request = Faces.getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (!has(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        analyticsStore.addPageView(viewId, new PageView(ipAddress));
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

    public String getTotalVisitorsByMonth() {
        if (totalVisitorsByMonth == null) {
            JsonArrayBuilder totals = Json.createArrayBuilder();
            for (int i = 0; i <= 11; i++) {
                Integer totalViewsInMonth = analyticsStore.getTotalVisitorsByMonth().get(i);
                totals.add(totalViewsInMonth);
            }
            totalVisitorsByMonth = totals.build().toString();
        }
        return totalVisitorsByMonth;
    }

    public String getUniqueVisitorsByMonth() {
        if (uniqueVisitorsByMonth == null) {
            JsonArrayBuilder totals = Json.createArrayBuilder();
            for (int i = 0; i <= 11; i++) {
                Integer totalViewsInMonth = analyticsStore.getUniqueVisitorsByMonth().get(i);
                totals.add(totalViewsInMonth);
            }
            uniqueVisitorsByMonth = totals.build().toString();
        }
        return uniqueVisitorsByMonth;
    }

    public String getVisitorsByPage() {
        if (visitorsByPage == null) {
            JsonArrayBuilder pageStatsJsonArray = Json.createArrayBuilder();
            for (int i = 0; i < pageStatsList.size(); i++) {
                //each stats is a page
                String color = PIE_CHART_COLORS.get(i);
                PageStats stats = pageStatsList.get(i);
                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                jsonObjectBuilder.add("value", stats.getTotalPageViews())
                        .add("color", color)
                        .add("highlight", color)
                        .add("label", stats.getViewId());
                pageStatsJsonArray.add(jsonObjectBuilder);
            }

            visitorsByPage = pageStatsJsonArray.build().toString();
        }
        return visitorsByPage;
    }

    /**
     * var data = {
     * labels : ["countryName","countryName","countryName","countryName","countryName","countryName","countryName"],
     * datasets : [
     * {
     * fillColor : "rgba(220,220,220,0.5)",
     * strokeColor : "rgba(220,220,220,1)",
     * data : [65,59,90,81,56,55,40]//page views
     * }
     * ]
     * }
     */
    public String getVisitorsByCountry() {
        if (visitorsByCountry == null) {

            Object[] sortedMap = analyticsStore.getTotalVisitorsByCountry().entrySet().toArray();
            //sort map by highest values
            Arrays.sort(sortedMap, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((Map.Entry<String, Integer>) o2).getValue()
                            .compareTo(((Map.Entry<String, Integer>) o1).getValue());
                }
            });
            //get the first 10 contries
            JsonArrayBuilder labels = Json.createArrayBuilder();
            JsonArrayBuilder data = Json.createArrayBuilder();
            for (int i = 0; i < 10; i++) {
                Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) sortedMap[i];
                labels.add(entry.getKey());
                data.add(entry.getValue());

            }
            JsonObjectBuilder dataset = Json.createObjectBuilder()
                    .add("fillCollor", "rgba(151,187,205,0.5)")
                    .add("strokeCollor", "rgba(151,187,205,1)")
                    .add("data",data);
            JsonArrayBuilder datasets = Json.createArrayBuilder()
                    .add(dataset);
            JsonObject visitorsByCountryjsonObject = Json.createObjectBuilder()
                    .add("labels", labels)
                    .add("datasets", datasets).build();

                visitorsByCountry = visitorsByCountryjsonObject.toString();
        }


        return visitorsByCountry;
    }

    /**
     * Creates a GeoJson feature layer to be presented in a leaflet web map
     */
    public String getPageViewsGeoJson() {
        if(pageViewsGeoJson == null) {
            JsonArrayBuilder geoJsonLayer = Json.createArrayBuilder();
            for (PageStats stats : analyticsStore.allPageStats()) {
                for (PageView pageView : stats.getPageViews()) {
                    if(!has(pageView.getCountry()) || !has(pageView.getLat())){
                        continue;
                    }
                    JsonObjectBuilder geoJson = Json.createObjectBuilder()
                            .add("type","Feature");
                    JsonObjectBuilder geometry = Json.createObjectBuilder()
                            .add("type","Point")
                            .add("coordinates",Json.createArrayBuilder()
                                    .add(new Double(pageView.getLon()))
                                    .add(new Double(pageView.getLat())));
                    geoJson.add("geometry",geometry);
                    JsonObjectBuilder properties = Json.createObjectBuilder()
                            .add("country",pageView.getCountry())
                            .add("city",pageView.getCity())
                            .add("page",stats.getViewId())
                            .add("date",sdf.format(pageView.getDate().getTime()));
                    geoJson.add("properties",properties);
                    geoJsonLayer.add(geoJson);
                }
            }
            pageViewsGeoJson = geoJsonLayer.build().toString();
        }

        return pageViewsGeoJson;
    }
}
