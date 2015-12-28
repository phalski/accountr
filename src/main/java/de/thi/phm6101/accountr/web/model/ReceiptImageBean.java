package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.Transaction;
import de.thi.phm6101.accountr.service.AccountrServiceBean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Lob;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

/**
 * Created by philipp on 28/12/15.
 */
@Named
@ApplicationScoped
public class ReceiptImageBean implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(AccountBean.class);
    @Inject
    private AccountrServiceBean accountrServiceBean;

    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            String id = context.getExternalContext().getRequestParameterMap().get("transactionID");
            Optional<Transaction> transactionOptional = accountrServiceBean.selectTransaction(Long.valueOf(id));
            return new DefaultStreamedContent(new ByteArrayInputStream(transactionOptional.get().getReceiptImage()));
        }
    }
}
