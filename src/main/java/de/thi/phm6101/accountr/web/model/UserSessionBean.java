package de.thi.phm6101.accountr.web.model;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.enterprise.context.RequestScoped;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.inject.Named;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@Named
@RequestScoped
public class UserSessionBean implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(UserSessionBean.class);

    private String login;

    private String password;

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
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)
                context.getExternalContext().getRequest();

        try {
            request.login(this.login, this.password);
        } catch (ServletException e) {
            e.printStackTrace();
            LOGGER.error(e);
            return "/login-error.xhtml";
        }
        if("/login.xhtml".equals(context.getViewRoot().getViewId()) || "/login-error.xhtml".equals(context.getViewRoot().getViewId())) {
            return "/accounts.xhtml";
        }
        return null;
    }

    public String doSignOut() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
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
