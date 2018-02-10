package com.github.adminfaces.showcase.pages.components;

import com.github.adminfaces.showcase.pages.BasePage;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

/**
 * Created by rafael-pestano on 16/01/17.
 */
@Location("pages/components/messages.xhtml")
public class MessagesPage extends BasePage {

    @FindByJQuery("button[id$='submitButton']")
    protected WebElement btnSubmit;

    @FindByJQuery("button.btn-primary")
    protected WebElement btnInfo;

    @FindByJQuery("button.btn-warning")
    protected WebElement btnWarn;

    @FindByJQuery("button.btn-danger")
    protected WebElement btnError;

    @FindByJQuery("button.btn-fatal")
    protected WebElement btnFatal;

    @FindByJQuery("span.ui-messages-info-summary")
    protected WebElement msgInfoSummary;

    @FindByJQuery("span.ui-messages-info-detail")
    protected WebElement msgInfoDetail;

    @FindByJQuery("span.ui-messages-warn-summary")
    protected WebElement msgWarnSummary;

    @FindByJQuery("span.ui-messages-warn-detail")
    protected WebElement msgWarnDetail;

    @FindByJQuery("span.ui-messages-error-summary")
    protected WebElement msgErrorSummary;

    @FindByJQuery("span.ui-messages-error-detail")
    protected WebElement msgErrorDetail;

    @FindByJQuery("span.ui-messages-fatal-summary")
    protected WebElement msgFatalSummary;

    @FindByJQuery("span.ui-messages-fatal-detail")
    protected WebElement msgFatalDetail;

    @FindByJQuery("div[id$='msgDefault'] span.ui-message-error-detail")
    private WebElement fieldMsgDefault;

    @FindByJQuery("div[id$='msgTxt'] span.ui-message-error-detail")
    private WebElement fieldMsgTxt;

    @FindByJQuery("div[id$='msgIcon'] span.ui-message-error-icon")
    private WebElement fieldMsgIcon;

    public void clickBtnSubmit(){
        waitModel().until().element(btnSubmit).is().visible();
        guardAjax(btnSubmit).click();
        waitModel(browser).until().element(fieldMsgDefault).is().present();
    }

    public void clickBtnInfo(){
        guardAjax(btnInfo).click();
    }

    public void clickBtnWarn(){
        guardAjax(btnWarn).click();
    }

    public void clickBtnError(){
        guardAjax(btnError).click();
    }

    public void clickBtnFatal(){
        guardAjax(btnFatal).click();
    }

    public WebElement getMsgInfoSummary() {
        return msgInfoSummary;
    }

    public WebElement getMsgInfoDetail() {
        return msgInfoDetail;
    }

    public WebElement getMsgWarnSummary() {
        return msgWarnSummary;
    }

    public WebElement getMsgWarnDetail() {
        return msgWarnDetail;
    }

    public WebElement getMsgErrorSummary() {
        return msgErrorSummary;
    }

    public WebElement getMsgErrorDetail() {
        return msgErrorDetail;
    }

    public WebElement getMsgFatalSummary() {
        return msgFatalSummary;
    }

    public WebElement getMsgFatalDetail() {
        return msgFatalDetail;
    }

    public WebElement getFieldMsgDefault() {
        return fieldMsgDefault;
    }

    public WebElement getFieldMsgTxt() {
        return fieldMsgTxt;
    }

    public WebElement getFieldMsgIcon() {
        return fieldMsgIcon;
    }
}
