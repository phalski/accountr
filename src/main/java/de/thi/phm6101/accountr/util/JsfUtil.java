package de.thi.phm6101.accountr.util;


import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Optional;

@Stateless
@PermitAll
public class JsfUtil {

    /**
     * Returns current view id
     * Encapsulated for test mocking
     * @return view id
     */
    public Optional<String> getCurrentViewId() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIViewRoot uiViewRoot = facesContext.getViewRoot();
        return (uiViewRoot == null) ? Optional.empty() : Optional.ofNullable(uiViewRoot.getViewId());
    }

    public HttpServletRequest getRequest() {
        FacesContext context = FacesContext.getCurrentInstance();
        return (HttpServletRequest)
                context.getExternalContext().getRequest();
    }

    public FacesContext getContext() {
        return FacesContext.getCurrentInstance();
    }
}
