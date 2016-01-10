package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.User;
import de.thi.phm6101.accountr.service.UserServiceBean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Named
@ViewScoped
public class UserBean implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(UserBean.class);

    @Inject
    private UserServiceBean userServiceBean;

    private long userId;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @PostConstruct
    public void initialize() {
        Optional<User> optionalAccount = userServiceBean.select(userId);
        setUser(optionalAccount.orElse(new User()));

        if (optionalAccount.isPresent()) {
            LOGGER.info(String.format("initialize: User-ID: %s", userId));
        } else {
            LOGGER.info("initialize: User-ID: -");
        }
    }

    public String doSignUp() {
        try {
            userServiceBean.insert(user);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(String.format("Failed to create user %s", user.getName()));
        }
        return "/accounts.xhtml?faces-redirect=true";
    }
}
