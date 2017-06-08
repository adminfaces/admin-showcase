/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.adminfaces.showcase.bean;

import com.github.adminfaces.showcase.model.Theme;
import com.github.adminfaces.showcase.service.ThemeService;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.SelectEvent;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class AutoCompleteMB implements Serializable {
    
    private Theme theme;
    private Theme theme2;
    private List<Theme> selectedThemes;
    
    @Inject
    private ThemeService service;
    
    public List<String> completeText(String query) {
		List<String> results = new ArrayList<String>();
		for(int i = 0; i < 10; i++) {
			results.add(query + i);
		}
		
		return results;
	}
    
    public List<Theme> completeTheme(String query) {
        List<Theme> allThemes = service.getThemes();
		List<Theme> filteredThemes = new ArrayList<Theme>();
        
        for (int i = 0; i < allThemes.size(); i++) {
            Theme skin = allThemes.get(i);
            if(skin.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredThemes.add(skin);
            }
        }
        
        return filteredThemes;
	}
    
    public List<Theme> completeThemeContains(String query) {
        List<Theme> allThemes = service.getThemes();
		List<Theme> filteredThemes = new ArrayList<Theme>();
        
        for (int i = 0; i < allThemes.size(); i++) {
            Theme skin = allThemes.get(i);
            if(skin.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredThemes.add(skin);
            }
        }
        
        return filteredThemes;
	}


        
    public void onItemSelect(SelectEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item Selected", event.getObject().toString()));
    }


    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme1) {
        this.theme = theme1;
    }

    public Theme getTheme2() {
        return theme2;
    }

    public void setTheme2(Theme theme2) {
        this.theme2 = theme2;
    }

    public List<Theme> getSelectedThemes() {
        return selectedThemes;
    }

    public void setSelectedThemes(List<Theme> selectedThemes) {
        this.selectedThemes = selectedThemes;
    }
    
    public void setService(ThemeService service) {
        this.service = service;
    }
    
    public char getThemeGroup(Theme theme) {
        return theme.getDisplayName().charAt(0);
    }

}
