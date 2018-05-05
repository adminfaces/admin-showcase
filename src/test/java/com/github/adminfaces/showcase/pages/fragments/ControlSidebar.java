package com.github.adminfaces.showcase.pages.fragments;

import java.util.concurrent.TimeUnit;
import static org.assertj.core.api.Assertions.assertThat;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.*;
import org.openqa.selenium.By;

/**
 * Created by rafael-pestano on 16/01/17.
 */
public class ControlSidebar {

    @Root
    private GrapheneElement root;

    @Drone
    protected WebDriver browser;

    @FindByJQuery("a.ui-link i.fa-gears")
    private GrapheneElement controlSidebarLink;

    @FindByJQuery("body")
    private GrapheneElement pageBody;

    @FindByJQuery("aside.control-sidebar")
    private GrapheneElement controlSidebar;

    @FindBy(id = "toggle-menu-layout")
    private GrapheneElement toggleMenuCheckbox;

    @FindBy(id = "fixed-layout")
    private GrapheneElement fixedLayoutCheckbox;

    @FindBy(id = "boxed-layout")
    private GrapheneElement boxedLayoutCheckbox;

    @FindBy(id = "sidebar-skin")
    private GrapheneElement sidebarSkinCheckbox;

    @FindBy(id = "btn-skin-black")
    private GrapheneElement btnSkinBlack;

    @FindBy(id = "btn-skin-teal")
    private GrapheneElement btnSkinTeal;

    public void openControlSidebar() {
        assertThat(controlSidebar.getAttribute("class")
                .contains("control-sidebar-open")).isFalse();
        browser.findElement(By.id("layout-setup")).click();
        waitModel().withTimeout(500, TimeUnit.MILLISECONDS);
        assertThat(controlSidebar.getAttribute("class")
                .contains("control-sidebar-open")).isTrue();
    }

    public void toggleFixedLayout() {
        waitGui().until().element(fixedLayoutCheckbox).is().visible();
        guardNoRequest(fixedLayoutCheckbox).click();
    }

    public void toggleBoxedLayout() {
        assertThat(pageBody.getAttribute("class")
                .contains("layout-boxed")).isFalse();
        guardNoRequest(boxedLayoutCheckbox).click();
        assertThat(pageBody.getAttribute("class")
                .contains("layout-boxed")).isTrue();
    }

    public void toggleSidebarSkin() {
        assertThat(controlSidebar.getAttribute("class")
                .contains("control-sidebar-dark")).isTrue();
        guardNoRequest(sidebarSkinCheckbox).click();
        assertThat(controlSidebar.getAttribute("class")
                .contains("control-sidebar-light")).isTrue();
    }

    public void activateSkinBlack() {
        assertThat(pageBody.getAttribute("class")
                .contains("skin-black")).isFalse();
        guardAjax(btnSkinBlack).click();
        assertThat(pageBody.getAttribute("class")
                .contains("skin-black")).isTrue();
    }

    public void activateSkinTeal() {
        assertThat(pageBody.getAttribute("class")
                .contains("skin-teal")).isFalse();
        guardAjax(btnSkinTeal).click();
        assertThat(pageBody.getAttribute("class")
                .contains("skin-teal")).isTrue();
    }

    public GrapheneElement getPageBody() {
        return pageBody;
    }
    
    
}
