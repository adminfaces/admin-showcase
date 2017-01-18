package com.github.adminfaces.showcase;

import com.github.adminfaces.showcase.pages.exception.ErrorPage;
import com.github.adminfaces.showcase.pages.exception.ExceptionPage;
import com.github.adminfaces.showcase.pages.IndexPage;
import com.github.adminfaces.showcase.pages.exception.NotFoundPage;
import com.github.adminfaces.showcase.pages.exception.ViewExpiredPage;
import com.github.adminfaces.showcase.pages.fragments.Menu;
import com.github.adminfaces.showcase.pages.messages.MessagesPage;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.github.adminfaces.showcase.ultil.DeployUtil.deploy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jboss.arquillian.graphene.Graphene.waitModel;


/**
 * Created by rafael-pestano on 16/01/17.
 */
@RunWith(Arquillian.class)
public class AdminFt {


    @Drone
    protected WebDriver browser;

    @Page
    protected ErrorPage errorPage;

    @Page
    protected NotFoundPage notFoundPage;

    @Page
    protected ViewExpiredPage viewExpiredPage;

    @Page
    protected  ExceptionPage exceptionPage;

    @FindByJQuery("div.ui-growl-message")
    private GrapheneElement growlMessage;

    @FindByJQuery("section.sidebar > ul.sidebar-menu")
    private Menu menu;

    @Deployment(testable = false)
    public static Archive<?> getDeployment(){
        return deploy();
    }


    @Test
    @InSequence(1)
    public void shouldLoadIndexPage(@InitialPage IndexPage index) {
        assertThat(index.getPageTitle().getText()).isEqualTo("Welcome to the AdminFaces Showcase!");
    }


    @Test
    @InSequence(2)
    public void shouldGoToErrorPage(@InitialPage ExceptionPage exception) {
        assertThat(exception.getTitle().getText()).contains("Exceptions");
        exception.clickRuntimeButton();
        assertThat(errorPage.getTitle().getText()).isEqualTo("500");
    }


    @Test
    @InSequence(2)
    public void shouldGoToViewExpiredPage(@InitialPage ExceptionPage exception) {
        assertThat(exception.getTitle().getText()).contains("Exceptions");
        exception.clickViewExpiredButton();
        assertThat(viewExpiredPage.getTitle().getText()).isEqualTo("View expired");
    }

    @Test
    @InSequence(2)
    public void shouldGoTo404Page(@InitialPage ExceptionPage exception) {
        assertThat(exception.getTitle().getText()).contains("Exceptions");
        exception.click404Button();
        assertThat(notFoundPage.getTitle().getText()).isEqualTo("Oops! Page not found.");
    }

    @Test
    @InSequence(3)
    public void shouldNavigateUsingSideMenu(@InitialPage IndexPage index){
        menu.goToHomePage();
        assertThat(index.getPageTitle().getText()).isEqualTo("Welcome to the AdminFaces Showcase!");
        menu.goToExceptionPage();
        assertThat(exceptionPage.getTitle().getText()).contains("Exceptions This page shows how the application behaves when exceptions are raised.");
    }

    @Test
    @InSequence(4)
    public void shouldFilterMenuItens(@InitialPage IndexPage index){
        if(isPhantomjs()){
            //this test doesn't work on phantomjs (cannot)
            return;
        }
        WebElement menuSearchInput = browser.findElement(By.cssSelector("input.form-control"));
        menuSearchInput.sendKeys("for");
        waitModel();
        assertThat(menu.getForms().isDisplayed()).isTrue();
        assertThat(menu.getHome().isDisplayed()).isFalse();
        assertThat(menu.getException().isDisplayed()).isFalse();
        menuSearchInput.clear();
        menuSearchInput.sendKeys("hom");
        waitModel();
        assertThat(menu.getHome().isDisplayed()).isTrue();
        assertThat(menu.getForms().isDisplayed()).isFalse();
        assertThat(menu.getException().isDisplayed()).isFalse();
        menuSearchInput.clear();
        menuSearchInput.sendKeys("exc");
        waitModel();
        assertThat(menu.getException().isDisplayed()).isTrue();
        assertThat(menu.getHome().isDisplayed()).isFalse();
        assertThat(menu.getForms().isDisplayed()).isFalse();
    }

    @Test
    @InSequence(4)
    public void shouldShowFacesMessages(@InitialPage MessagesPage messagesPage) {

        if(isPhantomjs()){
            //this test doesn't work on phantomjs (conflict with jquery used by primefaces ajax: msg: ReferenceError: Can't find variable: $)
            // as consequence it doesn't fire ajax request properly: RequestGuardException: Request type 'XHR' was expected, but type 'HTTP' was done instead
            //works great with chrome driver (tested with version 55)
            return;
        }

        messagesPage.clickBtnInfo();
        assertThat(growlMessage.getText()).contains("AdminFaces info message.");
        assertThat(messagesPage.getMsgInfoSummary().getText()).isEqualTo("Info");
        assertThat(messagesPage.getMsgInfoDetail().getText()).isEqualTo("AdminFaces info message.");

        messagesPage.clickBtnError();
        assertThat(growlMessage.getText()).contains("AdminFaces Error message.");

        assertThat(messagesPage.getMsgErrorSummary().getText()).isEqualTo("Error!");
        assertThat(messagesPage.getMsgErrorDetail().getText()).isEqualTo("AdminFaces Error message.");

        messagesPage.clickBtnWarn();
        assertThat(growlMessage.getText()).contains("AdminFaces Warning message.");

        assertThat(messagesPage.getMsgWarnSummary().getText()).isEqualTo("Warning!");
        assertThat(messagesPage.getMsgWarnDetail().getText()).isEqualTo("AdminFaces Warning message.");

        messagesPage.clickBtnFatal();
        assertThat(growlMessage.getText()).contains("AdminFaces Fatal message.");

        assertThat(messagesPage.getMsgFatalSummary().getText()).isEqualTo("Fatal!");
        assertThat(messagesPage.getMsgFatalDetail().getText()).isEqualTo("AdminFaces Fatal message.");
    }

    @Test
    @InSequence(5)
    public void shouldShowFieldMessagesAfterFormSubmit(@InitialPage MessagesPage messagesPage) {
        messagesPage.clickBtnSubmit();
        assertThat(messagesPage.getFieldMsgDefault().getText()).isEqualTo("Default: Validation Error: Value is required.");
        assertThat(messagesPage.getFieldMsgTxt().getText()).isEqualTo("Text: Validation Error: Value is required.");
        assertThat(messagesPage.getFieldMsgIcon().getAttribute("title")).isEqualTo("Icon: Validation Error: Value is required.");
    }


    private boolean isPhantomjs() {
        //some tests doesn't work on phantomjs (conflict with jquery used by primefaces ajax: msg: ReferenceError: Can't find variable: $)
        // as consequence it doesn't fire ajax request properly: RequestGuardException: Request type 'XHR' was expected, but type 'HTTP' was done instead
        //works great with chrome driver (tested with version 55)
        return browser.getClass().toString().toLowerCase().contains("phantomjs");
    }
}
