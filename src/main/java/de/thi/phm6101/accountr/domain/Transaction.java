package de.thi.phm6101.accountr.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by philipp on 21/12/15.
 */

@Entity
public class Transaction extends AbstractEntity {

    @ManyToOne
    @NotNull
    private Account account;

    @NotNull
    private Date date;

    @NotNull
    private String description;


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (!getAccount().equals(that.getAccount())) return false;
        if (!getDate().equals(that.getDate())) return false;
        return getDescription().equals(that.getDescription());

    }

    @Override
    public int hashCode() {
        int result = getAccount().hashCode();
        result = 31 * result + getDate().hashCode();
        result = 31 * result + getDescription().hashCode();
        return result;
    }
}


