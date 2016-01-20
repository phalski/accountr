package de.thi.phm6101.accountr.validation;

import de.thi.phm6101.accountr.service.AccountrServiceBean;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Validator to ensure unique account name
 */
@Named
@RequestScoped
public class AccountNameValidator implements Validator {

    private AccountrServiceBean accountrServiceBean;

    @Inject
    public AccountNameValidator(AccountrServiceBean accountrServiceBean) {
        this.accountrServiceBean = accountrServiceBean;
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {

        String name = (String) value;

        if (!accountrServiceBean.select(name).isEmpty()) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name is not available", "Name is not available");

            throw new ValidatorException(facesMessage);
        }

    }

}
