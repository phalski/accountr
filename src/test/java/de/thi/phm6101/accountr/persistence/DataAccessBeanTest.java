package de.thi.phm6101.accountr.persistence;

import de.thi.phm6101.accountr.domain.AbstractEntity;
import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.domain.Transaction;
import de.thi.phm6101.accountr.service.AccountrServiceBean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassAsset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by philipp on 21/12/15.
 */
@RunWith(Arquillian.class)
public class DataAccessBeanTest {

    @Inject
    DataAccessBean dataAccessBean;

    List<Account> validAccountList;
    List<Transaction> validTransactionList;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
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
        validAccountList = new ArrayList<>();
        Account account = new Account();
        account.setName("First Account");
        account.setDescription("The fist account");
        validAccountList.add(account);

        account = new Account();
        account.setName("Second Account");
        account.setDescription("The second account");
        validAccountList.add(account);

        account = new Account();
        account.setName("Third Account");
        account.setDescription("The third account");
        validAccountList.add(account);

        validTransactionList = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setDate(new Date());
        transaction.setDescription("First transaction");
        validTransactionList.add(transaction);

        transaction = new Transaction();
        transaction.setDate(new Date());
        transaction.setDescription("Second transaction");
        validTransactionList.add(transaction);

        transaction = new Transaction();
        transaction.setDate(new Date());
        transaction.setDescription("Third transaction");
        validTransactionList.add(transaction);
    }

    @After
    public void tearDown() throws Exception {
        // null vars
        validAccountList = null;
        validTransactionList = null;

        // clear tables
        for (Account account: dataAccessBean.getAll(Account.class)) {
            dataAccessBean.delete(account);
        }

        for (Transaction transaction: dataAccessBean.getAll(Transaction.class)) {
            dataAccessBean.delete(transaction);
        }
    }

    @Test
    public void thatAccountCanBeFoundById() throws Exception {
        Account insertedAccount = validAccountList.get(0);
        dataAccessBean.insert(insertedAccount);


        Account receivedAccount = dataAccessBean.get(Account.class, insertedAccount.getId());

        assertNotNull("No account received", receivedAccount);
        assertEquals("Received account does not equal inserted account", insertedAccount, receivedAccount);
    }

    @Test
    public void thatNoAccountIsFoundForInvalidId() throws Exception {
        Account account = dataAccessBean.get(Account.class, -1);

        assertNull("Account found",account);
    }

    @Test
    public void thatAllAccountsAreFound() throws Exception {
        for (Account account: validAccountList) {
            dataAccessBean.insert(account);
        }

        List<Account> accountList = dataAccessBean.getAll(Account.class);

        assertEquals("Illegal list size", validAccountList.size(), accountList.size());
    }

    @Test
    public void testNamedQuery() throws Exception {

    }

    @Test
    public void thatAccountCanBeAdded() throws Exception {
        dataAccessBean.insert(validAccountList.get(0));

        List<Account> accountList = dataAccessBean.getAll(Account.class);
        assertEquals("Account list is empty", 1, accountList.size());
        assertNotNull("Account has no id", accountList.get(0).getId());
    }

    @Test(expected=Exception.class)
    public void thatAccountCantBeAddedTwice() throws Exception {
        Account account = validAccountList.get(0);
        Account duplicate = validAccountList.get(0);
        dataAccessBean.insert(account);
        dataAccessBean.insert(duplicate);
    }

    @Test
    public void thatAccountDescriptionCanBeUpdated() throws Exception {
        Account insertedAccount = validAccountList.get(0);
        dataAccessBean.insert(insertedAccount);

        Account updatedAccount = dataAccessBean.get(Account.class, insertedAccount.getId());
        String updatedDescription = "Updated description";
        updatedAccount.setDescription(updatedDescription);

        dataAccessBean.update(updatedAccount);

        Account receivedAccount = dataAccessBean.get(Account.class, insertedAccount.getId());
        assertEquals("Description does not match new value", updatedDescription, receivedAccount.getDescription());
        assertNotEquals("Description did not change", insertedAccount.getDescription(), receivedAccount.getDescription());
    }

    @Test
    public void thatAccountCanBeDeleted() throws Exception {
        Account account = validAccountList.get(0);
        dataAccessBean.insert(account);

        List<Account> accountList = dataAccessBean.getAll(Account.class);
        assertTrue("Account could not be added to list.", accountList.contains(account));

        dataAccessBean.delete(account);

        accountList = dataAccessBean.getAll(Account.class);
        assertTrue("Account is still in list.", !accountList.contains(account));

    }

    @Test
    public void thatTransactionCanBeAddedToAccount() throws Exception {
        Account account = validAccountList.get(0);
        Transaction transaction = validTransactionList.get(0);
        dataAccessBean.insert(account);

        account.addTransaction(transaction);
        dataAccessBean.update(account);

        Account receivedAccount = dataAccessBean.get(Account.class, account.getId());

        assertTrue("Transaction cannot be found", receivedAccount.getTransactions().contains(transaction));
        // just one transaction added so first should be it
        Transaction receivedTransaction = receivedAccount.getTransactions().get(0);
        assertEquals("Transaction does not match inserted transaction", transaction, receivedTransaction);
        assertEquals("Transaction is not owned by account", account, transaction.getAccount());
        assertNotNull("Transaction has no id", transaction.getId());

    }

    @Test
    public void thatTransactionCanBeDeleted() throws Exception {
        Account account = validAccountList.get(0);
        Transaction transaction = validTransactionList.get(0);
        dataAccessBean.insert(account);
        account.addTransaction(transaction);
        dataAccessBean.update(account);
        Account receivedAccount = dataAccessBean.get(Account.class, account.getId());
        Transaction receivedTransaction = receivedAccount.getTransactions().get(0);
    }
}
