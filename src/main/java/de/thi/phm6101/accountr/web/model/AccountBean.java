package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.service.AccountrServiceBean;
import de.thi.phm6101.accountr.validation.MessageFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class AccountBean implements Serializable {

    private static final Logger logger = LogManager.getLogger(AccountBean.class);

    @Inject
    private AccountrServiceBean accountrServiceBean;

    private Account account;

    private long accountId;

    private List<Account> accountList;

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
        return account == null || account.getId() == 0;
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

    public List<String> getCurrencyCodeList() {
        List<String> currencyCodeList = new ArrayList<>();
        Currency.getAvailableCurrencies().forEach(c -> currencyCodeList.add(c.getCurrencyCode()));
        Collections.sort(currencyCodeList);
        return Collections.unmodifiableList(currencyCodeList);
    }

    /// LOGIC

    public void initialize() {
        Optional<Account> optionalAccount = accountrServiceBean.select(accountId);
        setAccount(optionalAccount.orElse(new Account()));

        if (getIsNewAccount()) {
            logger.info(String.format("AccountBean: Account is new"));
        } else {
            logger.info(String.format("AccountBean: Account has ID '%d'", accountId));
        }
    }

    public void initializeList() {
        accountList = getSearch().isEmpty() ? accountrServiceBean.select() : accountrServiceBean.select(getSearch());
        logger.info(String.format("AccountBean: Current list contains %d accounts", accountList.size()));
    }

    public String doSearch() {
        return String.format("accounts.xhtml?faces-redirect=true&search=%s", getSearch());
    }

    public String doInsertOrUpdate() {
        logger.info(String.format("Account %s %s %s", account.getId(), account.getName(), account.getDescription()));

        if (accountrServiceBean.exists(account)) {
            accountrServiceBean.update(account);
        } else {
            accountrServiceBean.insert(account);
        }


        return "accounts.xhtml?faces-redirect=true";
    }

    public String doDelete(Account account) {
        logger.info(String.format("doDelete: accountId:%s", account.getId()));
        if (accountrServiceBean.exists(account)) {
            accountrServiceBean.delete(account);
        }
        return "accounts.xhtml?faces-redirect=true";
    }
}
