package de.thi.phm6101.accountr.service;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.domain.Transaction;
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


@Stateless
public class AccountrServiceBean {

    private static final Logger LOGGER = LogManager.getLogger(AccountrServiceBean.class);

    @Inject
    private DataAccessBean dab;

    /// ACCOUNT CRUD

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

    public Account insert(Account account) {
        if (equalExists(account)) {
            throw new EntityExistsException(String.format("Account '%s' already exists.", account.getName()));
        }
        dab.insert(account);
        LOGGER.info(String.format("Inserted Account '%s' with id '%s'", account.getName(), account.getId()));
        return account;
    }

    public Account update(Account account) {
        if (!dab.exists(account)) {
            throw new EntityNotFoundException(String.format("Account '%s' does not exist.", account.getName()));
        }
        dab.update(account);
        LOGGER.info(String.format("Updated Account '%s' with id '%s'", account.getName(), account.getId()));
        return account;
    }

    public void delete(Account account) {
        if (!dab.exists(account)) {
            throw new EntityNotFoundException(String.format("Account '%s' does not exist.", account.getName()));
        }
        dab.delete(account);
    }

    public boolean exists(Account account) {
        return dab.exists(account);
    }

    public boolean equalExists(Account account) {
        return !select(account.getName()).isEmpty();
    }


    /// TRANSACTION CRUD

    public Optional<Transaction> selectTransaction(long id) {
        return Optional.ofNullable(dab.get(Transaction.class, id));
    }

    public List<Transaction> selectTransaction() {
        return dab.getAll(Transaction.class);
    }

    public void insertTransaction(Account account, Transaction transaction) {
        account.addTransaction(transaction);
        this.update(account);
    }

    public Transaction updateTransaction(Transaction transaction) {
        if (!dab.exists(transaction)) {
            throw new EntityNotFoundException(String.format("Transaction '%s' does not exist.", transaction.getDescription()));
        }
        dab.update(transaction);
        LOGGER.info(String.format("Updated Transaction '%s'", transaction.getDescription()));
        return transaction;
    }

    public void deleteTransaction(Transaction transaction) {
        Account account = transaction.getAccount();
        account.removeTransaction(transaction);
        this.update(account);
    }

}
