package com.github.adminfaces.showcase.bean;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.github.adminfaces.template.exception.BusinessException;
import org.omnifaces.cdi.ViewScoped;

import javax.faces.application.ViewExpiredException;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import java.io.Serializable;

/**
 * Created by rmpestano on 07/01/17.
 */
@Named
@ViewScoped
public class ExceptionMB implements Serializable {


    public void throwBusinessException() {
        throw new BusinessException("This kind of exception generates a faces message with severity <code>error</code>.");
    }

    public void multipleBusinessException() {
        throw new BusinessException().
                addException(new BusinessException("Exception 1")).
                addException(new BusinessException("Exception 2")).
                addException(new BusinessException("Exception 3"));
    }

    public void throwRuntime() {
        throw new RuntimeException("this is a runtime exception...");
    }

    public void throwAccessDenied() {
        throw new AccessDeniedException("this is an access denied exception...");
    }

    public void throwViewExpired() {
        throw new ViewExpiredException("this is a view expired exception...");
    }

    public void throwOptimisticLock() {
        throw new OptimisticLockException("this is a optimistic lock exception...");
    }
}
