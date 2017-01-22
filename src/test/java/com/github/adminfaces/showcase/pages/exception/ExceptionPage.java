package com.github.adminfaces.showcase.pages.exception;

import com.github.adminfaces.showcase.pages.BasePage;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by rafael-pestano on 16/01/17.
 */
@Location("pages/exception/exception.xhtml")
public class ExceptionPage extends BasePage {

    @FindByJQuery("button.btn-danger")
    protected WebElement btnRuntime;

    @FindByJQuery("button.bg-dark-red")
    protected WebElement btnViewExpired;

    @FindByJQuery("button.btn-warning")
    protected WebElement btn404;


    public void clickRuntimeButton() {
        if (isPhantomjs()) {
            guardHttp(btnRuntime).click();
        } else {
            guardAjax(btnRuntime).click();
        }

    }

    public void clickViewExpiredButton() {
        if (isPhantomjs()) {
            guardHttp(btnViewExpired).click();
        } else {
            guardAjax(btnViewExpired).click();
        }

    }

    public void click404Button() {
        guardHttp(btn404).click();
    }
}
