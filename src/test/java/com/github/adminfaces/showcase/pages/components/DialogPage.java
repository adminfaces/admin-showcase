package com.github.adminfaces.showcase.pages.components;

import com.github.adminfaces.showcase.pages.BasePage;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jboss.arquillian.graphene.Graphene.*;

/**
 * Created by rafael-pestano on 16/01/17.
 */
@Location("pages/components/dialog.xhtml")
public class DialogPage extends BasePage {

    @FindByJQuery("button[id$=bt-destroy]")
    protected WebElement btDestroyWorld;

    @FindByJQuery("a[id$=btDialogLogin]")
    protected WebElement btLogin;

    @FindByJQuery("div.ui-selectonemenu-trigger")
    protected WebElement selectOneTrigger;

    public void destroyTheWorld() {
        guardNoRequest(btDestroyWorld).click();
    }

    public void doLogin() {
        guardAjax(btLogin).click();
    }

    public void clickSelecOneMenu() {
        selectOneTrigger.click();
        waitModel();
    }
}
