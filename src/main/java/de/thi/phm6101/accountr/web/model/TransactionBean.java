package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.domain.Transaction;
import de.thi.phm6101.accountr.service.AccountrServiceBean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Optional;

/**
 * Created by philipp on 22/12/15.
 */
@Named
@ViewScoped
public class TransactionBean implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(TransactionBean.class);

    @Inject
    private AccountrServiceBean accountrServiceBean;

    @PostConstruct
    public void initialize() {
        Optional<Transaction> transactionOptional = accountrServiceBean.selectTransaction(transactionId);
        setTransaction(transactionOptional.orElse(new Transaction()));
        LOGGER.info("initialized");
    }


    private long accountId;

    private Account account;

    private long transactionId;

    private Transaction transaction;

    /// GET/SET


    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    /// ACTION METHODS

    public String doInsertOrUpdate() {
        Optional<Account> accountOptional = Optional.ofNullable(getAccount());
        if (accountOptional.isPresent()) {
            accountrServiceBean.update(accountOptional.get());
        }

        return null;
    }

}
