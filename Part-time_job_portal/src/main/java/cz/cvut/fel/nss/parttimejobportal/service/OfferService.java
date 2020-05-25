package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.dao.*;
import cz.cvut.fel.nss.parttimejobportal.dto.OfferDto;
import cz.cvut.fel.nss.parttimejobportal.dto.JobSessionDto;
import cz.cvut.fel.nss.parttimejobportal.model.*;
import cz.cvut.fel.nss.parttimejobportal.exception.BadDateException;
import cz.cvut.fel.nss.parttimejobportal.exception.MissingVariableException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotAllowedException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
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
public class OfferService {

    private final OfferDao offerDao;
    private final JobSessionDao jobSessionDao;
    private final TripReviewDao tripReviewDao;
    private final TranslateService translateService;
    private final AccessService accessService;
    private final UserDao userDao;
    private final EnrollmentDao enrollmentDao;
    private final TravelJournalDao travelJournalDao;
    private final ManagerDao managerDao;

    @Autowired
    public OfferService(OfferDao offerDao, JobSessionDao jobSessionDao, TripReviewDao tripReviewDao, TranslateService translateService, AccessService accessService, UserDao userDao, EnrollmentDao enrollmentDao, TravelJournalDao travelJournalDao, ManagerDao managerDao) {
        this.offerDao = offerDao;
        this.jobSessionDao = jobSessionDao;
        this.tripReviewDao = tripReviewDao;
        this.translateService = translateService;
        this.accessService = accessService;
        this.userDao = userDao;
        this.enrollmentDao = enrollmentDao;
        this.travelJournalDao = travelJournalDao;
        this.managerDao = managerDao;
    }

    @Transactional
    public List<Offer> findAll() {
        return offerDao.findAll();
    }

    @Transactional
    public List<OfferDto> findAllDto() {
        List<OfferDto> tripDtos = new ArrayList<>();

        for (Offer trip:offerDao.findAll()) {
            tripDtos.add(translateService.translateTrip(trip));
        }
        return tripDtos;
    }

    @Transactional
    public List<OfferDto> findAllDtoFiltered() {
        List<OfferDto> tripDtos = new ArrayList<>();

        for (Offer trip:offerDao.findAll()) {
            if(isTripActive(trip)) {
                tripDtos.add(translateService.translateTrip(trip));
            }
        }
        return tripDtos;
    }

    @Transactional
    public OfferDto find(Long id) {
        Offer trip = offerDao.find(id);
        UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
        //do not werk and we propably do not use this so whatever
        if(userDetails != null){
            System.out.println("THIS?");
                if (!userDetails.getUser().getRole().equals(Role.USER)) {
                    System.out.println("AM I doing this?");
                    return translateService.translateTrip(trip);
                }
        }

        List<JobSession> sessions = new ArrayList<>();
        for(JobSession tripSession : trip.getSessions()) {
            if(tripSession.isNotDeleted() && tripSession.getTo_date().isAfter(LocalDate.now())) {
                sessions.add(tripSession);
            }
        }
        trip.setSessions(sessions);

        return translateService.translateTrip(trip);
    }

    @Transactional
    public OfferDto findByString(String stringId) {
        Offer trip = offerDao.find(stringId);

        return translateService.translateTrip(trip);
    }

