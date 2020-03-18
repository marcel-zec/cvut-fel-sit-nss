package cz.cvut.fel.ear.rsp.travelandwork.service.security;

import cz.cvut.fel.ear.rsp.travelandwork.exception.AlreadyLoginException;
import cz.cvut.fel.ear.rsp.travelandwork.security.DefaultAuthenticationProvider;
import cz.cvut.fel.ear.rsp.travelandwork.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    private DefaultAuthenticationProvider provider;

    @Autowired
    public LoginService(DefaultAuthenticationProvider provider) {
        this.provider = provider;
    }


    @Transactional(readOnly = true)
    public void loginStudent(String username, String password) throws AlreadyLoginException {

        if (SecurityUtils.getCurrentUserDetails() != null) throw new AlreadyLoginException();
        Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
        provider.authenticate(auth, false);

    }

    @Transactional(readOnly = true)
    public void loginManager(String username, String password) throws AlreadyLoginException {

        if (SecurityUtils.getCurrentUserDetails() != null) throw new AlreadyLoginException();
        Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
        provider.authenticate(auth, true);
    }


}