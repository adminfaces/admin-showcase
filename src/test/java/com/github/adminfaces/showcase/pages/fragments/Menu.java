package com.github.adminfaces.showcase.pages.fragments;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

/**
 * Created by rafael-pestano on 16/01/17.
 */
public class Menu {

    @Root
    private GrapheneElement menu;

    @Drone
    protected WebDriver browser;

    @FindByJQuery("li a[href$='index.xhtml']")
    private GrapheneElement home;

    @FindByJQuery("li a[href$='exception.xhtml']")
    private GrapheneElement exception;

    @FindByJQuery("li a[href$='forms.xhtml']")
    private GrapheneElement forms;

    @FindByJQuery("li a[href$='datatable.xhtml']")
    private GrapheneElement datatable;

    @FindByJQuery("li a[href$='panel.xhtml']")
    private GrapheneElement panel;

    @FindByJQuery("li a[href$='buttons.xhtml']")
    private GrapheneElement buttons;

    @FindByJQuery("li a[href$='dialog.xhtml']")
    private GrapheneElement dialog;

    @FindBy(xpath = "//SPAN[text()='UI Elements']")
    protected WebElement uiElementsMenu;

    public void goToHomePage(){
        guardHttp(home).click();
    }

    public void goToExceptionPage(){
        guardHttp(exception).click();
    }

    public void goToDatatablePage(){
        uiElementsMenu.click();
        waitModel().until().element(datatable).is().visible();
        guardHttp(datatable).click();
    }

    public void goToPanelPage(){
        guardHttp(panel).click();
    }

    public void goToButtonsPage(){
        guardHttp(buttons).click();
    }

    public void goToDialogPage() {
        uiElementsMenu.click();
        waitModel().until().element(dialog).is().visible();
        guardHttp(dialog).click();
    }


    public GrapheneElement getHome() {
        return home;
    }

    public GrapheneElement getException() {
        return exception;
    }

    public GrapheneElement getForms() {
        return forms;
    }
}
