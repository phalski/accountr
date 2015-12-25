package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.service.AccountrServiceBean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Optional;

@Named
@ViewScoped
public class AccountBean implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(AccountBean.class);

    @Inject
    private AccountrServiceBean accountrServiceBean;

    private Account account;

    private long accountId;

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

    /// LOGIC

    @PostConstruct
    public void initialize() {
        Optional<Account> optionalAccount = accountrServiceBean.selectAccount(accountId);
        setAccount(optionalAccount.orElse(new Account()));
        LOGGER.info("initialized");
    }

    public String doInsertOrUpdate() {
        LOGGER.info(String.format("Account %s %s %s", account.getId(), account.getName(), account.getDescription()));
        if (accountrServiceBean.exists(account)) {
            accountrServiceBean.update(account);
        } else {
            accountrServiceBean.insert(account);
        }

        return null;
    }

    public String doDelete() {
        if (accountrServiceBean.exists(account)) {
            accountrServiceBean.delete(account);
        }
        return null;
    }
}
