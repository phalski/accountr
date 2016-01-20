package de.thi.phm6101.accountr.service;

import de.thi.phm6101.accountr.domain.*;
import de.thi.phm6101.accountr.exception.EntityExistsException;
import de.thi.phm6101.accountr.exception.EntityNotFoundException;
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

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Created by philipp on 16/01/16.
 */
@RunWith(Arquillian.class)
public class UserServiceBeanTest {

    private int NUMBER_OF_USERS = 5;

    /**
     * class under test
     */
    @Inject
    UserServiceBean userServiceBean;

    @PersistenceContext
    EntityManager em;

    @Inject
    UserTransaction utx;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(AbstractEntity.class)
                .addClass(User.class)
                .addClass(UserRole.class)
                .addClass(DataAccessBean.class)
                .addClass(AccountrServiceBean.class)
                .addClass(Account.class)
                .addClass(Transaction.class)
                .addClass(UserServiceBean.class)
                .addClass(EntityFactory.class)
                .addClass(EntityExistsException.class)
                .addClass(EntityNotFoundException.class)
                .addAsResource("META-INF/persistence.test.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("WEB-INF/accountr-test-ds.xml")
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
        em.createQuery("DELETE FROM UserRole").executeUpdate();
        em.createQuery("DELETE FROM User" ).executeUpdate();
        commitTransaction();
    }

    private void prepareDatabase() throws Exception {
        startTransaction();
        IntStream.range(0, NUMBER_OF_USERS).forEach(i -> {
            User user = EntityFactory.newUser();
            em.persist(user);

        });
        commitTransaction();
    }

    /**
     * method under test: select
     *
     */

    @Test
    public void thatAUserCanBeSelectedByID() throws Exception {
        User user = EntityFactory.newUser();
        user = userServiceBean.insert(user);

        Optional<User> userOptional = userServiceBean.select(user.getId());

        assertTrue(userOptional.isPresent());
    }

    @Test
    public void thatANotExistingUserCannotBeSelected() throws Exception {
        User user = EntityFactory.newUser(); // user without id

        Optional<User> userOptional = userServiceBean.select(user.getId());

        assertFalse(userOptional.isPresent());
    }

    /**
     * method under test: select
     *
     */

    @Test
    public void thatAUserCanBeInserted() throws Exception {
        User user = EntityFactory.newUser();

        try {
            user = userServiceBean.insert(user);
        } catch (Exception e) {
            fail();
        }

        Optional<User> userOptional = userServiceBean.select(user.getId());
        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());
    }

    @Test
    public void thatAUserCannotBeInsertedTwice() throws Exception {
        User user = EntityFactory.newUser();

        try {
            user = userServiceBean.insert(user);
        } catch (Exception e) {
            fail();
        }

        try {
            user = userServiceBean.insert(user);
            fail();
        } catch (Exception e) {

        }
    }

    /**
     * method under test: select
     *
     */

    @Test
    public void thatEqualExistsReturnsTrueWhenEqualUserExists() throws Exception {
        User user = EntityFactory.newUser();
        user = userServiceBean.insert(user);

        boolean exists = userServiceBean.equalExists(user);

        assertTrue(exists);
    }

    @Test
    public void thatEqualExistsReturnsFAlseWhenEqualUserNotExists() throws Exception {
        User user = EntityFactory.newUser();

        boolean exists = userServiceBean.equalExists(user);

        assertFalse(exists);
    }
}
