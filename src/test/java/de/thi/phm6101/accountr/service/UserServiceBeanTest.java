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

import static org.junit.Assert.*;

/**
 * Created by philipp on 16/01/16.
 */
@RunWith(Arquillian.class)
public class UserServiceBeanTest {

    /**
     * class under test
     */
    @Inject
    UserServiceBean userServiceBean;

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
                .addClass(EntityExistsException.class)
                .addClass(EntityNotFoundException.class)
                .addAsResource("META-INF/persistence.test.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("WEB-INF/accountr-test-ds.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void that() throws Exception {

    }
}
