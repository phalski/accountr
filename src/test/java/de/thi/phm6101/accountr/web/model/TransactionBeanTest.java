package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.domain.EntityFactory;
import de.thi.phm6101.accountr.domain.Transaction;
import de.thi.phm6101.accountr.service.AccountrServiceBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class TransactionBeanTest {

    TransactionBean transactionBean;

    AccountrServiceBean mockedAccountrServiceBean;

    @Before
    public void setUp() throws Exception {
        mockedAccountrServiceBean = mock(AccountrServiceBean.class);
        transactionBean = new TransactionBean(mockedAccountrServiceBean);
    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * method under test: initialize
     */


    @Test
    public void thatInitializeWorksWhenAccountIsPresent() throws Exception {
        Account account = EntityFactory.newAccount();
        when(mockedAccountrServiceBean.select(account.getId())).thenReturn(Optional.of(account));

        String outcome = transactionBean.initialize();

        assertEquals(null, outcome);
    }

    @Test
    public void thatInitializeFailsWhenNoAccountIsPresent() throws Exception {
        Account account = EntityFactory.newAccount();
        when(mockedAccountrServiceBean.select(account.getId())).thenReturn(Optional.empty());

        String outcome = transactionBean.initialize();

        assertEquals("error", outcome);
    }

    /**
     * method under test: doSave
     */

    @Test
    public void thatTransactionisInsertedWhenAccountIsPresent() throws Exception {
        Account account = EntityFactory.newAccount();
        Transaction transaction = EntityFactory.newTransaction();
        transactionBean.setAccount(account);
        transactionBean.setTransaction(transaction);

        String outcome = transactionBean.doSave();

        verify(mockedAccountrServiceBean, times(1)).insertTransaction(account, transaction);
        assertEquals(String.format("account.xhtml?faces-redirect=true&accountId=%d", account.getId()), outcome);
    }

    public void thatTransactionisNotInsertedWhenNoAccountIsPresent() throws Exception {
        Transaction transaction = EntityFactory.newTransaction();
        transactionBean.setAccount(null);
        transactionBean.setTransaction(transaction);

        String outcome = transactionBean.doSave();

        verify(mockedAccountrServiceBean, times(0)).insertTransaction(null, transaction);
        assertEquals(String.format("account.xhtml?faces-redirect=true&accountId=%d", null), outcome);
    }

    /**
     * method under test: doDelete
     */

    public void thatExistingTransactionCanBeDeleted() throws Exception {
        Transaction transaction = EntityFactory.newTransaction();
        transactionBean.setAccount(null);
        transactionBean.setTransaction(transaction);

        String outcome = transactionBean.doDelete(transaction);

        verify(mockedAccountrServiceBean, times(1)).deleteTransaction(transaction);
        assertEquals(String.format("account.xhtml?faces-redirect=true&accountId=%d", null), outcome);
    }

}
