package de.thi.phm6101.accountr.web.model;

import de.thi.phm6101.accountr.domain.Transaction;
import de.thi.phm6101.accountr.service.AccountrServiceBean;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

/**
 * Named bean for all image related logic
 */
@Named
@ApplicationScoped
public class ReceiptImageBean implements Serializable {

    @Inject
    private AccountrServiceBean accountrServiceBean;

    /**
     * Returns an image from database as streamed content. This is necessary for rendering image with primefaces.
     * @return image content
     * @throws IOException
     */
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
