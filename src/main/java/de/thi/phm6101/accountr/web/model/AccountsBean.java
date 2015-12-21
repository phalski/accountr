package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.service.AccountrServiceBean;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by philipp on 08/12/15.
 */
@Named
@ViewScoped
public class AccountsBean {

    private static final Logger LOGGER = LogManager.getLogger(AccountsBean.class);

    @Inject
    private AccountrServiceBean accountrServiceBean;

    private String filter;

    private List<Account> filteredAccounts;

    private Account account;

    public AccountsBean() {
        LOGGER.info("AccountsBean created");
    }

    public void initialize() {
        setFilteredAccounts(accountrServiceBean.findAll());
    }

    //
    // GET/SET
    //

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<Account> getfilteredAccounts() {
        return filteredAccounts;
    }

    public void setFilteredAccounts(List<Account> filteredAccounts) {
        this.filteredAccounts = filteredAccounts;
    }


    //
    // ACTIONS
    //

    public void doFilter() {
        LOGGER.info(String.format("Filtering results with filter: %s", getFilter()));
        setFilteredAccounts(accountrServiceBean.findAll());
    }

    public String doInsert() {
        Account a = new Account();
        a.setName("First");
        a.setDescription("First account ever");
        accountrServiceBean.doInsert(a);
        return "accounts";
    }

}
