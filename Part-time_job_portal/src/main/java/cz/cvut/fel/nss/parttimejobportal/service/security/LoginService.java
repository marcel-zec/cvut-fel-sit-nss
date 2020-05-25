package cz.cvut.fel.nss.parttimejobportal.service.security;

import cz.cvut.fel.nss.parttimejobportal.dao.ManagerDao;
import cz.cvut.fel.nss.parttimejobportal.dto.UserDto;
import cz.cvut.fel.nss.parttimejobportal.model.Manager;
import cz.cvut.fel.nss.parttimejobportal.model.Role;
import cz.cvut.fel.nss.parttimejobportal.service.TranslateService;
import cz.cvut.fel.nss.parttimejobportal.dao.UserDao;
import cz.cvut.fel.nss.parttimejobportal.exception.AlreadyLoginException;
import cz.cvut.fel.nss.parttimejobportal.security.DefaultAuthenticationProvider;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    private DefaultAuthenticationProvider provider;
    private TranslateService translateService;
    private UserDao userDao;
    private ManagerDao managerDao;

    @Autowired
    public LoginService(DefaultAuthenticationProvider provider, TranslateService translateService, UserDao userDao, ManagerDao managerDao) {
        this.provider = provider;
        this.translateService = translateService;
        this.userDao = userDao;
        this.managerDao = managerDao;
    }


    @Transactional(readOnly = true)
    public UserDto login(/*String username*/ String email, String password) throws AlreadyLoginException {

        if (SecurityUtils.getCurrentUserDetails() != null) throw new AlreadyLoginException();
        Authentication auth = new UsernamePasswordAuthenticationToken(/*username*/ email, password);
        provider.authenticate(auth);
        if (SecurityUtils.getCurrentUser().getRole() == Role.USER ) return translateService.translateUser(userDao.find(SecurityUtils.getCurrentUser().getId()));
        else return translateService.translateManager(managerDao.find(SecurityUtils.getCurrentUser().getId()));

    }


}
