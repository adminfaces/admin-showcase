package com.github.adminfaces.showcase.pages.components;

import com.github.adminfaces.showcase.pages.BasePage;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

/**
 * Created by rafael-pestano on 16/01/17.
 */
@Location("pages/components/datepicker.xhtml")
public class DatePickerPage extends BasePage {

    @FindByJQuery("input[id$=spanish_input]")
    private WebElement spanishDatePicker;

    @FindByJQuery("span[id=form\\:spanish] table.ui-datepicker-calendar")
    private WebElement spanishDatePickerDays;


    @FindByJQuery("span[id=form\\:spanish] select.ui-datepicker-month")
    private WebElement spanishDatePickerMonths;

    @FindByJQuery(".content-header h1")
    private WebElement pageTitle;

    public void selectDate() {
        spanishDatePicker.click();
        waitModel().until().element(spanishDatePickerDays).is().clickable();
        assertThat(spanishDatePickerMonths.getText()).contains("Ene");
        List<WebElement> days = spanishDatePickerDays.findElements(By.cssSelector(".ui-datepicker-calendar td a "));
        assertThat(days).isNotEmpty();
        days.get(1).click();
        waitModel().until().element(spanishDatePickerDays).is().not().clickable();
        assertThat(spanishDatePicker.getAttribute("value")).startsWith(Calendar.getInstance().get(Calendar.YEAR)+"");
    }

    public WebElement getPageTitle() {
        return pageTitle;
    }
}
