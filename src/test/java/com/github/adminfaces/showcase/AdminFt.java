package com.github.adminfaces.showcase;

import com.github.adminfaces.showcase.pages.exception.ErrorPage;
import com.github.adminfaces.showcase.pages.exception.ExceptionPage;
import com.github.adminfaces.showcase.pages.IndexPage;
import com.github.adminfaces.showcase.pages.exception.NotFoundPage;
import com.github.adminfaces.showcase.pages.exception.ViewExpiredPage;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static com.github.adminfaces.showcase.ultil.DeployUtil.deploy;
import static org.assertj.core.api.Assertions.assertThat;


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


}
