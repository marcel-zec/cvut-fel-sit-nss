package cz.cvut.fel.nss.parttimejobportal.dto;

import cz.cvut.fel.nss.parttimejobportal.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RequestWrapper {

    private User user;

    @Size(max = 255, min = 6, message = "Password control is in incorrect format.")
    @NotBlank(message = "Password control cannot be blank")
    private String password_control;


    public RequestWrapper(User user, String password_control) {
        this.user = user;
        this.password_control = password_control;
    }


    public RequestWrapper() {

    }


    public User getUser() {

        return user;
    }


    public String getPassword_control() {

        return password_control;
    }

    public void encodePassword() {
        this.password_control = new BCryptPasswordEncoder().encode(password_control);
    }

}
