package de.thi.phm6101.accountr.service;

import de.thi.phm6101.accountr.domain.Account;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by philipp on 08/12/15.
 */
@Stateless
public class AccountrServiceBean {

    private static final Logger LOGGER = LogManager.getLogger(AccountrServiceBean.class);

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    public List<Account> findAccountsByName(String name) {
        TypedQuery<Account> query = em.createQuery("SELECT a FROM Account as a WHERE a.name LIKE :name", Account.class);
        query.setParameter("name", name + "%");
        return query.getResultList();
    }

    public List<Account> findAll() {
        TypedQuery<Account> query = em.createQuery("SELECT account FROM Account as account", Account.class);
        LOGGER.debug(String.format("Find all returned %d elements", query.getResultList().size()));
        return query.getResultList();
    }

    public Account doInsert(Account account) {
        em.persist(account);
        LOGGER.info(String.format("Inserted Account with id %d", account.getId()));
        return account;
    }
}
