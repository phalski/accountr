package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.domain.Transaction;
import de.thi.phm6101.accountr.service.AccountrServiceBean;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
        LOGGER.info(String.format("initialize: Account-ID: %d, Transaction-ID: %d", accountId, transactionId));

        Optional<Account> accountOptional = accountrServiceBean.select(accountId);
        if (accountOptional.isPresent()) {
            account = accountOptional.get();
            transaction = new Transaction();
        } else {
            // update transaction
            Optional<Transaction> transactionOptional = accountrServiceBean.selectTransaction(transactionId);
            transaction = transactionOptional.orElse(null);
        }

        if (transaction == null) {
            LOGGER.error("No transaction created");
        };
    }

    // view params

    private long accountId;

    private long transactionId;

    // properties

    private Account account;

    private Transaction transaction;

    private Part part;

    /// GET/SET

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
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
        upload();
        insertOrUpdate();

        return String.format("account.xhtml?faces-redirect=true&accountId=%d", account.getId());
    }

    public String doCancel() {
        return String.format("account.xhtml?faces-redirect=true&accountId=%d", account.getId());
    }

    public String doDelete(Transaction transaction) {
        LOGGER.info(String.format("Deleting transaction %d", transaction.getId()));

        accountrServiceBean.deleteTransaction(transaction);

        return String.format("account.xhtml?faces-redirect=true&accountId=%d", transaction.getAccount().getId());
    }

    // UTIL

    private void insertOrUpdate() {
        if (account != null) {
            accountrServiceBean.insertTransaction(account, transaction);
        } else if (transaction.getAccount() != null) {
            accountrServiceBean.updateTransaction(transaction);
        }
    }

    private void upload() {
        if (part != null) {
            try {
                transaction.setReceiptImage(IOUtils.toByteArray(part.getInputStream()));
            } catch (IOException e) {
                LOGGER.error("Upload failed:" + e.toString());
            }
        }
    }




}
