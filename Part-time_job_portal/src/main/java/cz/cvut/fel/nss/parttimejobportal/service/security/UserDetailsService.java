package cz.cvut.fel.nss.parttimejobportal.service.security;

import cz.cvut.fel.nss.parttimejobportal.dao.AbstractUserDao;
import cz.cvut.fel.nss.parttimejobportal.model.AbstractUser;
import cz.cvut.fel.nss.parttimejobportal.model.User;
import cz.cvut.fel.nss.parttimejobportal.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final AbstractUserDao userDao;

    @Autowired
    public UserDetailsService(AbstractUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //final User user = userDao.findByUsername(username);
        final AbstractUser user = userDao.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User with email " + email + " not found.");
        }
        return new cz.cvut.fel.nss.parttimejobportal.security.model.UserDetails(user);
    }

}
