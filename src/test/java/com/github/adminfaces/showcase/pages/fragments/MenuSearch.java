package com.github.adminfaces.showcase.pages.fragments;

import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.assertj.core.api.Assertions.assertThat;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.*;
import org.openqa.selenium.By;

/**
 * Created by rafael-pestano on 22/07/18.
 */
public class MenuSearch {

    @Root
    private GrapheneElement root;

    @Drone
    protected WebDriver browser;

    @FindByJQuery("ul.dropdown-menu")
    private GrapheneElement menuItens;

     
    public boolean containsMenuItem(String menu) {
        List<GrapheneElement> itens = menuItens.findGrapheneElements(By.cssSelector("li a  span"));
        
        for (GrapheneElement menuItem : itens) {
            if(menuItem.getText().equalsIgnoreCase(menu)) {
            	return true;
            }
        }
        return false;
    }
    
    public void selectMenuItem(String menuItem) {
         List<GrapheneElement> itens = menuItens.findGrapheneElements(By.cssSelector("li a"));
           for (GrapheneElement item : itens) {
               if(item.getText().contains(menuItem)) {
                   item.click();
               }
           }
    }
    
}
