package cz.cvut.fel.nss.parttimejobportal.service.security;

import cz.cvut.fel.nss.parttimejobportal.model.AbstractUser;
import cz.cvut.fel.nss.parttimejobportal.model.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    public boolean isUser(AbstractUser user){
        return user.getRole() == Role.USER;
    }

    public boolean isManager(AbstractUser user){
        return user.getRole() == Role.ADMIN || user.getRole() == Role.SUPERUSER;
    }
}