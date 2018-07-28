package com.github.adminfaces.showcase.pages;

import org.jboss.arquillian.drone.api.annotation.Drone;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by rafael-pestano on 16/01/17.
 */
@Location("login.xhtml")
public class LoginPage  {
    
    @FindByJQuery("p.login-box-msg")
    private WebElement pageTitle;
    
    @FindByJQuery("input[type='email']")
    private WebElement email;
    
    @FindByJQuery("input[type='password']")
    private WebElement password;
    
    @FindByJQuery("button.btn-success")
    private WebElement signIn;
            
    @FindByJQuery("span.ui-messages-info-detail")
    protected GrapheneElement messages;


    public WebElement getPageTitle() {
        return pageTitle;
    }

    public GrapheneElement getMessages() {
        return messages;
    }
    
    
    
    public void doLogon(String email, String password) {
        this.email.clear();
        this.password.clear();
        waitModel();
        this.email.sendKeys(email);
        this.password.sendKeys(password);
        waitModel();
        signIn.click();
        waitModel().until().element(messages).is().present();
    }
}
