package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.exception.EntityExistsException;
import de.thi.phm6101.accountr.exception.EntityNotFoundException;
import de.thi.phm6101.accountr.service.AccountrServiceBean;
import de.thi.phm6101.accountr.util.JsfUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.lf5.util.StreamUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class AccountBean implements Serializable {

    private static final Logger logger = LogManager.getLogger(AccountBean.class);

    private AccountrServiceBean accountrServiceBean;
    private JsfUtil jsfUtil;

    private Account account;
    private long accountId;
    private List<Account> accountList;
    private String search = "";

    @Inject
    public AccountBean(AccountrServiceBean accountrServiceBean, JsfUtil jsfUtil) {
        this.accountrServiceBean = accountrServiceBean;
        this.jsfUtil = jsfUtil;
    }

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

    public String getCurrencySymbol(String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        return (currency == null) ? "" : currency.getSymbol();
    }

    /// LOGIC

    public String initialize() {
        Optional<Account> optionalAccount = accountrServiceBean.select(accountId);

        Optional<String> viewIdOptional = jsfUtil.getCurrentViewId();
        if (viewIdOptional.isPresent()
                && !viewIdOptional.get().equals("/account-form.xhtml")
                && !optionalAccount.isPresent())
        {
            return "error";
        }

        setAccount(optionalAccount.orElse(new Account()));

        if (getIsNewAccount()) {
            logger.info(String.format("AccountBean: Account is new"));
        } else {
            logger.info(String.format("AccountBean: Account has ID '%d'", accountId));
        }

        return null;
    }

    public void initializeList() {
        String search = getSearch();
        accountList = (search == null || search.isEmpty()) ? accountrServiceBean.select() : accountrServiceBean.select(search);
        logger.info(String.format("AccountBean: Current list contains %d accounts", accountList.size()));
    }

    public String doSearch() {
        return String.format("accounts.xhtml?faces-redirect=true&search=%s", getSearch());
    }

    public String doInsertOrUpdate() {

        try {
            if (accountrServiceBean.exists(account)) {
                accountrServiceBean.update(account);
                logger.info("AccountBean: Updated account");
            } else if (!accountrServiceBean.equalExists(account)) {
                accountrServiceBean.insert(account);
                logger.info("AccountBean: Inserted account");
            } else {
                logger.error("AccountBean: Cannot insert account");
                return "error";
            }
        } catch (EntityNotFoundException e) {
            logger.error(String.format("AccountBean: %s", e));
            return "error";
        } catch (EntityExistsException e) {
            logger.error(String.format("AccountBean: %s", e));
            return "error";
        }

        return "accounts.xhtml?faces-redirect=true";
    }

    public String doDelete(Account account) {
        logger.info(String.format("doDelete: accountId:%s", account.getId()));
        if (accountrServiceBean.exists(account)) {
            try {
                accountrServiceBean.delete(account);
            } catch (EntityNotFoundException e) {
                logger.error(String.format("AccountBean: %s", e));
                return "error";
            }
        }
        return "accounts.xhtml?faces-redirect=true";
    }
}
