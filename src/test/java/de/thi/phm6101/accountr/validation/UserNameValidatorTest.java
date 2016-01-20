package de.thi.phm6101.accountr.validation;

import de.thi.phm6101.accountr.domain.EntityFactory;
import de.thi.phm6101.accountr.domain.User;
import de.thi.phm6101.accountr.service.UserServiceBean;
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
public class UserNameValidatorTest {

    UserNameValidator userNameValidator;

    UserServiceBean mockedUserServiceBean;

    @Before
    public void setUp() throws Exception {
        mockedUserServiceBean = mock(UserServiceBean.class);
        userNameValidator = new UserNameValidator(mockedUserServiceBean);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void thatValidationSucceeds() throws Exception {
        User user = EntityFactory.newUser();
        when(mockedUserServiceBean.select(user.getName())).thenReturn(new ArrayList<User>());

        try {
            userNameValidator.validate(null, null, user.getName());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void thatValidationFails() throws Exception {
        User user = EntityFactory.newUser();
        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(mockedUserServiceBean.select(user.getName())).thenReturn(userList);

        try {
            userNameValidator.validate(null, null, user.getName());
            fail();
        } catch (Exception e) {
        }
    }
}
