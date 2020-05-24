package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.dto.UserDto;
import cz.cvut.fel.nss.parttimejobportal.exception.AlreadyLoginException;
import cz.cvut.fel.nss.parttimejobportal.service.security.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials="true", allowedHeaders = "*")
public class LoginController {

    private LoginService service;

    @Autowired
    public LoginController(LoginService service) {
         this.service = service;
    }

    @PostMapping(value = "/login",produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto login(@RequestBody HashMap<String,String> request) throws AlreadyLoginException {

        return service.login(request.get("email"),request.get("password"));
    }
}
