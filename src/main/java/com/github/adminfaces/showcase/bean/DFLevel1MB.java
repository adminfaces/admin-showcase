package com.github.adminfaces.showcase.bean;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rafael-pestano on 22/06/17.
 */
@Named("dfLevel1MB")
@ViewScoped
public class DFLevel1MB implements Serializable {

    public void openLevel2() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("appendTo", "@(body)");
        options.put("styleClass", "dlg2");
        PrimeFaces.current().dialog().openDynamic("level2", options, null);
    }

    public void onReturnFromLevel2(SelectEvent event) {
        //pass back to root
        PrimeFaces.current().dialog().closeDynamic(event.getObject());
    }
}
