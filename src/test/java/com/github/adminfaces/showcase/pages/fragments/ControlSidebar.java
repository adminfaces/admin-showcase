package com.github.adminfaces.showcase.pages.fragments;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jboss.arquillian.graphene.Graphene.*;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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

    @FindByJQuery(".control-sidebar")
    private GrapheneElement controlSidebar;

    @FindBy(id = "toggle-menu-layout")
    private GrapheneElement toggleMenuCheckbox;

    @FindBy(id = "fixed-layout")
    private GrapheneElement fixedLayoutCheckbox;

    @FindBy(id = "sidebar-collapsed")
    private GrapheneElement collapsedSidebarCheckbox;

    @FindBy(id = "boxed-layout")
    private GrapheneElement boxedLayoutCheckbox;
    
    @FindBy(id = "sidebar-expand-hover-label")
    private GrapheneElement expandOnHoverLabel;

    @FindBy(id = "sidebar-skin")
    private GrapheneElement sidebarSkinCheckbox;

    @FindBy(id = "btn-skin-black")
    private GrapheneElement btnSkinBlack;

    @FindBy(id = "btn-skin-teal")
    private GrapheneElement btnSkinTeal;

    @FindByJQuery("div#restore-defaults a")
    private GrapheneElement restoreDefaults;
    
    @FindByJQuery("a#layout-setup")
    private GrapheneElement openControlSidebarBtn;

    public void openControlSidebar() {
        assertThat(controlSidebar.getAttribute("class")
            .contains("control-sidebar-open")).isFalse();
        WebElement openControlSidebarLink = browser.findElement(By.id("layout-setup"));
        openControlSidebarLink.click();
        waitModel().until().element(restoreDefaults).is().present();
        assertThat(controlSidebar.getAttribute("class")
            .contains("control-sidebar-open")).isTrue();
    }

    public void toggleFixedLayout() {
        waitGui().until().element(fixedLayoutCheckbox).is().visible();
        boolean isFixedLayout = pageBody.getAttribute("class")
            .contains("fixed");
        guardNoRequest(fixedLayoutCheckbox).click();
        assertThat(pageBody.getAttribute("class")
            .contains("fixed")).isEqualTo(!isFixedLayout);
        assertThat(boxedLayoutCheckbox.getAttribute("class").contains("ui-state-disabled")).isEqualTo(!isFixedLayout);//boxed layout is disabled when fixed layout is enabled
    }

    public void toggleBoxedLayout() {
        waitGui().until().element(boxedLayoutCheckbox).is().visible();
        boolean isBoxedLayout = pageBody.getAttribute("class")
            .contains("layout-boxed");
        guardNoRequest(boxedLayoutCheckbox).click();
        assertThat(pageBody.getAttribute("class")
            .contains("layout-boxed")).isEqualTo(!isBoxedLayout);
        assertThat(fixedLayoutCheckbox.getAttribute("class").contains("ui-state-disabled")).isEqualTo(!isBoxedLayout);//fixed layout is disabled when boxed layout is enabled
    }

    public void toggleSidebarSkin() {
        assertThat(controlSidebar.getAttribute("class")
            .contains("control-sidebar-dark")).isTrue();
        guardNoRequest(sidebarSkinCheckbox).click();
        assertThat(controlSidebar.getAttribute("class")
            .contains("control-sidebar-light")).isTrue();
    }

    public void toggleMenuLayout() {
        boolean isTopMenu = isTopMenuLayout();
        waitModel().until().element(toggleMenuCheckbox).is().clickable();
        toggleMenuCheckbox.click();
        if (isTopMenu) {
            waitModel().until().element(By.cssSelector("ul.sidebar-menu")).is().present();

        } else {
            waitModel().until().element(By.cssSelector("ul.navbar-nav")).is().present();
        }
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

    public void restoreDefaults() {
        waitModel().until().element(restoreDefaults).is().present();
        restoreDefaults.click();
        waitModel().until().element(By.cssSelector("ul.sidebar-menu")).is().present();
    }

    public GrapheneElement getPageBody() {
        return pageBody;
    }

    private boolean isTopMenuLayout() {
        return pageBody.getAttribute("class").contains("layout-top-nav");
    }

    public void toggleSidebarCollapsed() {
        waitGui().until().element(collapsedSidebarCheckbox).is().visible();
        boolean isSidebarCoppased = pageBody.getAttribute("class")
            .contains("sidebar-collapse");
        guardNoRequest(collapsedSidebarCheckbox).click();
        assertThat(pageBody.getAttribute("class")
            .contains("sidebar-collapse")).isEqualTo(!isSidebarCoppased);
    }

    public void toggleExpandOnHover() {
        waitGui().until().element(expandOnHoverLabel).is().visible();
        boolean isSidebarCoppased = pageBody.getAttribute("class")
            .contains("sidebar-collapse");
        guardNoRequest(expandOnHoverLabel).click();
        assertThat(pageBody.getAttribute("class")
            .contains("sidebar-collapse")).isEqualTo(!isSidebarCoppased);

    }

}
