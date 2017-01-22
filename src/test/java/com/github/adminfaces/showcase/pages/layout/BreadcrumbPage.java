package com.github.adminfaces.showcase.pages.layout;

import com.github.adminfaces.showcase.pages.BasePage;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by rafael-pestano on 16/01/17.
 */
@Location("pages/layout/breadcrumb.xhtml")
public class BreadcrumbPage extends BasePage {

    @FindByJQuery("ol.breadcrumb")
    protected WebElement breadcrumb;

    @FindByJQuery("ol.breadcrumb > li:nth-child(1)")
    protected WebElement homeItem;

    @FindByJQuery("ol.breadcrumb > li:nth-child(2)")
    protected WebElement breadcrumbItem;


    @FindByJQuery("input[id$=inpt-title]")
    protected WebElement inputTitle;

    @FindByJQuery("input[id$=inpt-link]")
    protected WebElement inputLink;

    @FindByJQuery("button.btn-primary")
    protected WebElement btnAddBreadcrumb;


    public void clickBtnAdd() {
       guardHttp(btnAddBreadcrumb).click();
    }

    public WebElement getBreadcrumb() {
        return breadcrumb;
    }

    public WebElement getBreadcrumbItem() {
        return breadcrumbItem;
    }

    public WebElement getHomeItem() {
        return homeItem;
    }

    public WebElement getInputTitle() {
        return inputTitle;
    }

    public WebElement getInputLink() {
        return inputLink;
    }
}
