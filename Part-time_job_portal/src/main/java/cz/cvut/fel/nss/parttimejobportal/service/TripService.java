package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.dao.*;
import cz.cvut.fel.nss.parttimejobportal.dto.TripDto;
import cz.cvut.fel.nss.parttimejobportal.dto.TripSessionDto;
import cz.cvut.fel.nss.parttimejobportal.model.*;
import cz.cvut.fel.rsp.travelandwork.dao.*;
import cz.cvut.fel.nss.parttimejobportal.exception.BadDateException;
import cz.cvut.fel.nss.parttimejobportal.exception.MissingVariableException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotAllowedException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.rsp.travelandwork.model.*;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityUtils;
import cz.cvut.fel.nss.parttimejobportal.security.model.UserDetails;
import cz.cvut.fel.nss.parttimejobportal.service.security.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TripService {

    private final TripDao tripDao;
    private final TripSessionDao tripSessionDao;
    private final TripReviewDao tripReviewDao;
    private final TranslateService translateService;
    private final AccessService accessService;
    private final UserDao userDao;
    private final EnrollmentDao enrollmentDao;
    private final TravelJournalDao travelJournalDao;

    @Autowired
    public TripService(TripDao tripDao, TripSessionDao tripSessionDao, TripReviewDao tripReviewDao, TranslateService translateService, AccessService accessService, UserDao userDao, EnrollmentDao enrollmentDao, TravelJournalDao travelJournalDao) {
        this.tripDao = tripDao;
        this.tripSessionDao = tripSessionDao;
        this.tripReviewDao = tripReviewDao;
        this.translateService = translateService;
        this.accessService = accessService;
        this.userDao = userDao;
        this.enrollmentDao = enrollmentDao;
        this.travelJournalDao = travelJournalDao;
    }

    @Transactional
    public List<Trip> findAll() {
        return tripDao.findAll();
    }

    @Transactional
    public List<TripDto> findAllDto() {
        List<TripDto> tripDtos = new ArrayList<>();

        for (Trip trip:tripDao.findAll()) {
            tripDtos.add(translateService.translateTrip(trip));
        }
        return tripDtos;
    }

    @Transactional
    public List<TripDto> findAllDtoFiltered() {
        List<TripDto> tripDtos = new ArrayList<>();

        for (Trip trip:tripDao.findAll()) {
            if(isTripActive(trip)) {
                tripDtos.add(translateService.translateTrip(trip));
            }
        }
        return tripDtos;
    }

    @Transactional
    public TripDto find(Long id) {
        Trip trip = tripDao.find(id);
        UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
        //do not werk and we propably do not use this so whatever
        if(userDetails != null){
            System.out.println("THIS?");
                if (!userDetails.getUser().getRole().equals(Role.USER)) {
                    System.out.println("AM I doing this?");
                    return translateService.translateTrip(trip);
                }
        }

        List<TripSession> sessions = new ArrayList<>();
        for(TripSession tripSession : trip.getSessions()) {
            if(tripSession.isNotDeleted() && tripSession.getTo_date().isAfter(LocalDate.now())) {
                sessions.add(tripSession);
            }
        }
        trip.setSessions(sessions);

        return translateService.translateTrip(trip);
    }

    @Transactional
    public TripDto findByString(String stringId) {
        Trip trip = tripDao.find(stringId);

        return translateService.translateTrip(trip);
    }

    @Transactional
    public TripDto findByStringFiltered(String stringId) {
        Trip trip = tripDao.find(stringId);
        TripDto tripDto = translateService.translateTrip(trip);

        List<TripSessionDto> sessions = new ArrayList<>();
        for(TripSession tripSession : trip.getSessions()) {
            if(tripSession.isNotDeleted() &&
                    tripSession.getTo_date().isAfter(LocalDate.now()) &&
                    tripSession.getFrom_date().isAfter(LocalDate.now())) {
                sessions.add(translateService.translateSession(tripSession));
            }
        }
        tripDto.setSessions(sessions);

        return tripDto;
    }

    @Transactional
    public void create(Trip trip) throws BadDateException, MissingVariableException {

        Objects.requireNonNull(trip);
        if (trip.getSessions().size()<=0) throw new MissingVariableException();
        tripDao.persist(trip);
        for (TripSession session: trip.getSessions()) {
            if (session.getTo_date().isBefore(session.getFrom_date())) {
                tripDao.remove(trip);
                throw new BadDateException();
            }
            session.setTrip(trip);
            tripSessionDao.persist(session);
        }
        tripDao.update(trip);
    }

    @Transactional
    public void signUpToTrip(TripSessionDto tripSessionDto, User current_user) throws NotAllowedException {
        TripSession tripSession = tripSessionDao.find(tripSessionDto.getId());
//      TODO odkomentovat ked bude otestovane ukoncovanie tripov
//       if (tripSession.getFrom_date().isBefore(ChronoLocalDate.from(LocalDateTime.now()))) throw new NotAllowedException();
        User user = userDao.find(current_user.getId());

        if(checkOwnedAchievements(user.getTravel_journal(), tripSession.getTrip())) {
            Enrollment enrollment = new Enrollment();

            enrollment.setDeposit_was_paid(false);
            enrollment.setEnrollDate(LocalDateTime.now());
            enrollment.setActual_xp_reward(0);
            enrollment.setTrip(tripSession.getTrip());
            enrollment.setState(EnrollmentState.ACTIVE);
            enrollment.setTripSession(tripSession);
            enrollment.setTravelJournal(user.getTravel_journal());

            System.out.println(enrollment.toString());

            enrollmentDao.persist(enrollment);
            user.getTravel_journal().addEnrollment(enrollment);
            travelJournalDao.update(user.getTravel_journal());
        }
        else {
            System.out.println("!USER DID NOT GET SIGNED UP TO TRIP!");
        }
    }

    @Transactional
    public List<Trip> findAfford(User current_user) throws NotAllowedException {
        if (current_user == null) throw new NotAllowedException();
        User user = accessService.getUser(current_user);
        int level = translateService.countLevel(translateService.translateTravelJournal(user.getTravel_journal()).getXp_count());
        return  tripDao.find(level);
    }

    @Transactional
    public List<Trip> findNotAfford(User current_user) throws NotAllowedException {
        List<Trip> trips = tripDao.findAll();
        trips.removeAll(findAfford(current_user));
        return trips;
    }

    @Transactional
    public void update(String stringId, Trip newTrip) throws BadDateException, NotFoundException, MissingVariableException {
        Trip trip = tripDao.find(stringId);

        if (trip == null) throw new NotFoundException();
        //todo pridat vynimku na rolu

        newTrip.setId(trip.getId());

//        newTrip.setReviews(trip.getReviews());
        if (newTrip.getSessions().size()<=0) throw new MissingVariableException();

        //less new sessions
        if (newTrip.getSessions().size() < trip.getSessions().size()){
            for ( int i = newTrip.getSessions().size() ; i < trip.getSessions().size(); i++) {
                tripSessionDao.remove(trip.getSessions().get(i));
            }
        }

        for (int i = 0; i < newTrip.getSessions().size() ; i++) {
            TripSession newSession = newTrip.getSessions().get(i);
            if (newSession.getTo_date().isBefore(newSession.getFrom_date())) throw new BadDateException();

            if (i <= trip.getSessions().size()-1 ){
                TripSession oldSession = trip.getSessions().get(i);

                newTrip.getSessions().get(i).setId(oldSession.getId());
                oldSession = newSession;
                oldSession.setTrip(trip);
                tripSessionDao.update(oldSession);
            } else {
                newSession.setTrip(trip);
                tripSessionDao.persist(newSession);
            }
        }

        trip=newTrip;
        tripDao.update(trip);
    }

    @Transactional
    public void delete(String stringId) throws NotFoundException {

        Trip trip = tripDao.find(stringId);
        if (trip == null) throw new NotFoundException();

        for (TripSession session :trip.getSessions()) {
            session.softDelete();
            tripSessionDao.update(session);
        }

        trip.softDelete();
        tripDao.update(trip);
    }


    public List<TripDto> getAllTripsByFilter(String location, String from_date, String to_date,
                                             Double maxPrice, String[] search) {

        List<TripDto> tripDtos = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate local_to_date = LocalDate.parse("2030-01-01", formatter);
        if(to_date != null){ local_to_date = LocalDate.parse(to_date, formatter); }

        LocalDate local_from_date = LocalDate.parse("1999-01-01", formatter);
        if(from_date != null){ local_from_date = LocalDate.parse(from_date, formatter); }

        for (Trip trip : tripDao.findByFilter(location,  local_from_date, local_to_date, maxPrice, search)) {
            tripDtos.add(translateService.translateTrip(trip));
        }

        return tripDtos;
    }

    public boolean checkOwnedAchievements(TravelJournal usersJournal, Trip trip) {
        List<AchievementCategorized> ownedCat = usersJournal.getEarnedAchievementsCategorized();
        List<AchievementCertificate> ownedCer = usersJournal.getCertificates();
        List<AchievementSpecial> ownedSpec = usersJournal.getEarnedAchievementsSpecial();

        for (AchievementCategorized ac : trip.getRequired_achievements_categorized()) {
            if(!ownedCat.contains(ac)) {
                System.out.println("UserJournal " + usersJournal + " lacks this achievement" + ac.getName());
                return false;
            }
        }
        for(AchievementSpecial as : trip.getRequired_achievements_special()) {
            if(!ownedSpec.contains(as)) {
                System.out.println("UserJournal " + usersJournal + " lacks this achievement" + as.getName());
                return false;
            }
        }
        for(AchievementCertificate ac : trip.getRequired_achievements_certificate()) {
            if(!ownedCer.contains(ac)) {
                System.out.println("UserJournal " + usersJournal + " lacks this achievement" + ac.getName());
                return false;
            }
        }

        return true;
    }

    private boolean isTripActive(Trip trip) {
        for(TripSession tripSession : trip.getSessions()) {
            if(tripSession.isNotDeleted() && tripSession.getTo_date().isAfter(LocalDate.now()) && tripSession.getFrom_date().isAfter(LocalDate.now())) {
                return true;
            }
        }
        return false;
    }
}
