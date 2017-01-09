package com.github.adminfaces.showcase.bean;

import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by rmpestano on 07/01/17.
 */
@Named
@ViewScoped
public class ErrorMB implements Serializable {

    @PostConstruct
    public void init(){
        throw new RuntimeException("error...");
    }

}
