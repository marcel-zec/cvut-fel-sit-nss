package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.dao.*;
import cz.cvut.fel.nss.parttimejobportal.dto.UserDto;
import cz.cvut.fel.nss.parttimejobportal.model.*;
import cz.cvut.fel.nss.parttimejobportal.exception.BadPassword;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.exception.UnauthorizedException;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserDao dao;
    private final ManagerDao managerDao;
    private final TripReviewDao tripReviewDao;
    private final TravelJournalDao travelJournalDao;
    private final AddressDao addressDao;
    private final TranslateService translateService;
    private final TranslateBackService translateBackService;


    @Autowired
    public UserService(UserDao dao, ManagerDao managerDao, TripReviewDao tripReviewDao, TravelJournalDao travelJournalDao, AddressDao addressDao, TranslateService translateService, cz.cvut.fel.nss.parttimejobportal.service.TranslateBackService translateBackService) {
        this.dao = dao;
        this.managerDao = managerDao;
        this.tripReviewDao = tripReviewDao;
        this.travelJournalDao = travelJournalDao;
        this.addressDao = addressDao;
        this.translateService = translateService;
        this.translateBackService = translateBackService;
    }

    @Transactional
    public void createUser(User user, String passwordAgain) throws BadPassword {
        Objects.requireNonNull(user);
        if (!user.getPassword().equals(passwordAgain)) throw new BadPassword();
        user.encodePassword();

        if (user.getRole() != Role.USER) user.setRole(Role.USER);
        dao.persist(user);

        if (user.getAddress() != null){
            user.getAddress().setUser(user);
            addressDao.persist(user.getAddress());
        }
        if (user.getTravel_journal() == null) {
            TravelJournal tj = new TravelJournal(user);
            travelJournalDao.persist(tj);
            user.setTravel_journal(tj);
        }
        dao.update(user);
    }

    @Transactional(readOnly = true)
    public boolean exists(String login) {
        return dao.findByEmail(login) != null;
    }

    @Transactional
    public UserDto showCurrentUser() throws UnauthorizedException {
        if (SecurityUtils.isAuthenticatedAnonymously()) throw new UnauthorizedException();
        if(SecurityUtils.getCurrentUser().getRole().equals(Role.USER)) return translateService.translateUser(dao.find(SecurityUtils.getCurrentUser().getId()));
        return translateService.translateManager(managerDao.find(SecurityUtils.getCurrentUser().getId()));
    }

    @Transactional
    public void delete(Long id) throws NotFoundException {

        User user = dao.find(id);
        if (user == null) throw new NotFoundException();

        user.getAddress().softDelete();
        addressDao.update(user.getAddress());

        for (Enrollment e :user.getTravel_journal().getEnrollments()) {
            e.softDelete();
        }
        user.getTravel_journal().softDelete();
        travelJournalDao.update(user.getTravel_journal());

        for (TripReview tr: user.getTripReviews()) {
            tr.softDelete();
            tripReviewDao.update(tr);
        }

        user.softDelete();
        dao.update(user);
    }

    @Transactional
    public void update(UserDto userDto, AbstractUser current_user) throws NotFoundException {
        Objects.requireNonNull(userDto);
        User user = dao.find(current_user.getId());

        if (user == null) throw new NotFoundException();

        User newUser = (User) translateBackService.translateUser(userDto);

        newUser.setId(user.getId());
        newUser.setRole(user.getRole());
        newUser.setTripReviews(user.getTripReviews());

        if (newUser.getAddress() != null ) {
            Address oldAddress = user.getAddress();
            newUser.getAddress().setId(oldAddress.getId());
            oldAddress = newUser.getAddress();
            addressDao.update(oldAddress);
        }
        if (newUser.getTravel_journal() != null){
            TravelJournal oldTravelJournal = user.getTravel_journal();
            newUser.getTravel_journal().setId(oldTravelJournal.getId());
            oldTravelJournal = newUser.getTravel_journal();
            travelJournalDao.update(oldTravelJournal);
        }

        user=newUser;
        dao.update(user);
    }

    @Transactional
    public UserDto find(Long id) {
        Objects.requireNonNull(id);
        return translateService.translateUser(dao.find(id));
    }
    @Transactional
    public UserDto findByEmail(String email) {
        Objects.requireNonNull(email);
        return translateService.translateUser(dao.findByEmail(email));
    }

    @Transactional
    public List<UserDto> findAll() {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user:dao.findAll()) {
            userDtos.add(translateService.translateUser(user));
        }
        return userDtos;
    }

    @Transactional
    public boolean exists(Long id) {
        Objects.requireNonNull(id);
        return dao.exists(id);
    }

    @Transactional
    public List<User> findAllUsers() {
        return dao.findAll();
    }
}
