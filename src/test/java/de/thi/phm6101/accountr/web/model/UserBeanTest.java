package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.EntityFactory;
import de.thi.phm6101.accountr.domain.User;
import de.thi.phm6101.accountr.exception.EntityExistsException;
import de.thi.phm6101.accountr.service.UserServiceBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * Created by philipp on 16/01/16.
 */
public class UserBeanTest {

    UserBean userBean;

    UserServiceBean mockedUserServiceBean;

    @Before
    public void setUp() throws Exception {
        mockedUserServiceBean = mock(UserServiceBean.class);
        userBean = new UserBean(mockedUserServiceBean);
    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * method under test: initialize
     */

    @Test
    public void thatInitializeWorks() throws Exception {
        userBean.initialize();

        assertNotNull(userBean.getUser());
    }

    /**
     * method under test: doSignUp
     */

    @Test
    public void thatNotExistingUserCanSignUp() throws Exception {
        User user = EntityFactory.newUser();
        userBean.setUser(user);

        String outcome = userBean.doSignUp();

        verify(mockedUserServiceBean, times(1)).insert(user);
        assertEquals("/accounts.xhtml?faces-redirect=true", outcome);
    }

    @Test
    public void thatExistingUserCanNotSignUp() throws Exception {
        User user = EntityFactory.newUser();
        userBean.setUser(user);
        when(mockedUserServiceBean.insert(user)).thenThrow(new EntityExistsException("Exists"));


        String outcome = userBean.doSignUp();

        verify(mockedUserServiceBean, times(1)).insert(user);
        assertEquals("error", outcome);
    }

}
