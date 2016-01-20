package de.thi.phm6101.accountr.validation;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.domain.EntityFactory;
import de.thi.phm6101.accountr.service.AccountrServiceBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by philipp on 16/01/16.
 */
public class AccountNameValidatorTest {

    AccountNameValidator accountNameValidator;

    AccountrServiceBean mockedAccountrServiceBean;

    @Before
    public void setUp() throws Exception {
        mockedAccountrServiceBean = mock(AccountrServiceBean.class);
        accountNameValidator = new AccountNameValidator(mockedAccountrServiceBean);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void thatValidationSucceeds() throws Exception {
        Account account = EntityFactory.newAccount();
        when(mockedAccountrServiceBean.select(account.getName())).thenReturn(new ArrayList<Account>());

        try {
            accountNameValidator.validate(null, null, account.getName());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void thatValidationFails() throws Exception {
        Account account = EntityFactory.newAccount();
        List<Account> accountList = new ArrayList<>();
        accountList.add(account);
        when(mockedAccountrServiceBean.select(account.getName())).thenReturn(accountList);

        try {
            accountNameValidator.validate(null, null, account.getName());
            fail();
        } catch (Exception e) {
        }
    }
}
