package de.thi.phm6101.accountr.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User class for wildfly security
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "user.findByName",
                query = "SELECT t FROM User as t WHERE t.name LIKE :name")
})
public class User extends AbstractEntity {

    @Column(unique = true)
    private String name;

    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> roles;

    public User() {
        roles = new ArrayList<>();
        UserRole userRole = new UserRole();
        userRole.setRole("User");
        this.addRole(userRole);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserRole> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    public void addRole(UserRole role) {
        this.roles.add(role);
        if (role.getUser() != this) {
            role.setUser(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return getName() != null ? getName().equals(user.getName()) : user.getName() == null;

    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }
}
