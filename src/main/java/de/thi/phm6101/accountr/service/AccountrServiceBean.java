package de.thi.phm6101.accountr.service;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.exception.EntityAlreadyExistsException;
import de.thi.phm6101.accountr.persistence.DataAccessBean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by philipp on 08/12/15.
 */
@Stateless
public class AccountrServiceBean {

    private static final Logger LOGGER = LogManager.getLogger(AccountrServiceBean.class);

    @Inject
    private DataAccessBean dab;

    public List<Account> accountList() {
        List<Account> accounts = dab.getAll(Account.class);
        LOGGER.info(String.format("Find all returned %d elements", accounts.size()));
        return accounts;
    }

    public Optional<Account> select(long id) {
        return Optional.ofNullable(dab.get(Account.class, id));
    }

    public List<Account> select() {
        return dab.getAll(Account.class);
    }

    public List<Account> select(String name) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        return dab.namedQuery(Account.class,"findByName", parameters);
    }

    public Account insert(Account account) throws EntityExistsException {
        if (dab.getAll(Account.class).contains(account)) {
            throw new EntityExistsException(String.format("Account '%s' already exists.", account.getName()));
        }
        dab.insert(account);
        LOGGER.info(String.format("Inserted Account '%s' with id '%s'", account.getName(), account.getId()));
        return account;
    }

    public Account update(Account account) throws EntityNotFoundException {
        if (!dab.getAll(Account.class).contains(account)) {
            throw new EntityNotFoundException(String.format("Account '%s' does not exist.", account.getName()));
        }
        dab.update(account);
        LOGGER.info(String.format("Updated Account '%s' with id '%s'", account.getName(), account.getId()));
        return account;
    }

    public void delete(Account account) throws EntityNotFoundException {
        if (!dab.getAll(Account.class).contains(account)) {
            throw new EntityNotFoundException(String.format("Account '%s' does not exist.", account.getName()));
        }
        dab.delete(account);
    }

}
