package com.github.adminfaces.showcase.security;

import com.github.adminfaces.template.security.AdminSession;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Specializes;

/**
 * Created by rmpestano on 07/01/17.
 */
@Specializes
@SessionScoped
public class SessionConfig extends AdminSession {


    /**
     * in showcase user is always logged in
     */
    @Override
    public boolean isLoggedIn() {
        return true;
    }
}
