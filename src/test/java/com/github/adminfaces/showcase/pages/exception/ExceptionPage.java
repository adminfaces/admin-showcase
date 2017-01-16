package com.github.adminfaces.showcase.pages.exception;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by rafael-pestano on 16/01/17.
 */
@Location("pages/exception/exception.xhtml")
public class ExceptionPage {

    @FindByJQuery("section.content-header > h1")
    protected WebElement title;


    @FindByJQuery("button.btn-danger")
    protected WebElement btnRuntime;

    @FindByJQuery("button.bg-dark-red")
    protected WebElement btnViewExpired;

    @FindByJQuery("button.btn-warning")
    protected WebElement btn404;



    public WebElement getTitle() {
        return title;
    }

    public void clickRuntimeButton(){
        guardHttp(btnRuntime).click();
    }

    public void clickViewExpiredButton(){
        guardHttp(btnViewExpired).click();
    }

    public void click404Button(){
        guardHttp(btn404).click();
    }
}
