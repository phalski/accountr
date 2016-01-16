package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.exception.EntityExistsException;
import de.thi.phm6101.accountr.exception.EntityNotFoundException;
import de.thi.phm6101.accountr.service.AccountrServiceBean;
import de.thi.phm6101.accountr.util.JsfUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

/**
 * Named bean for all account related logic
 */
@Named
@ViewScoped
public class AccountBean implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(AccountBean.class);

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

    /**
     * Returns all currency codes for usage in EL
     * @return currency code list
     */
    public List<String> getCurrencyCodeList() {
        List<String> currencyCodeList = new ArrayList<>();
        Currency.getAvailableCurrencies().forEach(c -> currencyCodeList.add(c.getCurrencyCode()));
        Collections.sort(currencyCodeList);
        return Collections.unmodifiableList(currencyCodeList);
    }

    /**
     * Returns currency symbol for given code
     * @param currencyCode currency code
     * @return currency symbol or empty string if not found
     */
    public String getCurrencySymbol(String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        return (currency == null) ? "" : currency.getSymbol();
    }

    /// LOGIC

    /**
     * Loads account from db or sets a new one. Also ensures redirection if view is not account-form
     * @return outcome
     */
    public String initialize() {
        Optional<Account> optionalAccount = accountrServiceBean.select(accountId);

        Optional<String> viewIdOptional = jsfUtil.getCurrentViewId();
        if (viewIdOptional.isPresent()
                && !"/account-form.xhtml".equals(viewIdOptional.get())
                && !optionalAccount.isPresent())
        {
            return "error";
        }

        setAccount(optionalAccount.orElse(new Account()));

        if (getIsNewAccount()) {
            LOGGER.info(String.format("AccountBean: Account is new"));
        } else {
            LOGGER.info(String.format("AccountBean: Account has ID '%d'", accountId));
        }

        return null;
    }

    /**
     * Evaluates search GET parameter and fetches matching entities. Loads all entities if search parameter not present.
     * @return outcome
     */
    public void initializeList() {
        String currentSearch = getSearch();
        accountList = (currentSearch == null || currentSearch.isEmpty()) ? accountrServiceBean.select() : accountrServiceBean.select(currentSearch);
        LOGGER.info(String.format("AccountBean: Current list contains %d accounts", accountList.size()));
    }

    public String doSearch() {
        return String.format("accounts.xhtml?faces-redirect=true&search=%s", getSearch());
    }

    public String doInsertOrUpdate() {

        try {
            if (accountrServiceBean.exists(account)) {
                accountrServiceBean.update(account);
                LOGGER.info("AccountBean: Updated account");
            } else if (!accountrServiceBean.equalExists(account)) {
                accountrServiceBean.insert(account);
                LOGGER.info("AccountBean: Inserted account");
            } else {
                LOGGER.error("AccountBean: Cannot insert account");
                return "error";
            }
        } catch (EntityNotFoundException e) {
            LOGGER.error(String.format("AccountBean: %s", e));
            return "error";
        } catch (EntityExistsException e) {
            LOGGER.error(String.format("AccountBean: %s", e));
            return "error";
        }

        return "accounts.xhtml?faces-redirect=true";
    }

    public String doDelete(Account account) {
        LOGGER.info(String.format("doDelete: accountId:%s", account.getId()));
        if (accountrServiceBean.exists(account)) {
            try {
                accountrServiceBean.delete(account);
            } catch (EntityNotFoundException e) {
                LOGGER.error(String.format("AccountBean: %s", e));
                return "error";
            }
        }
        return "accounts.xhtml?faces-redirect=true";
    }
}
