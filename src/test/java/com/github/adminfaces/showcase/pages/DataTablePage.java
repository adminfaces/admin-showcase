/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.showcase.pages;

import java.util.List;
import static org.jboss.arquillian.graphene.Graphene.*;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author rafael-pestano
 */
public class DataTablePage extends BasePage {

	@FindByJQuery("hr.ui-separator + div.ui-datatable")
	private GrapheneElement datatable;

	@FindByJQuery("div.ui-datatable-sticky tbody tr")
	private List<GrapheneElement> datatableRows;

	@FindByJQuery(".ui-column-customfilter .ui-selectcheckboxmenu-trigger")
	private GrapheneElement colorsFilterTrigger;

	public void filterByColor() {
		guardNoRequest(colorsFilterTrigger).click();
		List<WebElement> colorOptions = browser.findElements(By.cssSelector("div.ui-selectcheckboxmenu-items-wrapper ul li"));
		waitModel().until().element(By.cssSelector("div.ui-selectcheckboxmenu-items-wrapper")).is().present();
		for (int i = 0; i <= 1; i++) {
			guardAjax(colorOptions.get(i)).click();
		}
	}

	public void selectRows() {
		Actions actions = new Actions(browser);
		actions.keyDown(Keys.LEFT_CONTROL);
		for (int i = 0; i <= 2; i++) {
			guardNoRequest(actions.moveToElement(datatableRows.get(i)).click()).perform();
			waitModel();
		}
		actions.keyUp(Keys.LEFT_CONTROL);
		assertThat(browser.findElements(By.cssSelector("tr.ui-datatable-selectable.ui-state-highlight")).size())
				.isEqualTo(3);
	}

}
