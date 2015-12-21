package de.thi.phm6101.accountr.service;

import de.thi.phm6101.accountr.domain.AbstractEntity;
import de.thi.phm6101.accountr.domain.Account;
import de.thi.phm6101.accountr.domain.Transaction;
import de.thi.phm6101.accountr.exception.EntityAlreadyExistsException;
import de.thi.phm6101.accountr.persistence.DataAccessBean;
import org.jboss.arquillian.container.test.api.Deployment;
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
import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by philipp on 21/12/15.
 */
@RunWith(Arquillian.class)
public class AccountrServiceBeanTest {

    @Inject
    AccountrServiceBean accountrServiceBean;


    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(AccountrServiceBean.class)
                .addClass(DataAccessBean.class)
                .addClass(Account.class)
                .addClass(Transaction.class)
                .addClass(AbstractEntity.class)
                .addClass(EntityAlreadyExistsException.class)
                .addAsResource("META-INF/persistence.test.xml", "META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void setUp() throws Exception {


    }

    @After
    public void tearDown() throws Exception {
        // clear tables
        for (Account account: accountrServiceBean.select()) {
            accountrServiceBean.delete(account);
        }

    }

    @Test
    public void thatAccountCanBeAdded() throws Exception {
        Account account = new Account();
        account.setName("name");
        account.setDescription("description");

        accountrServiceBean.insert(account);

        List<Account> accountList = accountrServiceBean.accountList();
        assertNotEquals(0, accountList.size());
        assertNotNull(accountList.get(0).getId());
    }
}
