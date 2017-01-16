package com.github.adminfaces.showcase.pages.exception;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;

/**
 * Created by rafael-pestano on 16/01/17.
 */
@Location("viewExpired.xhtml")
public class ViewExpiredPage {

    @FindByJQuery("div.error-content h3")
    protected WebElement title;


    public WebElement getTitle() {
        return title;
    }
}
