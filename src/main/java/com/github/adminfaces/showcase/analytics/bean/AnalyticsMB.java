package com.github.adminfaces.showcase.analytics.bean;

import com.github.adminfaces.showcase.analytics.model.PageStats;
import com.github.adminfaces.showcase.analytics.model.PageView;
import com.github.adminfaces.showcase.analytics.store.PageStatisticsStore;
import com.github.adminfaces.showcase.filter.BlackListFilter;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.github.adminfaces.template.util.Assert.has;

/**
 * Created by rmpestano on 01/05/17.
 */
@Named
@ViewScoped
public class AnalyticsMB implements Serializable {

    private static final List<String> PIE_CHART_COLORS = Arrays.asList("#f56954", "#00a65a", "#f39c12", "#00c0ef", "#3c8dbc", "#d2d6de", "#d81b60", "#01FF70", "#b50346",
            "#444", "#001F3F", "#B13C2E", "#009688", "#111", "#696969", "#0088cc", "#39CCCC", "#7FB77D", "#F012BE", "#3D9970", "#FF851B", "#1C28B7", "#FF495A", "#31FFB0",
            "#B1CC97", "#3F2A29", "#2F1B22", "gray","#8A0829","#4000FF","#01A9DB","#050563","#E3F25B","#D71A4C", "#C28503", "#660000","#86b300", "#b3b300","#ff4000"," #ff794d",
            "#b266ff","#006699","#02e3e6","#c35f3e","#1a1515");

    private static final SimpleDateFormat MONTH_YEAR_FORMAT = new SimpleDateFormat("MM/yyyy");

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
    private Calendar statisticsDate;
    private String monthYearSelection;

    @PostConstruct
    public void onPageVisited() {
        viewId = Faces.getViewId();
        HttpServletRequest request = Faces.getRequest();
        String browser = request.getHeader("User-Agent");
        if (!has(browser) || browser.contains("YandexBot")) {
            return;
        }
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (!has(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        if(BlackListFilter.isBlocked(ipAddress)) {
            return;
        }
        analyticsStore.addPageView(viewId, new PageView(ipAddress));
        pageStats = analyticsStore.getPageStats(viewId);
        statisticsDate = Calendar.getInstance();
        monthYearSelection = MONTH_YEAR_FORMAT.format(statisticsDate.getTime());

    }


    public PageStats getPageStats() {
        return pageStats;
    }

    public List<PageStats> getPageStatsList() {
        if (pageStatsList == null) {
           loadStatsList();
        }
        return pageStatsList;
    }

    public void initStatistics() {
        if(Faces.isAjaxRequest()) {
            return;
        }
        analyticsStore.initStatistics();

    }

    public void loadStatisticsFromBackup() {
        analyticsStore.loadStatisticsFromBackup();
        initStatistics();
        Messages.addInfo(null,"Page statistics loaded successful");
    }

    private void loadStatsList() {
        pageStatsList = analyticsStore.getPageStatsWithCountries(statisticsDate);
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

    public boolean hasPageViews() {
        boolean hasStats = pageStatsList != null && !pageStatsList.isEmpty();
        if(!hasStats) {
            return false;
        }

        boolean hasPageViews = false;
        for (PageStats stats : pageStatsList) {
            if(stats.getPageViews() != null && !stats.getPageViews().isEmpty()) {
                hasPageViews = true;
                break;
            }
        }
        return hasPageViews;
    }

    public void updateStatistics() throws ParseException {
        analyticsStore.resetStatstistics();
        statisticsDate = Calendar.getInstance();
        monthYearSelection = Faces.getRequestParameter("statsDate");
        if(monthYearSelection != null) {
            statisticsDate.setTime(MONTH_YEAR_FORMAT.parse(monthYearSelection));
        } else {
            statisticsDate = null;
        }
        totalVisitorsByMonth = null;
        uniqueVisitorsByMonth = null;
        visitorsByCountry = null;
        visitorsByPage = null;
        pageViewsGeoJson = null;
        loadStatsList();
    }

    public void clearFilter() {
        initStatistics();
    }

    public List<String> completeCountry(String query) {
        List<String> results = new ArrayList<>();

        if (has(query) && query.length() >= 2) {
            List<String> pageViewCountries = analyticsStore.getPageViewCountries(statisticsDate);
            for (String pageViewCountry : pageViewCountries) {
                if (pageViewCountry.toLowerCase().contains(query.toLowerCase())) {
                    results.add(pageViewCountry);
                }
            }
        }

        return results;
    }

    public void onCountrySelect(SelectEvent event) {
        initStatistics();
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
                Integer totalViewsInMonth = analyticsStore.getTotalVisitorsByMonth(statisticsDate).get(i);
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
                Integer totalViewsInMonth = analyticsStore.getUniqueVisitorsByMonth(statisticsDate).get(i);
                totals.add(totalViewsInMonth);
            }
            uniqueVisitorsByMonth = totals.build().toString();
        }
        return uniqueVisitorsByMonth;
    }

    public String getVisitorsByPage() {
        if (visitorsByPage == null && pageStatsList != null) {
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
            int size = sortedMap.length > 10 ? 10 : sortedMap.length;
            for (int i = 0; i < size; i++) {
                Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) sortedMap[i];
                labels.add(entry.getKey());
                data.add(entry.getValue());

            }
            JsonObjectBuilder dataset = Json.createObjectBuilder()
                    .add("fillCollor", "rgba(151,187,205,0.5)")
                    .add("strokeCollor", "rgba(151,187,205,1)")
                    .add("data", data);
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
        if (pageViewsGeoJson == null) {
            pageViewsGeoJson = analyticsStore.getGeoJsonCache(statisticsDate);
        }
        return pageViewsGeoJson;
    }


    public Calendar getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(Calendar statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public Integer getInitialStatisticsYear() {
        List<Integer> yearsWithStatistics = analyticsStore.getYearsWithStatistics();
        if(yearsWithStatistics == null || yearsWithStatistics.isEmpty()) {
            return Calendar.getInstance().get(Calendar.YEAR);
        }
        return yearsWithStatistics.get(0);
    }

    public String getMonthYearSelection() {
        return monthYearSelection;
    }

    public void setMonthYearSelection(String monthYearSelection) {
        this.monthYearSelection = monthYearSelection;
    }
}
