package cz.cvut.fel.nss.parttimejobportal.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ADMIN")
public class Admin extends AbstractUser {
    public Admin() {

        super(Role.ADMIN);
    }

    public Admin(String password, String firstName, String lastName, String email) {

        super(password, firstName, lastName, email, Role.ADMIN);
    }
}
