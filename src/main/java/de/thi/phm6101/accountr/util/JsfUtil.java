package de.thi.phm6101.accountr.util;


import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
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
}
