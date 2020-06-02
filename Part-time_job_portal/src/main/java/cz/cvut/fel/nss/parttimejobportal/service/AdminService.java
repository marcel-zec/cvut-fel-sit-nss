package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.dao.AbstractUserDao;
import cz.cvut.fel.nss.parttimejobportal.dao.ManagerDao;
import cz.cvut.fel.nss.parttimejobportal.dto.ManagerDto;
import cz.cvut.fel.nss.parttimejobportal.dto.UserDto;
import cz.cvut.fel.nss.parttimejobportal.model.Address;
import cz.cvut.fel.nss.parttimejobportal.model.Manager;
import cz.cvut.fel.nss.parttimejobportal.model.Role;
import cz.cvut.fel.nss.parttimejobportal.model.AbstractUser;
import cz.cvut.fel.nss.parttimejobportal.dao.AddressDao;
import cz.cvut.fel.nss.parttimejobportal.exception.BadPassword;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AdminService {

    private final ManagerDao managerDao;
    private final AddressDao addressDao;
    private final AbstractUserDao abstractUserDao;
    private final TranslateService translateService;

    public AdminService(ManagerDao managerDao, AddressDao addressDao, AbstractUserDao abstractUserDao, TranslateService translateService) {
        this.managerDao = managerDao;
        this.addressDao = addressDao;
        this.abstractUserDao = abstractUserDao;
        this.translateService = translateService;
    }


    @Transactional
    public void create(Manager manager, String passwordAgain) throws BadPassword {
        Objects.requireNonNull(manager);
        if (!manager.getPassword().equals(passwordAgain)) throw new BadPassword();
        manager.encodePassword();
        manager.setRole(Role.MANAGER);
        managerDao.persist(manager);

        if (manager.getAddress() != null){
            manager.getAddress().setUser(manager);
            addressDao.persist(manager.getAddress());
        }

        managerDao.update(manager);
    }

    @Transactional
    public ManagerDto find(Long id) {
        Objects.requireNonNull(id);
        Manager user = managerDao.find(id);
        if(user != null && user.getRole() == Role.MANAGER) return translateService.translateManager(user);
        else return null;
    }

    @Transactional
    public List<ManagerDto> findAll() {
        List<ManagerDto> managerDtos = new ArrayList<>();
        for (Manager manager : managerDao.findAll()) {
            managerDtos.add(translateService.translateManager(manager));
        }
        return managerDtos;
    }

    // predpokladam, ze admina muze upravovat samotny admin a superuser
    @Transactional
    public void update(Manager newUser, AbstractUser current_user) throws NotFoundException, UnauthorizedException {
        Objects.requireNonNull(newUser);
        current_user = managerDao.find(current_user.getId());
        AbstractUser user = managerDao.findByEmail(newUser.getEmail());

        if (user == null) throw NotFoundException.create("Admin", newUser.getEmail());

        //pokud je prihlaseny user s roli ADMIN, tak ze vsech adminů muze upravovat jenom sebe
        else if (current_user.getRole() == Role.MANAGER) {
            user = current_user;
        }

        newUser.setId(user.getId());
        if (newUser.getAddress() != null ) {
            Address oldAddress = user.getAddress();
            newUser.getAddress().setId(oldAddress.getId());
            oldAddress = newUser.getAddress();
            addressDao.update(oldAddress);
        }

        user = newUser;
        abstractUserDao.update(user);
    }
}
