package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.domain.Transaction;
import de.thi.phm6101.accountr.service.AccountrServiceBean;
import de.thi.phm6101.accountr.service.FileServiceBean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Optional;
import java.util.Scanner;

/**
 * Created by philipp on 22/12/15.
 */
@Named
@ViewScoped
public class TransactionBean implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(TransactionBean.class);

    @Inject
    private AccountrServiceBean accountrServiceBean;

    @Inject
    private FileServiceBean fileServiceBean;

    @PostConstruct
    public void initialize() {
        Optional<Account> accountOptional = accountrServiceBean.select(accountId);
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

    private Part receiptImage;

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

    public Part getReceiptImage() {
        return receiptImage;
    }

    public void setReceiptImage(Part receiptImage) {
        this.receiptImage = receiptImage;
    }

    /// ACTION METHODS

    public String doSave() {
        try {
            uploadReceipt();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        Account account = transaction.getAccount();
        account.removeTransaction(transaction);
        accountrServiceBean.update(account);

        return String.format("account.xhtml?faces-redirect=true&accountId=%d", transaction.getAccount().getId());
    }

    public void uploadReceipt() throws IOException {
        if (receiptImage != null && transaction != null) {
            String fileName = fileServiceBean.buildFileName(receiptImage);
            fileServiceBean.uploadFile(receiptImage, fileName);
            transaction.setReceiptFileName(fileName);
        }

    }


}
