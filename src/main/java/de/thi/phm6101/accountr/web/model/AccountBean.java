package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.service.AccountrServiceBean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Named
@ViewScoped
public class AccountBean implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(AccountBean.class);

    @Inject
    private AccountrServiceBean accountrServiceBean;

    private Account account;

    private long accountId;

    private List<Account> accountList;

    private boolean isNewAccount;

    private String search = "";

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

    public boolean getIsNewAccount() {
        return isNewAccount;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    /// LOGIC

    @PostConstruct
    public void initialize() {
        Optional<Account> optionalAccount = accountrServiceBean.select(accountId);
        setAccount(optionalAccount.orElse(new Account()));
        isNewAccount = !optionalAccount.isPresent();

        if (optionalAccount.isPresent()) {
            LOGGER.info(String.format("initialize: Account-ID: %s", accountId));
        } else {
            LOGGER.info("initialize: Account-ID: -");
        }
    }

    public void initializeList() {
        accountList = getSearch().isEmpty() ? accountrServiceBean.select() : accountrServiceBean.select(getSearch());
    }

    public String doSearch() {
        return String.format("accounts.xhtml?faces-redirect=true&search=%s", getSearch());
    }

    public String doInsertOrUpdate() {
        LOGGER.info(String.format("Account %s %s %s", account.getId(), account.getName(), account.getDescription()));
        if (accountrServiceBean.exists(account)) {
            accountrServiceBean.update(account);
        } else {
            accountrServiceBean.insert(account);
        }

        return "accounts.xhtml?faces-redirect=true";
    }

    public String doDelete(Account account) {
        LOGGER.info(String.format("doDelete: accountId:%s", account.getId()));
        if (accountrServiceBean.exists(account)) {
            accountrServiceBean.delete(account);
        }
        return "accounts.xhtml?faces-redirect=true";
    }
}
