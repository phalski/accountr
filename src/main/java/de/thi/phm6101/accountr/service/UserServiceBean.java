package de.thi.phm6101.accountr.service;

import de.thi.phm6101.accountr.domain.User;
import de.thi.phm6101.accountr.persistence.DataAccessBean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import de.thi.phm6101.accountr.exception.EntityExistsException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Stateless
public class UserServiceBean {

    private static final Logger LOGGER = LogManager.getLogger(AccountrServiceBean.class);

    @Inject
    private DataAccessBean dab;

    // USER CR

    public Optional<User> select(long id) {
        return Optional.ofNullable(dab.get(User.class, id));
    }

    public List<User> select(String name) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        return dab.namedQuery(User.class, "user.findByName", parameters);
    }

    public User insert(User user) throws EntityExistsException {
        if (equalExists(user)) {
            throw new EntityExistsException(String.format("User '%s' already exists.", user.getName()));
        }

        try {
            user.setPassword(encodePassword(user.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            LOGGER.warn("UserServiceBean: storing unencrypted password - " + e);
            user.setPassword(user.getPassword());
        }

        dab.insert(user);
        LOGGER.info(String.format("Inserted user '%s' with id '%s'", user.getName(), user.getId()));
        return user;
    }

    public boolean equalExists(User user) {
        return !select(user.getName()).isEmpty();
    }

    /**
     * Password hashing
     * @param password Password
     * @return hashed password
     * @throws NoSuchAlgorithmException
     */
    private String encodePassword(String password) throws NoSuchAlgorithmException {
        return Base64.getEncoder().encodeToString(
                MessageDigest.getInstance("SHA-256").digest(password.getBytes()));
    }

}
