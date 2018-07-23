package com.github.adminfaces.showcase.pages;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by rmpestano on 16/01/17.
 */
public class BasePage {

    @Drone
    protected WebDriver browser;

    @FindByJQuery("section.content-header > h1")
    protected WebElement title;

    @FindByJQuery("div.ui-growl-message")
    protected GrapheneElement growl;

    public WebElement getTitle() {
        return title;
    }

    public String getGrowlMsg() {
        return growl.getText();
    }

}
