package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.domain.Transaction;
import de.thi.phm6101.accountr.exception.EntityNotFoundException;
import de.thi.phm6101.accountr.service.AccountrServiceBean;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Transient;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

/**
 * Named bean for all transaction related logic.
 */
@Named
@ViewScoped
public class TransactionBean implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(TransactionBean.class);

    private AccountrServiceBean accountrServiceBean;

    // view params

    private long accountId;

    // properties

    private Account account;

    private Transaction transaction;

    @Transient
    private Part part;

    @Inject
    public TransactionBean(AccountrServiceBean accountrServiceBean) {
        this.accountrServiceBean = accountrServiceBean;
    }

    public String initialize() {
        Optional<Account> accountOptional = accountrServiceBean.select(accountId);
        if (accountOptional.isPresent()) {
            account = accountOptional.get();
            transaction = new Transaction();
            LOGGER.info(String.format("TransactionBean: Prepared new transaction for account '%d'", accountId));
        } else {
            LOGGER.error(String.format("TransactionBean: No account found for id %d", accountId));
            return "error";
        }

        return null;
    }


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

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }


    /// ACTION METHODS

    public String doSave() {
        if (part != null) {
            try {
                transaction.setReceiptImage(IOUtils.toByteArray(part.getInputStream()));
            } catch (IOException e) {
                LOGGER.error("Upload failed:" + e);
                return "error";
            }
        }

        if (account != null) {
            try {
                accountrServiceBean.insertTransaction(account, transaction);
            } catch (EntityNotFoundException e) {
                LOGGER.error(String.format("TransactionBean: %s", e));
                return "error";
            }
        }

        return String.format("account.xhtml?faces-redirect=true&accountId=%d", account.getId());
    }

    public String doDelete(Transaction transaction) {
        LOGGER.info(String.format("Deleting transaction %d", transaction.getId()));

        try {
            accountrServiceBean.deleteTransaction(transaction);
        } catch (EntityNotFoundException e) {
            LOGGER.error(String.format("TransactionBean: %s", e));
            return "error";
        }

        return String.format("account.xhtml?faces-redirect=true&accountId=%d", transaction.getAccount().getId());
    }

}
