package cz.cvut.fel.nss.parttimejobportal.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class Admin extends AbstractUser {
    public Admin() {

        super(Role.SUPERUSER);
    }


    public Admin(String password, String firstName, String lastName, String email) {

        super(password, firstName, lastName, email, Role.SUPERUSER);
    }


    public Admin(@Email(message = "Email should be valid") String email, @Size(max = 255, min = 6, message = "Password is in incorrect format.") String password) {

        super(email, password);
    }
}
