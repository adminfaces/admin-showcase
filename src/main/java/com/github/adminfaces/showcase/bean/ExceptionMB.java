package com.github.adminfaces.showcase.bean;

import org.omnifaces.cdi.ViewScoped;

import javax.faces.application.ViewExpiredException;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by rmpestano on 07/01/17.
 */
@Named
@ViewScoped
public class ExceptionMB implements Serializable {

    public void throwRuntime() {
        throw new RuntimeException("this is a runtime exception...");
    }

    public void throwViewExpired() {
        throw new ViewExpiredException("this is a view expired exception...");
    }

}
