package com.github.adminfaces.showcase.bean;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 * Created by rmpestano on 23/01/17.
 */
@Model
public class ButtonMB {

    public void buttonAction(ActionEvent actionEvent) {
        addMessage("Welcome to AdminFaces!!");
    }

    public void save(ActionEvent actionEvent) {
        addMessage("Data saved");
    }

    public void update(ActionEvent actionEvent) {
        addMessage("Data updated");
    }

    public void delete(ActionEvent actionEvent) {
        addMessage("Data deleted");
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}