package cz.cvut.fel.nss.parttimejobportal.service;

import com.hazelcast.core.HazelcastInstance;
import cz.cvut.fel.nss.parttimejobportal.dao.*;
import cz.cvut.fel.nss.parttimejobportal.dto.OfferDto;
import cz.cvut.fel.nss.parttimejobportal.dto.JobSessionDto;
import cz.cvut.fel.nss.parttimejobportal.model.*;
import cz.cvut.fel.nss.parttimejobportal.exception.BadDateException;
import cz.cvut.fel.nss.parttimejobportal.exception.MissingVariableException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotAllowedException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.rest.OfferController;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityUtils;
import cz.cvut.fel.nss.parttimejobportal.security.model.UserDetails;
import cz.cvut.fel.nss.parttimejobportal.service.security.AccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OfferService {

    private static final Logger LOG = LoggerFactory.getLogger(OfferService.class);
    private final OfferDao offerDao;
    private final JobSessionDao jobSessionDao;
    private final JobReviewDao jobReviewDao;
    private final TranslateService translateService;
    private final AccessService accessService;
    private final UserDao userDao;
    private final EnrollmentDao enrollmentDao;
    private final JobJournalDao jobJournalDao;
    private final ManagerDao managerDao;
    private final AdminDao adminDao;
    private final HazelcastInstance cacheInstance;
    private Map<Long, OfferDto> cache;

    @Autowired
    public OfferService(OfferDao offerDao, JobSessionDao jobSessionDao, JobReviewDao jobReviewDao, TranslateService translateService, AccessService accessService, UserDao userDao, EnrollmentDao enrollmentDao, JobJournalDao jobJournalDao, ManagerDao managerDao, AdminDao adminDao, HazelcastInstance hazelcastInstance) {
        this.offerDao = offerDao;
        this.jobSessionDao = jobSessionDao;
        this.jobReviewDao = jobReviewDao;
        this.translateService = translateService;
        this.accessService = accessService;
        this.userDao = userDao;
        this.enrollmentDao = enrollmentDao;
        this.jobJournalDao = jobJournalDao;
        this.managerDao = managerDao;
        this.adminDao = adminDao;
        this.cacheInstance = hazelcastInstance;
        this.cache = cacheInstance.getMap("offers");
    }


    /**
     * Get all Offers from database.
     * @return <List<Offer>
     */
    @Transactional
    public List<Offer> findAll() {
        return offerDao.findAll();
    }


    /**
     * Get all Offers.
     * @return  List<OfferDto>
     */
    @Transactional
    public List<OfferDto> findAllDto() {
        List<OfferDto> offerDtos = new ArrayList<>();
        for (Offer offer:offerDao.findAll()) {
            if (SecurityUtils.getCurrentUser().getRole() == Role.MANAGER && offer.getAuthor().getId().equals(SecurityUtils.getCurrentUser().getId())) offerDtos.add(translateService.translateTrip(offer));
            else if (SecurityUtils.getCurrentUser().getRole() == Role.ADMIN) offerDtos.add(translateService.translateTrip(offer));
        }
        return offerDtos;
    }


    /**
     * Get all active offers.
     * @return Collection<OfferDto>
     */
    @Transactional
    public Collection<OfferDto> findAllDtoFiltered() {

        if(cache.isEmpty()) offersToCache();

        removeOldOffersFromCache();
        return cache.values();
    }


    /**
     * Putting offers to empty cache.
     */
    private void offersToCache(){
        LOG.info("Putting offers to empty cache.");
        List<OfferDto> offerDtos = new ArrayList<>();
        for (Offer offer:offerDao.findAll()) offerDtos.add(translateService.translateTrip(offer));
        offerDtos.removeIf(offerDto -> !isTripActive(offerDto));
        for(OfferDto offer: offerDtos) cache.put(offer.getId(),offer);
    }


    /**
     * Remove offers from cache.
     */
    private void removeOldOffersFromCache(){
        int sizeBefore = cache.size();
        cache.values().removeIf(offerDto -> !isTripActive(offerDto));
        int sizeAfter = cache.size();
        if (sizeAfter < sizeBefore) LOG.info("Removed "+ (sizeBefore - sizeAfter) + " offers from cache.");
    }


    /**
     * Get Offer by id.
     * @param id
     * @return OfferDto
     */
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


    /**
     * Get offer by string id.
     * @param stringId
     * @return OfferDto
     */
    @Transactional
    public OfferDto findByString(String stringId) {
        Offer trip = offerDao.find(stringId);

        return translateService.translateTrip(trip);
    }


    /**
     * Get offer by string id and active.
     * @param stringId
     * @return
     */
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


    /**
     * Create new Offer.
     * @param offer
     * @throws BadDateException if to_date is before from_date
     * @throws MissingVariableException if offer has not sessions
     */
    @Transactional
    public void create(Offer offer) throws BadDateException, MissingVariableException {

        Objects.requireNonNull(offer);
        if (offer.getSessions().size()<=0) throw new MissingVariableException();
        offer.setAuthor(managerDao.find(SecurityUtils.getCurrentUser().getId()));
        offerDao.persist(offer);
        for (JobSession session: offer.getSessions()) {
            if (session.getTo_date().isBefore(session.getFrom_date())) {
                offerDao.remove(offer);
                throw new BadDateException();
            }
            session.setTrip(offer);
            jobSessionDao.persist(session);
        }
        offerDao.update(offer);
        addOfferToCache(translateService.translateTrip(offer));
    }


    /**
     * add offer to cache
     * @param offerDto
     */
    private void addOfferToCache(OfferDto offerDto) {
        cache.put(offerDto.getId(),offerDto);
        LOG.info("Putting new offer (ID: " + offerDto.getId() + ") to cache.");
    }


    /**
     * Sign up current logged in user to trip.
     * @param tripSessionDto
     * @param current_user
     * @throws NotAllowedException
     */
    @Transactional
    public void signUpToTrip(JobSessionDto tripSessionDto, AbstractUser current_user) throws NotAllowedException {
        JobSession tripSession = jobSessionDao.find(tripSessionDto.getId());
//      TODO odkomentovat ked bude nasadene na produkcny server
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
            enrollment.setJobJournal(user.getTravel_journal());

            System.out.println(enrollment.toString());

            enrollmentDao.persist(enrollment);
            user.getTravel_journal().addEnrollment(enrollment);
            jobJournalDao.update(user.getTravel_journal());
        }
        else {
            System.out.println("!USER DID NOT GET SIGNED UP TO TRIP!");
        }
    }


    /**
     * Get all offer that user can afford by level.
     * @param current_user
     * @return List<Offer>
     * @throws NotAllowedException
     */
    @Transactional
    public List<Offer> findAfford(AbstractUser current_user) throws NotAllowedException {
        if (current_user == null) throw new NotAllowedException();
        User user = userDao.find(current_user.getId());
        if (user == null) throw new NotAllowedException();
        int level = translateService.countLevel(translateService.translateJobJournal(user.getTravel_journal()).getXp_count());
        return  offerDao.find(level);
    }


    /**
     *  Get all offer that user can not afford by level.
     * @param current_user
     * @return List<Offer>
     * @throws NotAllowedException
     */
    @Transactional
    public List<Offer> findNotAfford(AbstractUser current_user) throws NotAllowedException {
        List<Offer> trips = offerDao.findAll();
        trips.removeAll(findAfford(current_user));
        return trips;
    }


    /**
     * Update offer.
     * @param stringId offer string id
     * @param newOffer new updated offer
     * @throws BadDateException if to_date is before from_date
     * @throws NotFoundException if offer doesnt exist
     * @throws MissingVariableException if offer has not sessions
     * @throws NotAllowedException if own manager is not logged in
     */
    @Transactional
    public void update(String stringId, Offer newOffer) throws BadDateException, NotFoundException, MissingVariableException, NotAllowedException {
        Offer offer = offerDao.find(stringId);
        if (offer == null) throw new NotFoundException();

        Manager manager = managerDao.find(SecurityUtils.getCurrentUser().getId());
        if (manager!=null && !offer.getAuthor().getId().equals(manager.getId())) throw new NotAllowedException("You are not allowed to update this offer.");

        newOffer.setId(offer.getId());
        newOffer.setAuthor(manager);

//        newTrip.setReviews(trip.getReviews());
        if (newOffer.getSessions().size()<=0) throw new MissingVariableException();

        //less new sessions
        if (newOffer.getSessions().size() < offer.getSessions().size()){
            for ( int i = newOffer.getSessions().size() ; i < offer.getSessions().size(); i++) {
                jobSessionDao.remove(offer.getSessions().get(i));
            }
        }

        for (int i = 0; i < newOffer.getSessions().size() ; i++) {
            JobSession newSession = newOffer.getSessions().get(i);
            if (newSession.getTo_date().isBefore(newSession.getFrom_date())) throw new BadDateException();

            if (i <= offer.getSessions().size()-1 ){
                JobSession oldSession = offer.getSessions().get(i);

                newOffer.getSessions().get(i).setId(oldSession.getId());
                oldSession = newSession;
                oldSession.setTrip(offer);
                jobSessionDao.update(oldSession);
            } else {
                newSession.setTrip(offer);
                jobSessionDao.persist(newSession);
            }
        }
        offer=newOffer;
        offerDao.update(offer);
    }


    /**
     * Delete offer by string id.
     * @param stringId
     * @throws NotFoundException
     * @throws NotAllowedException
     */
    @Transactional
    public void delete(String stringId) throws NotFoundException, NotAllowedException {

        Offer offer = offerDao.find(stringId);
        Manager manager = null;

        if (SecurityUtils.getCurrentUser().getRole() == Role.MANAGER) manager = managerDao.find(SecurityUtils.getCurrentUser().getId());
        if (offer == null) throw new NotFoundException();

        if (manager!=null && offer.getAuthor() != manager ) throw new NotAllowedException("You are not allowed delete this offer.");

        for (JobSession session :offer.getSessions()) {
            session.softDelete();
            jobSessionDao.update(session);
        }

        offer.softDelete();
        offerDao.update(offer);
    }


    /**
     * Filter offers by location, from_date, to_date, price
     * @param location
     * @param from_date
     * @param to_date
     * @param minPrice
     * @param search
     * @return
     */
    public List<OfferDto> getAllTripsByFilter(String location, String from_date, String to_date,
                                             Double minPrice, String[] search) {

        List<OfferDto> tripDtos = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate local_to_date = LocalDate.parse("2030-01-01", formatter);
        if(to_date != null){ local_to_date = LocalDate.parse(to_date, formatter); }

        LocalDate local_from_date = LocalDate.parse("1999-01-01", formatter);
        if(from_date != null){ local_from_date = LocalDate.parse(from_date, formatter); }

        for (Offer trip : offerDao.findByFilter(location,  local_from_date, local_to_date, minPrice, search)) {
            tripDtos.add(translateService.translateTrip(trip));
        }

        return tripDtos;
    }


    /**
     * Checking whether user own enough achievements to sign up to offer
     * @param usersJournal
     * @param trip
     * @return boolean
     */
    public boolean checkOwnedAchievements(JobJournal usersJournal, Offer trip) {
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


    /**
     * Whether offer is active or not.
     * @param offer
     * @return boolean
     */
    private boolean isTripActive(OfferDto offer) {
        for(JobSessionDto tripSession : offer.getSessions()) {
            if(tripSession.getTo_date().isAfter(LocalDate.now()) && tripSession.getFrom_date().isAfter(LocalDate.now())) {
                return true;
            }
        }
        return false;
    }
}
