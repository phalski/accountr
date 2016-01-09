package de.thi.phm6101.accountr.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by philipp on 09/01/16.
 */

@Entity
public class UserRole extends AbstractEntity {

    @ManyToOne
    private User user;

    private String role;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (!this.user.getRoles().contains(this)) {
            this.user.addRole(this);
        }
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRole userRole = (UserRole) o;

        if (getUser() != null ? !getUser().equals(userRole.getUser()) : userRole.getUser() != null) return false;
        return getRole() != null ? getRole().equals(userRole.getRole()) : userRole.getRole() == null;

    }

    @Override
    public int hashCode() {
        int result = getUser() != null ? getUser().hashCode() : 0;
        result = 31 * result + (getRole() != null ? getRole().hashCode() : 0);
        return result;
    }
}
