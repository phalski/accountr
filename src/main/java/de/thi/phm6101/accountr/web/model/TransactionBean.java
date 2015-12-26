package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.domain.Transaction;
import de.thi.phm6101.accountr.service.AccountrServiceBean;
import net.bootsfaces.render.A;
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
        Optional<Account> accountOptional = accountrServiceBean.selectAccount(accountId);
        if (accountOptional.isPresent()) {
            account = accountOptional.get();
            Optional<Transaction> transactionOptional = accountrServiceBean.selectTransaction(transactionId);
            isNewTransaction = !transactionOptional.isPresent();
            setTransaction(transactionOptional.orElse(new Transaction()));
            if (transactionOptional.isPresent()) {
                LOGGER.info(String.format("initialize: Account-ID: %d, Transaction-ID: %d", accountId, transactionId));
            } else {
                LOGGER.info(String.format("initialize: Account-ID: %d, Transaction-ID: -", accountId));
            }
        }

    }


    private long accountId;

    private Account account;

    private boolean isInitialized = false;

    private boolean isNewTransaction;

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

    public boolean getIsInitialized() {
        return isInitialized;
    }

    public boolean getIsNewTransaction() {
        return isNewTransaction;
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

    public String doSave() {
        if (isNewTransaction) {
            account.addTransaction(transaction);
            accountrServiceBean.update(account);
        } else {
            accountrServiceBean.updateTransaction(transaction);
        }

        return String.format("account.xhtml?faces-redirect=true&accountId=%d", account.getId());
    }

    public String doCancel() {
        return String.format("account.xhtml?faces-redirect=true&accountId=%d", account.getId());
    }

    public String doDelete(Transaction transaction) {
        LOGGER.info(String.format("Deleting transaction %d", transaction.getId()));

        if (accountrServiceBean.existsTransaction(transaction)) {
            accountrServiceBean.deleteTransaction(transaction);
        }
//        if (accountrServiceBean.exists(transaction.getAccount())) {
//            Account account = transaction.getAccount();
//            account.removeTransaction(transaction);
//            this.account = accountrServiceBean.update(account);
//        } else {
//            LOGGER.warn("doDelete: Transaction has no account");
//        }
        return String.format("account.xhtml?faces-redirect=true&accountId=%d", transaction.getAccount().getId());
    }

}
