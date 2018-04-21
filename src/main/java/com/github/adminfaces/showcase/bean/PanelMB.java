package com.github.adminfaces.showcase.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.ToggleEvent;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;

/**
 * Created by rmpestano on 23/01/17.
 */
@Named
@ViewScoped
public class PanelMB implements Serializable {
    
    private List<String> boxColors;
    private String selectedColor;
    private boolean solid;
    
    @PostConstruct
    public void init() {
        boxColors = Arrays.asList("box-default","box-primary","box-info","box-success","box-warning","box-danger","box-fatal","box-teal");
        selectedColor = "box-primary";
        solid = false;
    }

    public List<String> getBoxColors() {
        return boxColors;
    }

    public void setBoxColors(List<String> boxColors) {
        this.boxColors = boxColors;
    }
    
    
    public void onClose(CloseEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Panel Closed", "Closed panel id:'" + event.getComponent().getId() + "'");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onToggle(ToggleEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, event.getComponent().getId() + " toggled", "Status:" + event.getVisibility().name());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String getSelectedColor() {
        return selectedColor;
    }

    public boolean isSolid() {
        return solid;
    }

    public void setSelectedColor(String selectedColor) {
        this.selectedColor = selectedColor;
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }
    
    
}
