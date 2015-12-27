package de.thi.phm6101.accountr.service;

import de.thi.phm6101.accountr.domain.AbstractEntity;
import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.domain.EntityFactory;
import de.thi.phm6101.accountr.domain.Transaction;
import de.thi.phm6101.accountr.persistence.DataAccessBean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.graphene.shaded.net.sf.cglib.proxy.Factory;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Created by philipp on 21/12/15.
 */
@RunWith(Arquillian.class)
public class AccountrServiceBeanTest {

    private static final int NUMBER_OF_ACCOUNTS = 5;
    private static final int NUMBER_OF_TRANSACTIONS = 5;

    /**
     * class under test
     */
    @Inject
    AccountrServiceBean accountrServiceBean;

    @PersistenceContext
    EntityManager em;

    @Inject
    UserTransaction utx;



    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(EntityFactory.class)
                .addClass(AccountrService.class)
                .addClass(AccountrServiceBean.class)
                .addClass(DataAccessBean.class)
                .addClass(Account.class)
                .addClass(Transaction.class)
                .addClass(AbstractEntity.class)
                .addClass(EntityExistsException.class)
                .addAsResource("META-INF/persistence.test.xml", "META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void setUp() throws Exception {
        clearDatabase();
        prepareDatabase();
        startTransaction();
    }

    @After
    public void tearDown() throws Exception {
        commitTransaction();
    }

    /// UTIL

    private void startTransaction() throws Exception {
        utx.begin();
        em.joinTransaction();
    }

    private void commitTransaction() throws Exception {
        utx.commit();
    }

    private void clearDatabase() throws Exception {
        startTransaction();
        em.createQuery("DELETE FROM Transaction ").executeUpdate();
        em.createQuery("DELETE FROM Account" ).executeUpdate();
        commitTransaction();
    }

    private void prepareDatabase() throws Exception {
        startTransaction();
        IntStream.range(0, NUMBER_OF_ACCOUNTS).forEach(i -> {
            Account account = EntityFactory.newAccount();
            em.persist(account);
            IntStream.range(0, NUMBER_OF_TRANSACTIONS).forEach(j -> {
                account.addTransaction(EntityFactory.newTransaction());
            });
            em.persist(account);
        });
        commitTransaction();
    }


    /// TESTS

    @Test
    public void thatAllAccountsCanBeSelected() throws Exception {
        List<Account> accountList = accountrServiceBean.selectAccount();

        assertEquals("Account list does not contain all elements", accountList.size(), NUMBER_OF_ACCOUNTS);
    }

    @Test
    public void thatAccountsCanBeSelectedByName() throws Exception {
        Account account = EntityFactory.newAccount();
        accountrServiceBean.insert(account);

        List<Account> accountList = accountrServiceBean.selectAccount(account.getName());

        assertEquals("Invalid element count", accountList.size(), 1);
        assertEquals("Accounts are not equal", accountList.get(0), account);
    }

    @Test
    public void thatAccountCanBeSelectedById() throws Exception {
        Account account = EntityFactory.newAccount();
        accountrServiceBean.insert(account);

        Optional<Account> accountOptional = accountrServiceBean.selectAccount(account.getId());

        assertTrue("No account found", accountOptional.isPresent());
        assertEquals("Accounts are not equal", accountOptional.get(), account);
    }

    @Test
    public void thatAccountCanBeInserted() throws Exception {
        Account account = EntityFactory.newAccount();

        accountrServiceBean.insert(account);

        assertNotNull("No Account found", em.find(Account.class, account.getId()));
    }

    @Test
    public void thatAccountCanBeUpdated() throws Exception {
        List<Account> accountList = accountrServiceBean.selectAccount();
        Account account = accountList.get(0);
        String oldName = account.getName();
        String oldDescription = account.getDescription();

        account.setName("Updated Name");
        account.setDescription("Updated Description");
        accountrServiceBean.update(account);
        Optional<Account> accountOptional = accountrServiceBean.selectAccount(account.getId());

        assertTrue("No account found", accountOptional.isPresent());
        assertEquals("Accounts are not equal", accountOptional.get(), account);
        assertNotEquals("Accounts data did not change", accountOptional.get().getName(), oldName);
        assertNotEquals("Accounts data did not change", accountOptional.get().getDescription(), oldDescription);
    }

    @Test
    public void thatAccountCanBeDeleted() throws Exception {
        List<Account> accountList = accountrServiceBean.selectAccount();
        Account account = accountList.get(0);

        accountrServiceBean.delete(account);
        Optional<Account> accountOptional = accountrServiceBean.selectAccount(account.getId());

        assertFalse("Account not deleted", accountOptional.isPresent());
    }

    @Test
    public void thatTransactionCanBeAdded() throws Exception {
        List<Account> accountList = accountrServiceBean.selectAccount();
        Account account = accountList.get(0);
        Transaction transaction = EntityFactory.newTransaction();

        account.addTransaction(transaction);
        accountrServiceBean.update(account);
        Optional<Account> accountOptional = accountrServiceBean.selectAccount(account.getId());

        assertTrue("No account found", accountOptional.isPresent());
        assertTrue("Transaction not inserted", accountOptional.get().getTransactions().contains(transaction));
    }

    @Test
    public void thatTransactionCanBeRemoved() throws Exception {
        List<Account> accountList = accountrServiceBean.selectAccount();
        Account account = accountList.get(0);
        Transaction transaction = account.getTransactions().get(0);

        account.removeTransaction(transaction);
        accountrServiceBean.update(account);
        Optional<Account> accountOptional = accountrServiceBean.selectAccount(account.getId());

        assertTrue("No account found", accountOptional.isPresent());
        assertFalse("Transaction not deleted", accountOptional.get().getTransactions().contains(transaction));
    }

    @Test
    public void thatTransactionCanBeEdited() throws Exception {
        List<Account> accountList = accountrServiceBean.selectAccount();
        Account account = accountList.get(0);
        Transaction transaction = account.getTransactions().get(0);
        long id = transaction.getId();
        double amount = transaction.getAmount();
        Date date = transaction.getDate();
        String description = transaction.getDescription();

        transaction.setAmount(-1);
        transaction.setDate(Date.from(Instant.now()));
        transaction.setDescription("Edited description");
        accountrServiceBean.update(account);
        Optional<Account> accountOptional = accountrServiceBean.selectAccount(account.getId());

        assertTrue("No account found", accountOptional.isPresent());
        Optional<Transaction> transactionOptional = accountOptional.get().getTransaction(id);
        assertTrue("No transaction found", transactionOptional.isPresent());
        assertEquals("Amount does not have new value", transactionOptional.get().getAmount(), transaction.getAmount(), 0.0001d);
        assertEquals("Date does not have new value", transactionOptional.get().getDate(), transaction.getDate());
        assertEquals("Description does not have new value", transactionOptional.get().getDescription(), transaction.getDescription());
        assertNotEquals("Amount still has old value", transactionOptional.get().getAmount(), amount, 0.0001d);
        assertNotEquals("Date still has old value", transactionOptional.get().getDate(), date);
        assertNotEquals("Description still has old value", transactionOptional.get().getDescription(), description);
    }

    @Test
    public void thatImageCanBeUploadedForTransaction() throws Exception {

    }
}
