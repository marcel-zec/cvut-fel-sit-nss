package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.dto.AbstractUserDto;
import cz.cvut.fel.nss.parttimejobportal.exception.AlreadyLoginException;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityConstants;
import cz.cvut.fel.nss.parttimejobportal.service.security.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@CrossOrigin(origins = SecurityConstants.ORIGIN_URI, allowCredentials="true", allowedHeaders = "*")
public class LoginController {

    private LoginService service;

    @Autowired
    public LoginController(LoginService service) {
         this.service = service;
    }


    @PostMapping(value = "/login",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AbstractUserDto> loginUser(@RequestBody HashMap<String,String> request) throws AlreadyLoginException {

        return ResponseEntity.status(HttpStatus.OK).body(service.login(request.get("email"),request.get("password")));
    }
}
