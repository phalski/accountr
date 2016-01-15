package de.thi.phm6101.accountr.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Created by philipp on 21/12/15.
 */

@Entity
public class Transaction extends AbstractEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    @JsonBackReference
    private Account account;

    private double amount;

    @NotNull
    private Date date = Date.from(Instant.now());

    @NotNull
    private String description;

    private String receiptFileName;

    @Column(length = 100000) // forec blob
    @Lob
    private byte[] receiptImage;

    private UUID uuid = UUID.randomUUID();


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
        if (!this.account.getTransactions().contains(this)) {
            this.account.addTransaction(this);
        }
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceiptFileName() {
        return receiptFileName;
    }

    public boolean getHasReceipt() {
        return receiptFileName != null && !receiptFileName.isEmpty();
    }

    public void setReceiptFileName(String receiptFileName) {
        this.receiptFileName = receiptFileName;
    }

    public byte[] getReceiptImage() {
        return receiptImage;
    }

    public void setReceiptImage(byte[] receiptImage) {
        this.receiptImage = receiptImage;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        return getUuid().equals(that.getUuid());

    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}


