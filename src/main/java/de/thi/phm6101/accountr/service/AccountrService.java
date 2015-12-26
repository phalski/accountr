package de.thi.phm6101.accountr.service;

import de.thi.phm6101.accountr.domain.Account;

import javax.persistence.EntityExistsException;

/**
 * Created by philipp on 26/12/15.
 */
public interface AccountrService {

    public Account createAccount(Account account) throws EntityExistsException;
}
