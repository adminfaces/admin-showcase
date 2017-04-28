package com.github.adminfaces.showcase.bean;

import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmpestano on 28/04/17.
 */
@Named
@ViewScoped
public class MultiSelectBean implements Serializable {

    private List<SelectItem> categories;
    private String selection;

    @PostConstruct
    public void init() {
        categories = new ArrayList<SelectItem>();
        SelectItemGroup group1 = new SelectItemGroup("Group 1");
        SelectItemGroup group2 = new SelectItemGroup("Group 2");
        SelectItemGroup group3 = new SelectItemGroup("Group 3");
        SelectItemGroup group4 = new SelectItemGroup("Group 4");

        SelectItemGroup group11 = new SelectItemGroup("Group 1.1");
        SelectItemGroup group12 = new SelectItemGroup("Group 1.2");

        SelectItemGroup group21 = new SelectItemGroup("Group 2.1");

        SelectItem option31 = new SelectItem("Option 3.1", "Option 3.1");
        SelectItem option32 = new SelectItem("Option 3.2", "Option 3.2");
        SelectItem option33 = new SelectItem("Option 3.3", "Option 3.3");
        SelectItem option34 = new SelectItem("Option 3.4", "Option 3.4");

        SelectItem option41 = new SelectItem("Option 4.1", "Option 4.1");

        SelectItem option111 = new SelectItem("Option 1.1.1");
        SelectItem option112 = new SelectItem("Option 1.1.2");
        group11.setSelectItems(new SelectItem[]{option111, option112});

        SelectItem option121 = new SelectItem("Option 1.2.1", "Option 1.2.1");
        SelectItem option122 = new SelectItem("Option 1.2.2", "Option 1.2.2");
        SelectItem option123 = new SelectItem("Option 1.2.3", "Option 1.2.3");
        group12.setSelectItems(new SelectItem[]{option121, option122, option123});

        SelectItem option211 = new SelectItem("Option 2.1.1", "Option 2.1.1");
        group21.setSelectItems(new SelectItem[]{option211});

        group1.setSelectItems(new SelectItem[]{group11, group12});
        group2.setSelectItems(new SelectItem[]{group21});
        group3.setSelectItems(new SelectItem[]{option31, option32, option33, option34});
        group4.setSelectItems(new SelectItem[]{option41});

        categories.add(group1);
        categories.add(group2);
        categories.add(group3);
        categories.add(group4);
    }

    public List<SelectItem> getCategories() {
        return categories;
    }

    public String getSelection() {
        return selection;
    }
    public void setSelection(String selection) {
        this.selection = selection;
    }
}