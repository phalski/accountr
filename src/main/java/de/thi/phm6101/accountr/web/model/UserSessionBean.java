package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.util.JsfUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.enterprise.context.RequestScoped;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.inject.Inject;
import javax.inject.Named;

import javax.persistence.Transient;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;

/**
 * Named bean for user session handling
 */
@Named
@RequestScoped
public class UserSessionBean implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(UserSessionBean.class);

    @Transient
    private JsfUtil jsfUtil;

    private String login;

    private String password;

    @Inject
    public UserSessionBean(JsfUtil jsfUtil) {
        this.jsfUtil = jsfUtil;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String doSignIn() {
        try {
            jsfUtil.getRequest().login(this.login, this.password);
        } catch (ServletException e) {
            LOGGER.error("UserSessionBean: " + e);
            return "/login-error.xhtml";
        }
        if ("/login.xhtml".equals(jsfUtil.getCurrentViewId().get()) || "/login-error.xhtml".equals(jsfUtil.getCurrentViewId().get())) {
            return "/accounts.xhtml";
        }
        return null;
    }

    public String doSignOut() {
        FacesContext facesContext = jsfUtil.getContext();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.invalidateSession();
        externalContext.setResponseStatus(401);

        try {
            externalContext.getResponseOutputWriter().write("<html><head><meta http-equiv='refresh' content='0;accounts.xhtml'></head></html>");
        } catch (IOException e) {
            LOGGER.error(String.format("UserSessionBean: %s", e));
            return "error";
        } finally {
            facesContext.responseComplete();
        }
        return null;
    }

}
