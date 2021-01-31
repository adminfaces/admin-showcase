package com.github.adminfaces.showcase.bean;

/**
 * Created by rafael-pestano on 08/02/17.
 */

import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.*;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

@Named
@ViewScoped
public class ScheduleMB implements Serializable {

	private ScheduleModel eventModel;

	private ScheduleModel lazyEventModel;

	private ScheduleEvent event = new DefaultScheduleEvent();

    @PostConstruct
	public void init() {
		eventModel = new DefaultScheduleModel();
		eventModel.addEvent(new DefaultScheduleEvent<String>("Champions League Match", previousDay8Pm(), previousDay11Pm()));
		eventModel.addEvent(new DefaultScheduleEvent("Birthday Party", today1Pm(), today6Pm()));
		eventModel.addEvent(new DefaultScheduleEvent("Breakfast at Tiffanys", nextDay9Am(), nextDay11Am()));
		eventModel.addEvent(new DefaultScheduleEvent("Plant the new garden stuff", theDayAfter3Pm(), fourDaysLater3pm()));

		lazyEventModel = new LazyScheduleModel() {

			@Override
			public void loadEvents(LocalDateTime start, LocalDateTime end) {
				LocalDateTime random = getRandomDate(start);
				addEvent(new DefaultScheduleEvent("Lazy Event 1", random, random));

				random = getRandomDate(end);
				addEvent(new DefaultScheduleEvent("Lazy Event 2", random, random));
			}
		};
	}

	public LocalDateTime getRandomDate(LocalDateTime base) {
		base.withDayOfMonth(((int) (Math.random()*30)) + 1);	//set random day of month
		return base;
	}

	public Date getInitialDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);

		return calendar.getTime();
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public ScheduleModel getLazyEventModel() {
		return lazyEventModel;
	}

	private LocalDateTime today() {
		return LocalDateTime.now().withSecond(0).withMinute(0).withHour(0);
	}

	private LocalDateTime previousDay8Pm() {
		LocalDateTime yesterday8pm = LocalDate.now().minus(Period.ofDays(-1)).atTime(20, 0);
		return LocalDateTime.from(yesterday8pm);
	}

	private LocalDateTime previousDay11Pm() {
		LocalDateTime yesterday8pm = LocalDate.now().minus(Period.ofDays(-1)).atTime(23, 0);
		return LocalDateTime.from(yesterday8pm);
	}

	private LocalDateTime today1Pm() {
		return LocalDateTime.from(LocalDate.now().atTime(13, 0));
	}

	private LocalDateTime theDayAfter3Pm() {
		return LocalDate.now().plus(Period.ofDays(+1)).atTime(15, 0);
	}

	private LocalDateTime today6Pm() {
		return LocalDate.now().atTime(18, 0);
	}

	private LocalDateTime nextDay9Am() {
		return LocalDate.now().plus(Period.ofDays(+1)).atTime(21, 0);
	}

	private LocalDateTime nextDay11Am() {
		return LocalDate.now().plus(Period.ofDays(+1)).atTime(23, 0);
	}

	private LocalDateTime fourDaysLater3pm() {
		return LocalDate.now().plus(Period.ofDays(+4)).atTime(15, 0);
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public void addEvent(ActionEvent actionEvent) {
        String eventTypeMsg = null;
		if(event.getId() == null) {
		    eventTypeMsg = "created!";
            eventModel.addEvent(event);
        } else {
            eventTypeMsg = "updated!";
            eventModel.updateEvent(event);
        }
        Messages.create("Info").detail("Event <b>"+event.getTitle()+"</b> "+eventTypeMsg).add();
		event = new DefaultScheduleEvent();
	}

	public void onEventSelect(SelectEvent selectEvent) {
		event = (ScheduleEvent) selectEvent.getObject();
	}

	public void onDateSelect(SelectEvent selectEvent) {
		event = new DefaultScheduleEvent("", (LocalDateTime) selectEvent.getObject(), (LocalDateTime) selectEvent.getObject());
	}

	public void onEventMove(ScheduleEntryMoveEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

		addMessage(message);
	}

	public void onEventResize(ScheduleEntryResizeEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDeltaEnd() + ", Minute delta:" + event.getMinuteDeltaEnd());

		addMessage(message);
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}

