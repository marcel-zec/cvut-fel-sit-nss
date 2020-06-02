package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.dao.JobJournalDao;
import cz.cvut.fel.nss.parttimejobportal.dto.*;
import cz.cvut.fel.nss.parttimejobportal.model.*;
import cz.cvut.fel.nss.parttimejobportal.dao.CategoryDao;
import cz.cvut.fel.nss.parttimejobportal.dao.OfferDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TranslateService {
    private final JobJournalDao jobJournalDao;
    private final OfferDao offerDao;
    private final CategoryDao categoryDao;

    public TranslateService(JobJournalDao jobJournalDao, OfferDao offerDao, CategoryDao categoryDao) {
        this.jobJournalDao = jobJournalDao;
        this.offerDao = offerDao;
        this.categoryDao = categoryDao;
    }

    @Transactional
    public UserDto translateUser(User user) {
        System.out.println(user.toString());
        Objects.requireNonNull(user);
        List<JobReviewDto> jobReviewDtos = new ArrayList<>();
        List<JobReview> jobReviews = user.getJobReviews();
        List<UserReviewDto> userReviewDtos = new ArrayList<>();
        List<UserReview> userReviews =  user.getUserReviews();

        if (jobReviews.size() > 0){
            jobReviews.forEach(review-> jobReviewDtos.add(translateJobReview(review)));
        }

        if (userReviews.size() > 0){
            userReviews.forEach(review-> userReviewDtos.add(translateUserReview(review)));
        }

        if (user.getTravel_journal() != null) {
            JobJournalDto jobJournalDto = translateJobJournal(user.getTravel_journal());
            return new UserDto(user.getId(),user.getFirstName(),user.getLastName(),user.getEmail(),
                    translateAddress(user.getAddress()), user.getPhone_number(), jobJournalDto,jobReviewDtos,userReviewDtos);
        }

       return new UserDto(user.getId(),user.getFirstName(),user.getLastName(),user.getEmail(),
                translateAddress(user.getAddress()), user.getPhone_number(), null, jobReviewDtos, userReviewDtos);
    }

    @Transactional
    public ManagerDto translateManager(Manager manager) {
        System.out.println(manager.toString());
        Objects.requireNonNull(manager);
        List<UserReviewDto> userReviewDtos = new ArrayList<>();
        List<OfferDto> offersDto = new ArrayList<>();
        List<UserReview> userReviews =  manager.getUserReviewsAuthor();
        List<Offer> offers =  manager.getOffers();


        if (userReviews.size() > 0){
            userReviews.forEach(review-> userReviewDtos.add(translateUserReview(review)));
        }

        if (offers.size() > 0){
            offers.forEach(offer-> offersDto.add(translateTrip(offer)));
        }


        return new ManagerDto(manager.getId(),manager.getFirstName(),manager.getLastName(),manager.getEmail(),
                translateAddress(manager.getAddress()), manager.getPhone_number(),manager.getCompany(), userReviewDtos, offersDto );
    }

    @Transactional
    public AbstractUserDto translateAdmin(Admin admin){
        Objects.requireNonNull(admin);
        return new AbstractUserDto(admin.getId(),admin.getFirstName(), admin.getLastName(), admin.getEmail(), translateAddress(admin.getAddress()), Role.ADMIN);
    }

    @Transactional
    public AddressDto translateAddress(Address address) {
        Objects.requireNonNull(address);

        return new AddressDto(address.getId(),address.getCity(),address.getStreet(),address.getHouseNumber(),address.getZipCode(),
                address.getCountry(),address.getUser().getId());
    }

    @Transactional
    public OfferDto translateTrip(Offer trip) {
        Objects.requireNonNull(trip);
        List<JobSessionDto> sessions = new ArrayList<>();
        List<AchievementCertificateDto> required_certificates = new ArrayList<>();
        List<AchievementCategorizedDto> required_achievements_categorized = new ArrayList<>();
        List<AchievementSpecialDto> required_achievements_special = new ArrayList<>();
        List<AchievementSpecialDto> gain_achievements = new ArrayList<>();
        List<JobReviewDto> jobReviews = new ArrayList<>();
        Offer trip1 = offerDao.find(trip.getId());

        trip1.getRequired_achievements_certificate().forEach(achievementCertificate -> required_certificates.add(translateAchievementCertificate(achievementCertificate)));
        trip1.getRequired_achievements_categorized().forEach(achievementCategorized -> required_achievements_categorized.add(translateAchievementCategorized(achievementCategorized)));
        trip1.getRequired_achievements_special().forEach(achievementSpecial -> required_achievements_special.add(translateAchievementSpecial(achievementSpecial)));
        trip1.getGain_achievements_special().forEach(achievementSpecial -> gain_achievements.add(translateAchievementSpecial(achievementSpecial)));
        trip1.getJobReviews().forEach(review -> jobReviews.add(translateJobReview(review)));
        trip1.getSessions().forEach(session-> sessions.add(translateSession(session)));

        return new OfferDto(trip.getId(),trip.getName(),trip.getShort_name(),trip.getPossible_xp_reward(),
                trip.getDescription(),trip.getRating(),trip.getSalary(),trip.getLocation(), trip.getRequired_level(),
                translateCategory(trip.getCategory()),trip.getAuthor().getId(), required_certificates, required_achievements_categorized, required_achievements_special, gain_achievements, sessions, jobReviews);
    }

    @Transactional
    public JobSessionDto translateSession(JobSession tripSession) {
        Objects.requireNonNull(tripSession);
        return new JobSessionDto(tripSession.getId(),tripSession.getFrom_date(),tripSession.getTo_date(),tripSession.getCapacity(),tripSession.getTrip().getId());
    }

    @Transactional
    public AchievementCertificateDto translateAchievementCertificate(AchievementCertificate achievementCertificate){
        Objects.requireNonNull(achievementCertificate);
        List<Long> trips = new ArrayList<>();
        List<Long> owned_travel_journals = new ArrayList<>();

        achievementCertificate.getTrips().forEach(trip -> trips.add(trip.getId()));
        achievementCertificate.getOwned_travel_journals().forEach(jobJournal -> owned_travel_journals.add(jobJournal.getId()));

        return new AchievementCertificateDto(achievementCertificate.getId(),achievementCertificate.getName(),achievementCertificate.getDescription(),achievementCertificate.getIcon(),
                trips,owned_travel_journals);
    }

    @Transactional
    public AchievementSpecialDto translateAchievementSpecial(AchievementSpecial achievementSpecial){
        Objects.requireNonNull(achievementSpecial);
        List<Long> trips = new ArrayList<>();
        List<Long> owned_travel_journals = new ArrayList<>();

        achievementSpecial.getTrips().forEach(trip -> trips.add(trip.getId()));
        achievementSpecial.getOwned_travel_journals().forEach(jobJournal -> owned_travel_journals.add(jobJournal.getId()));

        return new AchievementSpecialDto(achievementSpecial.getId(),achievementSpecial.getName(),achievementSpecial.getDescription(),achievementSpecial.getIcon(),
                trips,owned_travel_journals);
    }

    @Transactional
    public AchievementCategorizedDto translateAchievementCategorized(AchievementCategorized achievementCategorized){
        Objects.requireNonNull(achievementCategorized);
        List<Long> trips = new ArrayList<>();
        List<Long> owned_travel_journals = new ArrayList<>();

        achievementCategorized.getTrips().forEach(trip -> trips.add(trip.getId()));
        achievementCategorized.getOwned_travel_journals().forEach(jobJournal -> owned_travel_journals.add(jobJournal.getId()));

        return new AchievementCategorizedDto(achievementCategorized.getId(),achievementCategorized.getName(),achievementCategorized.getDescription(),achievementCategorized.getIcon(),
                trips,owned_travel_journals, achievementCategorized.getLimit(), achievementCategorized.getCategory().getId());
    }

    @Transactional
    public JobJournalDto translateJobJournal(JobJournal jobJournal){
        Objects.requireNonNull(jobJournal);
        List<AchievementCertificateDto> certificateDtos = new ArrayList<>();
        List<AchievementCategorizedDto> categorizedDtos = new ArrayList<>();
        List<AchievementSpecialDto> specialDtos = new ArrayList<>();
        HashMap<CategoryDto, Integer> trip_counter= new HashMap<CategoryDto, Integer>();
        JobJournal jobJournal1 = jobJournalDao.find(jobJournal.getId());

        for (Long categoryID : jobJournal1.getTrip_counter().keySet()) {
            Category category = categoryDao.find(categoryID);
            CategoryDto categoryDto= translateCategory(category);
            trip_counter.put(categoryDto,jobJournal.getTrip_counter().get(category));
        }

        jobJournal1.getCertificates().forEach(certificate -> certificateDtos.add(translateAchievementCertificate(certificate)));
        jobJournal1.getEarnedAchievementsCategorized().forEach(categorized -> categorizedDtos.add(translateAchievementCategorized(categorized)));
        jobJournal1.getEarnedAchievementsSpecial().forEach(special -> specialDtos.add(translateAchievementSpecial(special)));

        return new JobJournalDto(jobJournal.getId(), jobJournal.getXp_count(), trip_counter,jobJournal.getUser().getId(), certificateDtos, categorizedDtos, specialDtos, countLevel(jobJournal.getXp_count()));
    }

    @Transactional
    public CategoryDto translateCategory(Category category){

        return category == null ? null : new CategoryDto(category.getId(),category.getName());
    }

    @Transactional
    public EnrollmentDto translateEnrollment(Enrollment enrollment){
        Objects.requireNonNull(enrollment);
        List<AchievementSpecialDto> recieved_achievements_special = new ArrayList<>();

        enrollment.getRecieved_achievements().forEach(achievement_special -> recieved_achievements_special.add(translateAchievementSpecial(achievement_special)));
        JobReviewDto jobReviewDto = enrollment.hasJobReview() ? translateJobReview(enrollment.getJobReview()) : null;

        return new EnrollmentDto(enrollment.getId(),enrollment.getEnrollDate(),enrollment.isDeposit_was_paid(),enrollment.getActual_xp_reward(),enrollment.getState(),
                recieved_achievements_special,enrollment.getJobJournal().getId(),translateTrip(enrollment.getTrip()),translateSession(enrollment.getTripSession()),jobReviewDto);
    }

    @Transactional
    public JobReviewDto translateJobReview(JobReview jobReview){
        Objects.requireNonNull(jobReview);

        return new JobReviewDto(jobReview.getId(),jobReview.getNote(),jobReview.getDate(),
                jobReview.getRating(),jobReview.getAuthor().getFirstName() + " " +jobReview.getAuthor().getLastName());
    }

    @Transactional
    public UserReviewDto translateUserReview(UserReview userReview){
        Objects.requireNonNull(userReview);

        return new UserReviewDto(userReview.getId(),userReview.getNote(),userReview.getDate(),
                userReview.getRating(),userReview.getUser().getId(),userReview.getAuthor().getId(),translateSession(userReview.getTripSession()));
    }

    @Transactional
    public int countLevel(int xp){
        return xp/10 ;
    }
}
