/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.showcase.pages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.util.List;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 *
 * @author rafael-pestano
 */
public class DataTablePage extends BasePage {

    @FindByJQuery("hr.ui-separator + div.ui-datatable")
    private GrapheneElement datatable;

    @FindByJQuery("div.ui-datatable td .ui-chkbox-box")
    private List<GrapheneElement> datatableCheckBoxes;

    @FindByJQuery(".ui-column-customfilter .ui-selectcheckboxmenu-trigger")
    private GrapheneElement colorsFilterTrigger;

    public void filterByColor() {
        colorsFilterTrigger.click();
        List<WebElement> colorOptions = browser.findElements(By.cssSelector("div.ui-selectcheckboxmenu-items-wrapper ul li"));
        waitModel().until().element(By.cssSelector("div.ui-selectcheckboxmenu-items-wrapper")).is().present();
        for (int i = 0; i <= 1; i++) {
            guardAjax(colorOptions.get(i)).click();
        }
        browser.findElement(By.cssSelector("a.ui-selectcheckboxmenu-close .ui-icon-circle-close")).click();
        waitModel().until().element(By.cssSelector("div.ui-selectcheckboxmenu-items-wrapper")).is().not().visible();
        
    }

    public void selectRows() {
        for (int i = 0; i <= 2; i++) {
            waitModel();
            datatableCheckBoxes.get(i).click();
        }
        assertThat(browser.findElements(By.cssSelector("tr.ui-datatable-selectable.ui-state-highlight")).size())
                .isEqualTo(3);
    }

}
