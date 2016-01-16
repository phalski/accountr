package de.thi.phm6101.accountr.service;

import de.thi.phm6101.accountr.domain.Account;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.transaction.Transactional;
import java.util.List;

@WebService
@Stateless
public class AccountrSoapService {

    @EJB
    private AccountrServiceBean accountrServiceBean;

    @WebMethod
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public List<Account> accounts() {
        List<Account> accounts = accountrServiceBean.select();
        // clear to prevent recursion
        accounts.forEach(a -> a.getTransactions().stream().forEach(t -> t.setAccount(null)));

        return accounts;
    }

    @WebMethod
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Account accountById(@WebParam(name="id") Long id) {
        Account account = accountrServiceBean.select(id).get();
        if (account != null) {
            // clear to prevent recursion
            account.getTransactions().stream().forEach(t -> t.setAccount(null));
        }
        return account;
    }
}
