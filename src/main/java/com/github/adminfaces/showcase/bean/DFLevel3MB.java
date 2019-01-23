package com.github.adminfaces.showcase.bean;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;

import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by rafael-pestano on 22/06/17.
 */
@Named("dfLevel3MB")
@ViewScoped
public class DFLevel3MB implements Serializable {

    private String val;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public void closeDialog() {
        //pass back to level 2
        PrimeFaces.current().dialog().closeDynamic(val);
    }
}
