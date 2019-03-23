package com.github.adminfaces.showcase.pages.components;

import com.github.adminfaces.showcase.pages.BasePage;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import org.jboss.arquillian.graphene.request.RequestGuardException;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

/**
 * Created by rafael-pestano on 23/03/2019.
 */
@Location("pages/sample/sample-form.xhtml")
public class SampleFormPage extends BasePage {

    @FindByJQuery("button.btn-primary")
    protected WebElement btnSubmit;
    
    @FindByJQuery("span.ui-messages-info-detail")
    protected WebElement infoMsg;

    @FindByJQuery("button.btn-success")
    protected WebElement btnClear;

    @FindByJQuery("button.btn-danger")
    protected WebElement btnDelete;

    @FindByJQuery("button.btn-default")
    protected WebElement btnBack;

    @FindByJQuery("input[id=firstname]")
    protected WebElement firstname;

    @FindBy(id = "id")
    protected WebElement id;

    @FindBy(css = "input[id=firstname] ~ span.help-block .ui-message-error-detail")
    protected WebElement firstameMsg;

    @FindByJQuery("input[id=surname]")
    protected WebElement surname;

    @FindBy(css = "input[id=surname] ~ span.help-block .ui-message-error-detail")
    protected WebElement surnameMsg;

    @FindByJQuery("input[id=age_input]")
    protected WebElement age;

    @FindBy(css = "span[id=age] ~ span.help-block .ui-message-error-detail")
    protected WebElement ageMsg;

    @FindByJQuery("input[id=talk_input]")
    protected WebElement talk;

    @FindByJQuery("span[id=talk] .ui-icon-triangle-1-s")
    protected WebElement talkTrigger;

    @FindByJQuery("ul.ui-autocomplete-items")
    protected WebElement talkItens;

    @FindBy(css = "span[id=talk] ~ span.help-block .ui-message-error-detail")
    protected WebElement talkMsg;

    @FindBy(css = "div[id=useSpaces]")
    protected WebElement useSpaces;

    @FindByJQuery("button.btn-material.btn-primary")
    protected WebElement btnConfirm;

    @FindByJQuery("button.btn-material.btn-danger")
    protected WebElement btnCancel;

    public void clickBtnSubmit() {
        waitModel().until().element(btnSubmit).is().visible();
        guardAjax(btnSubmit).click();
    }

    public void clickBtnClear() {
        guardAjax(btnClear).click();
    }

    public void clickBtnRemove() {
        guardAjax(btnDelete).click();
    }

    public void clickBtnConfirm() {
        guardAjax(btnConfirm).click();
    }

    public void clickBtnCancel() {
        guardAjax(btnCancel).click();
    }

    public void clickBtnBack() {
        try {
            guardAjax(btnBack).click();
        } catch (RequestGuardException e) {
        }
    }

    public WebElement getInfoMsg() {
        return infoMsg;
    }
    
    public WebElement getFirstname() {
        return firstname;
    }

    public WebElement getId() {
        return id;
    }

    public WebElement getSurname() {
        return surname;
    }

    public WebElement getTalk() {
        return talk;
    }

    public void selectTalk() {
        talkTrigger.click();
        waitModel().until().element(talkItens).is().present();
        browser.findElements(By.cssSelector("li.ui-autocomplete-item")).get(0).click();
        waitModel().until().element(talkItens).is().not().clickable();
    }

    public WebElement getUseSpaces() {
        return useSpaces;
    }

    public WebElement getAge() {
        return age;
    }

    public WebElement getFirstameMsg() {
        return firstameMsg;
    }

    public WebElement getSurnameMsg() {
        return surnameMsg;
    }

    public WebElement getAgeMsg() {
        return ageMsg;
    }

    public WebElement getTalkMsg() {
        return talkMsg;
    }

}