    @Transactional
    public OfferDto findByStringFiltered(String stringId) {
        Offer trip = offerDao.find(stringId);
        OfferDto tripDto = translateService.translateTrip(trip);

        List<JobSessionDto> sessions = new ArrayList<>();
        for(JobSession tripSession : trip.getSessions()) {
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
    public void create(Offer offer) throws BadDateException, MissingVariableException {

        Objects.requireNonNull(offer);
        if (offer.getSessions().size()<=0) throw new MissingVariableException();
        offerDao.persist(offer);
        for (JobSession session: offer.getSessions()) {
            if (session.getTo_date().isBefore(session.getFrom_date())) {
                offerDao.remove(offer);
                throw new BadDateException();
            }
            session.setTrip(offer);
            jobSessionDao.persist(session);
        }
        offer.setAuthor(managerDao.find(SecurityUtils.getCurrentUser().getId()));
        offerDao.update(offer);
    }

    @Transactional
    public void signUpToTrip(JobSessionDto tripSessionDto, AbstractUser current_user) throws NotAllowedException {
        JobSession tripSession = jobSessionDao.find(tripSessionDto.getId());
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
    public List<Offer> findAfford(AbstractUser current_user) throws NotAllowedException {
        if (current_user == null) throw new NotAllowedException();
        User user = userDao.find(current_user.getId());
        if (user == null) throw new NotAllowedException();
        int level = translateService.countLevel(translateService.translateTravelJournal(user.getTravel_journal()).getXp_count());
        return  offerDao.find(level);
    }

    @Transactional
    public List<Offer> findNotAfford(AbstractUser current_user) throws NotAllowedException {
        List<Offer> trips = offerDao.findAll();
        trips.removeAll(findAfford(current_user));
        return trips;
    }

    @Transactional
    public void update(String stringId, Offer newTrip) throws BadDateException, NotFoundException, MissingVariableException {
        Offer trip = offerDao.find(stringId);

        if (trip == null) throw new NotFoundException();
        //todo pridat vynimku na rolu

        newTrip.setId(trip.getId());
        newTrip.setAuthor(managerDao.find(SecurityUtils.getCurrentUser().getId()));

//        newTrip.setReviews(trip.getReviews());
        if (newTrip.getSessions().size()<=0) throw new MissingVariableException();

        //less new sessions
        if (newTrip.getSessions().size() < trip.getSessions().size()){
            for ( int i = newTrip.getSessions().size() ; i < trip.getSessions().size(); i++) {
                jobSessionDao.remove(trip.getSessions().get(i));
            }
        }

        for (int i = 0; i < newTrip.getSessions().size() ; i++) {
            JobSession newSession = newTrip.getSessions().get(i);
            if (newSession.getTo_date().isBefore(newSession.getFrom_date())) throw new BadDateException();

            if (i <= trip.getSessions().size()-1 ){
                JobSession oldSession = trip.getSessions().get(i);

                newTrip.getSessions().get(i).setId(oldSession.getId());
                oldSession = newSession;
                oldSession.setTrip(trip);
                jobSessionDao.update(oldSession);
            } else {
                newSession.setTrip(trip);
                jobSessionDao.persist(newSession);
            }
        }

        trip=newTrip;
        offerDao.update(trip);
    }

    @Transactional
    public void delete(String stringId) throws NotFoundException {

        Offer trip = offerDao.find(stringId);
        if (trip == null) throw new NotFoundException();

        for (JobSession session :trip.getSessions()) {
            session.softDelete();
            jobSessionDao.update(session);
        }

        trip.softDelete();
        offerDao.update(trip);
    }


    public List<OfferDto> getAllTripsByFilter(String location, String from_date, String to_date,
                                             Double maxPrice, String[] search) {

        List<OfferDto> tripDtos = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate local_to_date = LocalDate.parse("2030-01-01", formatter);
        if(to_date != null){ local_to_date = LocalDate.parse(to_date, formatter); }

        LocalDate local_from_date = LocalDate.parse("1999-01-01", formatter);
        if(from_date != null){ local_from_date = LocalDate.parse(from_date, formatter); }

        for (Offer trip : offerDao.findByFilter(location,  local_from_date, local_to_date, maxPrice, search)) {
            tripDtos.add(translateService.translateTrip(trip));
        }

        return tripDtos;
    }

    public boolean checkOwnedAchievements(TravelJournal usersJournal, Offer trip) {
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

    private boolean isTripActive(Offer trip) {
        for(JobSession tripSession : trip.getSessions()) {
            if(tripSession.isNotDeleted() && tripSession.getTo_date().isAfter(LocalDate.now()) && tripSession.getFrom_date().isAfter(LocalDate.now())) {
                return true;
            }
        }
        return false;
    }
}
