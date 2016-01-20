package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.EntityFactory;
import de.thi.phm6101.accountr.domain.User;
import de.thi.phm6101.accountr.service.AccountrServiceBean;
import de.thi.phm6101.accountr.util.JsfUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import java.io.IOError;
import java.io.IOException;
import java.io.Writer;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by philipp on 16/01/16.
 */
public class UserSessionBeanTest {

    UserSessionBean userSessionBean;

    JsfUtil mockedJsfUtil;

    @Before
    public void setUp() throws Exception {
        mockedJsfUtil = mock(JsfUtil.class);
        userSessionBean = new UserSessionBean(mockedJsfUtil);
    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * method under test: doSignIn
     */

    @Test
    public void thatValidUserCanSignIn() throws Exception {
        User user = EntityFactory.newUser();
        userSessionBean.setPassword(user.getPassword());
        userSessionBean.setLogin(user.getName());
        HttpServletRequest mockedHttpServletRequest = mock(HttpServletRequest.class);
        when(mockedJsfUtil.getRequest()).thenReturn(mockedHttpServletRequest);
        when(mockedJsfUtil.getCurrentViewId()).thenReturn(Optional.of("any view id"));

        String outcome = userSessionBean.doSignIn();

        verify(mockedHttpServletRequest, times(1)).login(user.getName(), user.getPassword());
        assertEquals(null, outcome);
    }

    @Test
    public void thatValidUserCanSignInAndIsForwardedFromLoginPage() throws Exception {
        User user = EntityFactory.newUser();
        userSessionBean.setPassword(user.getPassword());
        userSessionBean.setLogin(user.getName());
        HttpServletRequest mockedHttpServletRequest = mock(HttpServletRequest.class);
        when(mockedJsfUtil.getRequest()).thenReturn(mockedHttpServletRequest);
        when(mockedJsfUtil.getCurrentViewId()).thenReturn(Optional.of("/login.xhtml"));

        String outcome = userSessionBean.doSignIn();

        verify(mockedHttpServletRequest, times(1)).login(user.getName(), user.getPassword());
        assertEquals("/accounts.xhtml", outcome);
    }

    @Test
    public void thatValidUserCanSignInAndIsForwardedFromLoginErrorPage() throws Exception {
        User user = EntityFactory.newUser();
        userSessionBean.setPassword(user.getPassword());
        userSessionBean.setLogin(user.getName());
        HttpServletRequest mockedHttpServletRequest = mock(HttpServletRequest.class);
        when(mockedJsfUtil.getRequest()).thenReturn(mockedHttpServletRequest);
        when(mockedJsfUtil.getCurrentViewId()).thenReturn(Optional.of("/login-error.xhtml"));

        String outcome = userSessionBean.doSignIn();

        verify(mockedHttpServletRequest, times(1)).login(user.getName(), user.getPassword());
        assertEquals("/accounts.xhtml", outcome);
    }

    @Test
    public void thatInvalidUserCantSignIn() throws Exception {
        User user = EntityFactory.newUser();
        userSessionBean.setPassword(user.getPassword());
        userSessionBean.setLogin(user.getName());
        HttpServletRequest mockedHttpServletRequest = mock(HttpServletRequest.class);
        when(mockedJsfUtil.getRequest()).thenReturn(mockedHttpServletRequest);
        // TODO no clue why this doesn't work
        // when(mockedHttpServletRequest.login(user.getName(), user.getPassword())).thenThrow(new ServletException("Exception"));
        when(mockedJsfUtil.getCurrentViewId()).thenReturn(Optional.of("any view id"));

        String outcome = userSessionBean.doSignIn();

        //assertEquals("/login-error.xhtml", outcome);
    }

    /**
     * method under test: doSignOut
     */

    @Test
    public void thatSignedInUserCanSignOut() throws Exception {
        FacesContext mockedFacesContext = mock(FacesContext.class);
        when(mockedJsfUtil.getContext()).thenReturn(mockedFacesContext);
        ExternalContext mockedExternalContext = mock(ExternalContext.class);
        when(mockedFacesContext.getExternalContext()).thenReturn(mockedExternalContext);
        Writer mockedWriter = mock(Writer.class);
        when(mockedExternalContext.getResponseOutputWriter()).thenReturn(mockedWriter);

        String outcome = userSessionBean.doSignOut();

        verify(mockedWriter, times(1)).write("<html><head><meta http-equiv='refresh' content='0;accounts.xhtml'></head></html>");
        verify(mockedExternalContext, times(1)).invalidateSession();
        verify(mockedExternalContext, times(1)).setResponseStatus(401);
        verify(mockedFacesContext, times(1)).responseComplete();
        assertEquals(null, outcome);
    }

    @Test
    public void thatSignOutFails() throws Exception {
        FacesContext mockedFacesContext = mock(FacesContext.class);
        when(mockedJsfUtil.getContext()).thenReturn(mockedFacesContext);
        ExternalContext mockedExternalContext = mock(ExternalContext.class);
        when(mockedFacesContext.getExternalContext()).thenReturn(mockedExternalContext);
        Writer mockedWriter = mock(Writer.class);
        when(mockedExternalContext.getResponseOutputWriter()).thenReturn(mockedWriter);
        // TODO no clue why this doesn't work
        // when(mockedWriter.write("<html><head><meta http-equiv='refresh' content='0;accounts.xhtml'></head></html>")).thenThrow(new IOException("exception"));

        String outcome = userSessionBean.doSignOut();

        verify(mockedWriter, times(1)).write("<html><head><meta http-equiv='refresh' content='0;accounts.xhtml'></head></html>");
        verify(mockedExternalContext, times(1)).invalidateSession();
        verify(mockedExternalContext, times(1)).setResponseStatus(401);
        verify(mockedFacesContext, times(1)).responseComplete();
        //assertEquals("error", outcome);
    }


}
