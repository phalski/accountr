package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.User;
import de.thi.phm6101.accountr.exception.EntityExistsException;
import de.thi.phm6101.accountr.service.UserServiceBean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;


/**
 * Named bean used for user sign up
 */
@Named
@ViewScoped
public class UserBean implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(UserBean.class);

    @Inject
    private UserServiceBean userServiceBean;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @PostConstruct
    public void initialize() {
        this.user = new User();
    }

    /**
     * Creates user in database
     * @return outcome
     */
    public String doSignUp() {
        try {
            userServiceBean.insert(user);
        } catch (EntityExistsException e) {
            LOGGER.error(String.format("Failed to create user %s", e));
            return "error";
        }
        return "/accounts.xhtml?faces-redirect=true";
    }
}
