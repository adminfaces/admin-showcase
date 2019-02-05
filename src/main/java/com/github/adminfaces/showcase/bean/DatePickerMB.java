package com.github.adminfaces.showcase.bean;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class DatePickerMB implements Serializable {

	private Date date1;
    private Date date2;
    private Date date3;
    private Date date4;
    private Date date5;
    private Date date6;
    private Date date7;
    private Date date8;
    private Date date9;
    private Date date10;
    private Date date11;
    private Date date12;
    private Date date13;
    private Date date14;
    private List<Date> multi;
    private List<Date> range;
    private List<Date> invalidDates;
    private List<Integer> invalidDays;
    private Date minDate;
    private Date maxDate;
 
    @PostConstruct
    public void init() {
        invalidDates = new ArrayList<>();
        Date today = new Date();
        invalidDates.add(today);
        long oneDay = 24 * 60 * 60 * 1000;
        for (int i = 0; i < 5; i++) {
            invalidDates.add(new Date(invalidDates.get(i).getTime() + oneDay));
        }
 
        invalidDays = new ArrayList<>();
        invalidDays.add(0); /* the first day of week is disabled */
        invalidDays.add(3);
 
        minDate = new Date(today.getTime() - (365 * oneDay));
        maxDate = new Date(today.getTime() + (365 * oneDay));
    }
 
    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }
 
    public Date getDate1() {
        return date1;
    }
 
    public void setDate1(Date date1) {
        this.date1 = date1;
    }
 
    public Date getDate2() {
        return date2;
    }
 
    public void setDate2(Date date2) {
        this.date2 = date2;
    }
 
    public Date getDate3() {
        return date3;
    }
 
    public void setDate3(Date date3) {
        this.date3 = date3;
    }
 
    public Date getDate4() {
        return date4;
    }
 
    public void setDate4(Date date4) {
        this.date4 = date4;
    }
 
    public Date getDate5() {
        return date5;
    }
 
    public void setDate5(Date date5) {
        this.date5 = date5;
    }
 
    public Date getDate6() {
        return date6;
    }
 
    public void setDate6(Date date6) {
        this.date6 = date6;
    }
 
    public Date getDate7() {
        return date7;
    }
 
    public void setDate7(Date date7) {
        this.date7 = date7;
    }
 
    public Date getDate8() {
        return date8;
    }
 
    public void setDate8(Date date8) {
        this.date8 = date8;
    }
 
    public Date getDate9() {
        return date9;
    }
 
    public void setDate9(Date date9) {
        this.date9 = date9;
    }
 
    public Date getDate10() {
        return date10;
    }
 
    public void setDate10(Date date10) {
        this.date10 = date10;
    }
 
    public Date getDate11() {
        return date11;
    }
 
    public void setDate11(Date date11) {
        this.date11 = date11;
    }
 
    public Date getDate12() {
        return date12;
    }
 
    public void setDate12(Date date12) {
        this.date12 = date12;
    }
 
    public Date getDate13() {
        return date13;
    }
 
    public void setDate13(Date date13) {
        this.date13 = date13;
    }
 
    public Date getDate14() {
        return date14;
    }
 
    public void setDate14(Date date14) {
        this.date14 = date14;
    }
 
    public List<Date> getMulti() {
        return multi;
    }
 
    public void setMulti(List<Date> multi) {
        this.multi = multi;
    }
 
    public List<Date> getRange() {
        return range;
    }
 
    public void setRange(List<Date> range) {
        this.range = range;
    }
 
    public List<Date> getInvalidDates() {
        return invalidDates;
    }
 
    public void setInvalidDates(List<Date> invalidDates) {
        this.invalidDates = invalidDates;
    }
 
    public List<Integer> getInvalidDays() {
        return invalidDays;
    }
 
    public void setInvalidDays(List<Integer> invalidDays) {
        this.invalidDays = invalidDays;
    }
 
    public Date getMinDate() {
        return minDate;
    }
 
    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }
 
    public Date getMaxDate() {
        return maxDate;
    }
 
    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }
}
