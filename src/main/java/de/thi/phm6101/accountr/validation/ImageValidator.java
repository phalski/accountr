package de.thi.phm6101.accountr.validation;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

@Named
@RequestScoped
public class ImageValidator implements Validator {


    private static long MAX_FILE_SIZE = 1000000;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        Part file = (Part) value;
        if (file != null) {
            if (!"image/jpeg".equals(file.getContentType())
                    && !"image/png".equals(file.getContentType())
                    && !"image/gif".equals(file.getContentType())) {
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "File is not an image", "File is not an image");

                throw new ValidatorException(facesMessage);
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Image is too large", "Image is too large");

                throw new ValidatorException(facesMessage);
            }
        }
    }

}
