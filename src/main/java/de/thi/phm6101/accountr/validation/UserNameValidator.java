package de.thi.phm6101.accountr.validation;

import de.thi.phm6101.accountr.service.UserServiceBean;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class UserNameValidator implements Validator {

    @Inject
    private UserServiceBean userServiceBean;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        String name = (String) value;

        if (!userServiceBean.select(name).isEmpty()) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Name is not available", "Name is not available");

            throw new ValidatorException(facesMessage);
        }

    }

}