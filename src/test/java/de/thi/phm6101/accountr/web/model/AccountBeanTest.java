package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.domain.EntityFactory;
import de.thi.phm6101.accountr.service.AccountrServiceBean;

import de.thi.phm6101.accountr.util.JsfUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class AccountBeanTest {

    AccountBean accountBean;

    AccountrServiceBean mockedAccountrServiceBean;
    JsfUtil mockedJsfUtil;

    @Before
    public void setUp() throws Exception {
        mockedAccountrServiceBean = mock(AccountrServiceBean.class);
        mockedJsfUtil = mock(JsfUtil.class);
        accountBean = new AccountBean(mockedAccountrServiceBean, mockedJsfUtil);


    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * method under test: initialize
     */


    @Test
    public void testThatInitialisationOnAccountFormFailsWhenNoAccountIsPresent() throws Exception {
        Account account = EntityFactory.newAccount();
        when(mockedJsfUtil.getCurrentViewId()).thenReturn(Optional.of("NOT-/account-form.xhtml"));
        when(mockedAccountrServiceBean.select(account.getId())).thenReturn(Optional.empty());
        accountBean.setAccountId(account.getId());

        String outcome = accountBean.initialize();

        assertEquals("error", outcome);
    }

    @Test
    public void testThatInitialisationWorksWithoutAccountOnAccountForm() throws Exception {
        Account account = EntityFactory.newAccount();
        when(mockedJsfUtil.getCurrentViewId()).thenReturn(Optional.of("/account-form.xhtml"));
        when(mockedAccountrServiceBean.select(account.getId())).thenReturn(Optional.empty());

        String outcome = accountBean.initialize();

        assertEquals(null, outcome);
    }

    /**
     * method under test: initializeList
     */

    @Test
    public void thatListContainsAllWhenSearchIsNull() throws Exception {
        List<Account> accountList = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> accountList.add(EntityFactory.newAccount()));
        when(mockedAccountrServiceBean.select()).thenReturn(accountList);
        accountBean.setSearch(null);

        accountBean.initializeList();

        assertEquals(accountBean.getAccountList().size(), 10);
    }

    @Test
    public void thatListContainsAllWhenSearchIsEmpty() throws Exception {
        List<Account> accountList = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> accountList.add(EntityFactory.newAccount()));
        when(mockedAccountrServiceBean.select()).thenReturn(accountList);
        accountBean.setSearch("");

        accountBean.initializeList();

        assertEquals(accountBean.getAccountList().size(), 10);
    }

    @Test
    public void thatListContainsElementsWhenSearchSuccessfull() throws Exception {
        Account account = EntityFactory.newAccount();
        List<Account> accountList = new ArrayList<>();
        accountList.add(account);
        when(mockedAccountrServiceBean.select(account.getName())).thenReturn(accountList);
        accountBean.setSearch(account.getName());

        accountBean.initializeList();

        assertNotEquals(accountBean.getAccountList().size(), 0);
    }

    @Test
    public void thatListContainsNoElementsWhenSearchFailed() throws Exception {
        Account account = EntityFactory.newAccount();
        List<Account> accountList = new ArrayList<>();
        when(mockedAccountrServiceBean.select(account.getName())).thenReturn(accountList);
        accountBean.setSearch(account.getName());

        accountBean.initializeList();

        assertEquals(accountBean.getAccountList().size(), 0);
    }

    /**
     * method under test: doInsertOrUpdate
     */

    @Test
    public void thatNotExistingAccountIsInserted() throws Exception {
        Account account = EntityFactory.newAccount();
        when(mockedAccountrServiceBean.exists(account)).thenReturn(false);
        when(mockedAccountrServiceBean.equalExists(account)).thenReturn(false);
        accountBean.setAccount(account);

        String outcome = accountBean.doInsertOrUpdate();

        verify(mockedAccountrServiceBean, times(1)).insert(account);
        assertEquals(outcome, "accounts.xhtml?faces-redirect=true");
    }

    @Test
    public void thatExistingAccountIsUpdated() throws Exception {
        Account account = EntityFactory.newAccount();
        when(mockedAccountrServiceBean.exists(account)).thenReturn(true);
        accountBean.setAccount(account);

        String outcome = accountBean.doInsertOrUpdate();

        verify(mockedAccountrServiceBean, times(1)).update(account);
        assertEquals(outcome, "accounts.xhtml?faces-redirect=true");
    }

    @Test
    public void thatNotExistingAccountCannotBeInsertedWhenEqualAccountIsPresent() throws Exception {
        Account account = EntityFactory.newAccount();
        when(mockedAccountrServiceBean.exists(account)).thenReturn(false);
        when(mockedAccountrServiceBean.equalExists(account)).thenReturn(true);
        accountBean.setAccount(account);

        String outcome = accountBean.doInsertOrUpdate();

        verify(mockedAccountrServiceBean, times(0)).insert(account);
        verify(mockedAccountrServiceBean, times(0)).update(account);
        assertEquals(outcome, "error");
    }

    /**
     * method under test: doDelete
     */

    @Test
    public void thatExistingAccountCanBeDeleted() throws Exception {
        Account account = EntityFactory.newAccount();
        when(mockedAccountrServiceBean.exists(account)).thenReturn(true);
        accountBean.setAccount(account);

        String outcome = accountBean.doDelete(account);

        verify(mockedAccountrServiceBean, times(1)).delete(account);
        assertEquals(outcome, "accounts.xhtml?faces-redirect=true");
    }

    @Test
    public void thatNotExistingAccountWillNotBeDeleted() throws Exception {
        Account account = EntityFactory.newAccount();
        when(mockedAccountrServiceBean.exists(account)).thenReturn(false);
        accountBean.setAccount(account);

        String outcome = accountBean.doInsertOrUpdate();

        verify(mockedAccountrServiceBean, times(0)).delete(account);
        assertEquals(outcome, "accounts.xhtml?faces-redirect=true");
    }
}
