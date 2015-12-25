package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.domain.Transaction;
import de.thi.phm6101.accountr.service.AccountrServiceBean;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by philipp on 08/12/15.
 */
@Named
@ViewScoped
public class AccountsBean implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(AccountsBean.class);

    @Inject
    private AccountrServiceBean accountrServiceBean;

    private String filter;

    private List<Account> filteredAccounts;

    private Account account;


    public void initialize() {
        setFilteredAccounts(accountrServiceBean.selectAccount());
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
        return accountrServiceBean.selectAccount();
    }

    public void setFilteredAccounts(List<Account> filteredAccounts) {
        this.filteredAccounts = filteredAccounts;
    }



    //
    // ACTIONS
    //

    public void doFilter() {
        LOGGER.info(String.format("Filtering results with filter: %s", getFilter()));
        setFilteredAccounts(accountrServiceBean.selectAccount());
    }

    public String doInsert() {
        Account a = new Account();
        a.setName("First Account");
        a.setDescription("First account ever");
        Transaction t = new Transaction();
        t.setDate(new Date());
        t.setDescription("Test transaction");
        a.addTransaction(t);
        accountrServiceBean.insert(a);
        return "accounts";
    }

}
