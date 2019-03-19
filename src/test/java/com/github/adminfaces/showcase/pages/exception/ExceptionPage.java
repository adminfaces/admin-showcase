package com.github.adminfaces.showcase.pages.exception;

import com.github.adminfaces.showcase.pages.BasePage;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.jboss.arquillian.graphene.request.RequestGuardException;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by rafael-pestano on 16/01/17.
 */
@Location("pages/exception/exception.xhtml")
public class ExceptionPage extends BasePage {

    @FindByJQuery("button.ui-button:first")
    protected WebElement btnBusiness;

    @FindByJQuery("button.btn-danger")
    protected WebElement btnRuntime;

    @FindByJQuery("button.btn-fatal")
    protected WebElement btnViewExpired;

    @FindByJQuery("button.btn-warning")
    protected WebElement btn404;

    @FindByJQuery("button.bg-gray")
    protected WebElement btn403;

    @FindByJQuery("button.bg-black")
    protected WebElement btnMultipleBusiness;

    @FindByJQuery("span.ui-messages-error-detail")
    protected List<GrapheneElement> errorMessages;


    public void clickBusinessButton() {
        try {
            guardAjax(btnBusiness).click();
        }catch (RequestGuardException e) {
            guardHttp(btnBusiness).click();
        }
    }

    public void clickMultipleBusinessButton() {
        guardAjax(btnMultipleBusiness).click(); 
    }

    public void clickRuntimeButton() {
        guardAjax(btnRuntime).click();
    }

    public void clickViewExpiredButton() {
        guardAjax(btnViewExpired).click();
    }

    public void click404Button() {
        guardHttp(btn404).click();
    }

    public void click403Button() {
        guardHttp(btn403).click();
    }


    public List<GrapheneElement> getErrorMessages() {
        return errorMessages;
    }
}
