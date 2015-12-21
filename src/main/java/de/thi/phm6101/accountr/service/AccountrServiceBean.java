package de.thi.phm6101.accountr.service;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.exception.EntityAlreadyExistsException;
import de.thi.phm6101.accountr.persistence.DataAccessBean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by philipp on 08/12/15.
 */
@Stateless
public class AccountrServiceBean {

    private static final Logger LOGGER = LogManager.getLogger(AccountrServiceBean.class);

    @Inject
    private DataAccessBean dab;

    public List<Account> findAccountsByName(String name) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        return dab.namedQuery(Account.class,"findByName", parameters);
    }

    public List<Account> accountList() {
        List<Account> accounts = dab.getAll(Account.class);
        LOGGER.info(String.format("Find all returned %d elements", accounts.size()));
        return accounts;
    }

    public Account insert(Account account) throws EntityAlreadyExistsException {
        if (dab.getAll(Account.class).contains(account)) {
            throw new EntityAlreadyExistsException(String.format("Account '%s' already exists.", account.getName()));
        }
        dab.insert(account);
        LOGGER.info(String.format("Inserted Account '%s' with id '%s'", account.getName(), account.getId()));
        return account;
    }

}
