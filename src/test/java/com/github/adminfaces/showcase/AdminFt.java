package com.github.adminfaces.showcase;

import com.github.adminfaces.showcase.pages.IndexPage;
import com.github.adminfaces.showcase.pages.components.ChipsPage;
import com.github.adminfaces.showcase.pages.components.DialogPage;
import com.github.adminfaces.showcase.pages.components.MessagesPage;
import com.github.adminfaces.showcase.pages.exception.*;
import com.github.adminfaces.showcase.pages.fragments.ControlSidebar;
import com.github.adminfaces.showcase.pages.fragments.Menu;
import com.github.adminfaces.showcase.pages.fragments.MenuSearch;
import com.github.adminfaces.showcase.pages.layout.BreadcrumbPage;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static com.github.adminfaces.showcase.ultil.DeployUtil.deploy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import org.openqa.selenium.support.FindBy;

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
    protected AccessDeniedPage accessDeniedPage;

    @Page
    protected ViewExpiredPage viewExpiredPage;

    @Page
    protected ExceptionPage exceptionPage;

    @Page
    protected MessagesPage messagesPage;

    @Page
    protected DialogPage dialogPage;

    @FindByJQuery("div.ui-growl-message")
    private GrapheneElement growlMessage;

    @FindByJQuery("section.sidebar > ul.sidebar-menu")
    private Menu menu;

    @FindBy(id = "menu-search")
    private MenuSearch menuSearch;

    @FindByJQuery("#controlsidebarPanel")
    private ControlSidebar controlSidebar;

    @FindByJQuery("body")
    private GrapheneElement pageBody;

    @Deployment(testable = false)
    public static Archive<?> getDeployment() {
        return deploy();
    }

    @Test
    @InSequence(1)
    public void shouldLoadIndexPage(@InitialPage IndexPage index) {
        assertThat(index.getPageTitle().getText()).startsWith("Welcome to the AdminFaces Showcase!");
    }

    @Test
    @InSequence(2)
    public void shouldThrowBusinessException(@InitialPage ExceptionPage exception) {
        assertThat(exception.getTitle().getText()).contains("Exceptions");
        assertThat(pageBody.getAttribute("class")
                .contains("skin-red")).isTrue();//skin-red is 'forced' in exceptions pages via ui:param
        exception.clickBusinessButton();
        assertThat(exceptionPage.getErrorMessages().get(0).getText()).isEqualTo("This kind of exception generates a faces message with severity error.");
    }

    @Test
    @InSequence(2)
    public void shouldMultipleBusinessException(@InitialPage ExceptionPage exception) {
        assertThat(exception.getTitle().getText()).contains("Exceptions");
        exception.clickMultipleBusinessButton();
        for (int i = 1; i <= 3; i++) {
            assertThat(exceptionPage.getErrorMessages().get(i - 1).getText()).isEqualTo("Exception " + i);
        }

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
        waitModel();
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
    @InSequence(2)
    public void shouldGoTo403Page(@InitialPage ExceptionPage exception) {
        assertThat(exception.getTitle().getText()).contains("Exceptions");
        exception.click403Button();
        assertThat(accessDeniedPage.getTitle().getText()).isEqualTo("Access denied! You do not have access to the requested page.");
    }

    @Test
    @InSequence(3)
    public void shouldNavigateUsingSideMenu(@InitialPage IndexPage index) {
        menu.goToHomePage();
        assertThat(index.getPageTitle().getText()).startsWith("Welcome to the AdminFaces Showcase!");
        menu.goToExceptionPage();
        assertThat(exceptionPage.getTitle().getText()).contains("Exceptions This page shows how the application behaves when exceptions are raised.");
        menu.goToDatatablePage();
        assertThat(browser.findElement(By.tagName("h1")).getText()).startsWith("Datatable");
        menu.goToPanelPage();
        assertThat(browser.findElement(By.tagName("h1")).getText()).startsWith("Panel");
        menu.goToButtonsPage();
        assertThat(browser.findElement(By.tagName("h1")).getText()).startsWith("Buttons");
    }

    @Test
    @InSequence(4)
    public void shouldFilterMenuItens(@InitialPage IndexPage index) {
        WebElement menuSearchInput = browser.findElement(By.cssSelector("input.form-control"));
        menuSearchInput.sendKeys("for");
        waitModel();
        assertThat(menuSearch.containsMenuItem("forms")).isTrue();
        assertThat(menuSearch.containsMenuItem("home")).isFalse();
        assertThat(menuSearch.containsMenuItem("exceptions")).isFalse();
        menuSearchInput.clear();

        menuSearchInput.sendKeys("at");
        assertThat(menuSearch.containsMenuItem("DataTable")).isTrue();
        assertThat(menuSearch.containsMenuItem("TriStateCheckbox")).isTrue();
        assertThat(menuSearch.containsMenuItem("Material")).isTrue();
        waitModel();
        menuSearchInput.clear();
        waitModel();
        menuSearchInput.sendKeys("hom");
        assertThat(menuSearch.containsMenuItem("home")).isTrue();
        waitModel();
        menuSearchInput.clear();
        menuSearchInput.sendKeys("exc");
        waitModel();
        assertThat(menuSearch.containsMenuItem("Exceptions")).isTrue();
        menuSearch.selectMenuItem("Exceptions");
        waitModel().until().element(exceptionPage.getTitle()).is().present();
    }

    @Test
    @InSequence(4)
    public void shouldShowFacesMessages(@InitialPage MessagesPage messagesPage) {
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

    @Test
    @InSequence(6)
    public void shouldCreateBreadcrumbs(@InitialPage BreadcrumbPage breadcrumbPage) {
        assertThat(breadcrumbPage.getBreadcrumb().isDisplayed()).isTrue();
        assertThat(breadcrumbPage.getHomeItem().isDisplayed()).isTrue();
        assertThat(breadcrumbPage.getHomeItem().getText()).isEqualTo("Home");
        breadcrumbPage.getHomeItem().click();//click in 'home' item to clear items added in other tests
        waitModel();
        Graphene.goTo(BreadcrumbPage.class);

        assertThat(breadcrumbPage.getBreadcrumbItem().isDisplayed()).isTrue();
        assertThat(breadcrumbPage.getBreadcrumbItem().getText()).isEqualTo("Breadcrumbs");

        breadcrumbPage.getInputLink().sendKeys("/pages/components/messages.xhtml");
        breadcrumbPage.getInputTitle().sendKeys("Messages");
        breadcrumbPage.clickBtnAdd();
        WebElement messagesItem = breadcrumbPage.getBreadcrumbItem();//Messages item will be added as second item because "Breadcrumb" item is added in preRenderView
        assertThat(messagesItem.isDisplayed()).isTrue();
        assertThat(messagesItem.getText()).isEqualTo("Messages");
        Graphene.guardHttp(messagesItem).click();
        assertThat(messagesPage.getTitle().getText()).isEqualTo("Messages This page shows how faces messages are rendered.");
    }

    @Test
    @InSequence(7)
    public void shouldConfigureLayoutViaControlSidebar(@InitialPage IndexPage indexPage) {
        controlSidebar.openControlSidebar();
        controlSidebar.toggleFixedLayout();
        controlSidebar.toggleFixedLayout();//uncheck so we can toogle boxed layout
        controlSidebar.toggleBoxedLayout();
        controlSidebar.toggleSidebarCollapsed();
        controlSidebar.toggleSidebarSkin();
        controlSidebar.activateSkinBlack();
        controlSidebar.activateSkinTeal();
        controlSidebar.toggleMenuLayout();
    }

    @Test
    @InSequence(8)
    public void shouldKeepLayoutConfigurationAcrossPages(@InitialPage IndexPage indexPage) {
        assertThat(pageBody.getAttribute("class")
                .contains("layout-top-nav")).isTrue(); //layout mode must be persisted across pages

        assertThat(pageBody.getAttribute("class")
                .contains("layout-boxed")).isTrue();

        assertThat(pageBody.getAttribute("class")
                .contains("skin-teal")).isTrue();
    }

    @Test
    @InSequence(9)
    public void shouldRestoreLayoutConfiguration(@InitialPage IndexPage indexPage) {
        controlSidebar.openControlSidebar();
        controlSidebar.restoreDefaults();

        assertThat(pageBody.getAttribute("class")
                .contains("layout-top-nav")).isFalse();
        assertThat(pageBody.getAttribute("class")
                .contains("layout-top-nav")).isFalse();

        assertThat(pageBody.getAttribute("class")
                .contains("layout-boxed")).isFalse();

        assertThat(pageBody.getAttribute("class")
                .contains("skin-blue")).isTrue();
    }

    @Test
    @InSequence(10)
    public void shouldAddChipsTags(@InitialPage ChipsPage chipsPage) {
        chipsPage.addDefaultChips();
        chipsPage.addDangerChips();
        chipsPage.addCustomChips();
    }

    @Test
    @InSequence(11)
    public void shouldDestroyTheWorld(@InitialPage DialogPage dialogPage) {
        dialogPage.destroyTheWorld();
        WebElement confirmDialog = browser.findElement(By.xpath("//SPAN[contains(@class, 'ui-dialog-title') and text()='Confirmation']"));
        assertThat(confirmDialog.isDisplayed()).isTrue();
        browser.findElement(By.xpath("//span[contains(@class,'ui-button-text') and text()='Yes']")).click();
        waitModel().until().element(confirmDialog)
                .is().not().visible();
    }

    @Test
    @InSequence(12)
    public void shouldFillLoginDialog(@InitialPage IndexPage indexPage) {
        waitModel().until().element(By.cssSelector("section.sidebar > ul.sidebar-menu")).is().present();
        menu.goToDialogPage();
        assertThat(browser.findElement(By.cssSelector("section.content-header h1")).getText()).startsWith("Dialog Dialog");
        dialogPage.doLogin();
        assertThat(browser.findElement(By.xpath("//SPAN[contains(@class, 'ui-dialog-title') and text()='Login']")).isDisplayed()).isTrue();
        waitModel().until().element(By.xpath("//button[contains(@class,'ui-autocomplete-dropdown')]")).is().clickable();
        Actions actions = new Actions(browser);
        actions = new Actions(browser);
        actions.moveToElement(browser.findElement(By.xpath("//button[contains(@class,'ui-autocomplete-dropdown')]"))).click().perform();
        waitModel().until().element(By.xpath("//li[contains(@class,'ui-autocomplete-item')]")).is().visible();
        browser.findElement(By.xpath("//li[contains(@class,'ui-autocomplete-item') and text()='0']")).click();
        waitModel().until().element(By.xpath("//li[contains(@class,'ui-autocomplete-item')]")).is().not().visible();
        dialogPage.clickSelecOneMenu();
        WebElement select = browser.findElement(By.xpath("//li[contains(@class,'ui-selectonemenu-item') and text()='Cash']"));
        waitModel().until().element(select).is().visible();
        actions.moveToElement(select).click().perform();
        waitModel().until().element(select).is().not().visible();
    }

}
