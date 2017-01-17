package com.github.adminfaces.showcase.pages.messages;

import com.github.adminfaces.showcase.pages.BasePage;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;
import static org.jboss.arquillian.graphene.Graphene.*;

/**
 * Created by rafael-pestano on 16/01/17.
 */
@Location("pages/messages/messages.xhtml")
public class MessagesPage extends BasePage {

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
}
