package de.thi.phm6101.accountr.validation;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class MessageFactory {

    public static FacesMessage newMessage(FacesContext context, FacesMessage.Severity severity,
                                          String msgKey, Object... args) {
        ResourceBundle bundle = ResourceBundle.getBundle(
                context.getApplication().getMessageBundle(),
                context.getViewRoot().getLocale());
        String msg = bundle.getString(msgKey);

        if (args != null) {
            MessageFormat format = new MessageFormat(msg);
            msg = format.format(args);
        }

        return new FacesMessage(severity, msg, null);
    }
}
