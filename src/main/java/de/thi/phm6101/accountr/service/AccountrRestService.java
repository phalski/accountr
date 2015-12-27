package de.thi.phm6101.accountr.service;

import de.thi.phm6101.accountr.domain.Account;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created by philipp on 27/12/15.
 */
@Path("/accounts")
public class AccountrRestService {

    @EJB
    AccountrServiceBean accountrServiceBean;

    @GET
    @Produces("application/json")
    public List<Account> findAll() {
        return accountrServiceBean.select();
    }

    @GET
    @Path("/{accountId}")
    @Produces("application/json")
    public Account findById(@PathParam("accountId") Long id) {
        return accountrServiceBean.select(id).get();
    }
}
