package com.github.adminfaces.showcase.pages;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;

/**
 * Created by rafael-pestano on 16/01/17.
 */
@Location("index.xhtml")
public class IndexPage {

    @FindByJQuery("div.content-wrapper h2")
    protected WebElement pageTitle;


    public WebElement getPageTitle() {
        return pageTitle;
    }
}
