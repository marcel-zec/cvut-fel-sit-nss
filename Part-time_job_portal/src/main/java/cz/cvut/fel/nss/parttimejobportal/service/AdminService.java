package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.dto.UserDto;
import cz.cvut.fel.nss.parttimejobportal.model.Address;
import cz.cvut.fel.nss.parttimejobportal.model.Role;
import cz.cvut.fel.nss.parttimejobportal.model.User;
import cz.cvut.fel.nss.parttimejobportal.dao.AddressDao;
import cz.cvut.fel.nss.parttimejobportal.dao.UserDao;
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

    private final UserDao userDao;
    private final AddressDao addressDao;
    private final TranslateService translateService;

    public AdminService(UserDao userDao, AddressDao addressDao, TranslateService translateService) {
        this.userDao = userDao;
        this.addressDao = addressDao;
        this.translateService = translateService;
    }


    @Transactional
    public void create(User user, String passwordAgain) throws BadPassword {
        Objects.requireNonNull(user);
        if (!user.getPassword().equals(passwordAgain)) throw new BadPassword();
        user.encodePassword();
        user.setRole(Role.ADMIN);
        userDao.persist(user);

        if (user.getAddress() != null){
            user.getAddress().setUser(user);
            addressDao.persist(user.getAddress());
        }

        userDao.update(user);
    }

    @Transactional
    public UserDto find(Long id) {
        Objects.requireNonNull(id);
        User user = userDao.find(id);
        if(user != null && user.getRole() == Role.ADMIN) return translateService.translateUser(user);
        else return null;
    }

    @Transactional
    public List<UserDto> findAll() {
        List<UserDto> adminDtos = new ArrayList<>();
        for (User user:userDao.findAll()) {
            if(user.getRole() == Role.ADMIN) adminDtos.add(translateService.translateUser(user));
        }
        return adminDtos;
    }

    // predpokladam, ze admina muze upravovat samotny admin a superuser
    @Transactional
    public void update(User newUser, User current_user) throws NotFoundException, UnauthorizedException {
        Objects.requireNonNull(newUser);
        current_user = userDao.find(current_user.getId());
        User user = userDao.findByEmail(newUser.getEmail());

        if (user == null) throw NotFoundException.create("Admin", newUser.getEmail());

        //pokud je prihlaseny user s roli ADMIN, tak ze vsech adminů muze upravovat jenom sebe
        else if (current_user.getRole() == Role.ADMIN) {
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
        userDao.update(user);
    }
}
