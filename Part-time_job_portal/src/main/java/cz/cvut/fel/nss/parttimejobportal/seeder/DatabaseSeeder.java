package cz.cvut.fel.nss.parttimejobportal.seeder;

import cz.cvut.fel.nss.parttimejobportal.dao.*;
import cz.cvut.fel.nss.parttimejobportal.dto.JobSessionDto;
import cz.cvut.fel.nss.parttimejobportal.model.*;
import cz.cvut.fel.nss.parttimejobportal.service.EnrollmentService;
import cz.cvut.fel.nss.parttimejobportal.service.TranslateService;
import cz.cvut.fel.nss.parttimejobportal.service.TravelJournalService;
import cz.cvut.fel.nss.parttimejobportal.service.TripService;
import cz.cvut.fel.nss.parttimejobportal.dao.*;
import cz.cvut.fel.nss.parttimejobportal.exception.NotAllowedException;
import cz.cvut.fel.nss.parttimejobportal.model.*;
import cz.cvut.fel.nss.parttimejobportal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
public class DatabaseSeeder implements
        ApplicationListener<ContextRefreshedEvent> {

    private Logger LOGGER = Logger.getLogger(DatabaseSeeder.class.getName());
    private TripDao tripDao;
    private TripSessionDao tripSessionDao;
    private AchievementCertificateDao achievementCertificateDao;
    private AchievementCategorizedDao achievementCategorizedDao;
    private AchievementSpecialDao achievementSpecialDao;
    private CategoryDao categoryDao;
    private UserDao userDao;
    private AddressDao addressDao;
    private EnrollmentDao enrollmentDao;
    private TripService tripService;
    private TranslateService translateService;
    private TravelJournalService travelJournalService;
    private TravelJournalDao travelJournalDao;
    private TripReviewDao tripReviewDao;
    private UserReviewDao userReviewDao;
    private EnrollmentService enrollmentService;

    @Autowired
    public DatabaseSeeder(TripDao tripDao, TripSessionDao tripSessionDao, AchievementCertificateDao achievementCertificateDao,
                          AchievementCategorizedDao achievementCategorizedDao, AchievementSpecialDao achievementSpecialDao,
                          CategoryDao categoryDao, UserDao userDao, AddressDao addressDao, EnrollmentDao enrollmentDao,
                          TripService tripService, TranslateService translateService, TravelJournalService travelJournalService,
                          TravelJournalDao travelJournalDao, TripReviewDao tripReviewDao, UserReviewDao userReviewDao, EnrollmentService enrollmentService) {
        this.tripDao = tripDao;
        this.tripSessionDao = tripSessionDao;
        this.achievementCertificateDao = achievementCertificateDao;
        this.achievementCategorizedDao = achievementCategorizedDao;
        this.achievementSpecialDao = achievementSpecialDao;
        this.categoryDao = categoryDao;
        this.userDao = userDao;
        this.addressDao = addressDao;
        this.enrollmentDao = enrollmentDao;
        this.tripService = tripService;
        this.translateService = translateService;
        this.travelJournalService = travelJournalService;
        this.travelJournalDao = travelJournalDao;
        this.tripReviewDao = tripReviewDao;
        this.userReviewDao = userReviewDao;
        this.enrollmentService = enrollmentService;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //TODO - vykona sa hned po spusteni
        System.out.println("Vypis po stupusteni aplikacie.");

        createCategories();
        createAchievement();
        createTrips();
        setAchievementsAndCategories();
        createUsers();
        addAchievementsToUsers();
        try {
            signUsersToTrips();
        } catch (NotAllowedException e) {
            e.printStackTrace();
        }
        createTripReviews();
        createUserReviews();
    }

    private void createUserReviews() {
        //1.userReview from Milan to Jan
        User author = userDao.findByEmail("milan@gmail.com");
        User user = userDao.findByEmail("jan@gmail.com");
        JobSession tripSession = user.getTravel_journal().getEnrollments().get(0).getTripSession();
        UserReview userReview = new UserReview("It was a pleasure to work with you, Jane :) ", LocalDateTime.now(), 5, user, author, tripSession);
        userReviewDao.persist(userReview);

        //2.userReview from Jan to Milan
        author = userDao.findByEmail("jan@gmail.com");
        user = userDao.findByEmail("milan@gmail.com");
        tripSession = user.getTravel_journal().getEnrollments().get(0).getTripSession();
        userReview = new UserReview("You are a super fugu guy, Milane :) ", LocalDateTime.now(), 5, user, author, tripSession);
        userReviewDao.persist(userReview);

        //3.userReview from Milan to Julia
//        author = userDao.findByEmail("milan@gmail.com");
//        user = userDao.findByEmail("july1331@gmail.com");
//        tripSession = user.getTravel_journal().getEnrollments().get(0).getTripSession();
//        userReview = new UserReview("I hope I see you again someday, Julia :) ", LocalDateTime.now(), 5, user, author, tripSession);
//        userReviewDao.persist(userReview);
    }

    private void createTripReviews() {
        //1.tripReview from Milan
        User author = userDao.findByEmail("milan@gmail.com");
        if(author.getTravel_journal().getEnrollments().size() > 0) {
            Enrollment enrollment = author.getTravel_journal().getEnrollments().get(0);
            TripReview tripReview = new TripReview("Really good trip, love it <3", LocalDateTime.now(), 5, author, enrollment.getTrip(),enrollment);
            tripReviewDao.persist(tripReview);
            updateTripRating(tripReview.getTrip(), tripReview.getRating());
        }

        //2.tripReview from Milan
        author = userDao.findByEmail("milan@gmail.com");
        if(author.getTravel_journal().getEnrollments().size() > 1) {
            Enrollment enrollment = author.getTravel_journal().getEnrollments().get(1);
            TripReview tripReview = new TripReview("it was good, but the whether was really bad :( ", LocalDateTime.now(), 3, author, enrollment.getTrip(),enrollment);
            tripReviewDao.persist(tripReview);
            updateTripRating(tripReview.getTrip(), tripReview.getRating());
        }

        //3.tripReview from Jan
        author = userDao.findByEmail("jan@gmail.com");
        if(author.getTravel_journal().getEnrollments().size() > 0) {
            Enrollment enrollment = author.getTravel_journal().getEnrollments().get(0);
            TripReview tripReview = new TripReview("it was the best trip of my entire life! Don't be afraid to enrol ;) ", LocalDateTime.now(), 3, author, enrollment.getTrip(),enrollment);
            tripReviewDao.persist(tripReview);
            updateTripRating(tripReview.getTrip(), tripReview.getRating());
        }
    }

    @Transactional
    void createTrips(){
        String description;// = "Nullam gravida lectus tempus congue pretium. Nunc volutpat diam orci, a consectetur dui iaculis sollicitudin. Fusce varius nisi placerat turpis viverra pulvinar. Pellentesque vel commodo nibh, sed volutpat nunc. Duis congue enim malesuada sapien commodo egestas. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Vestibulum interdum, ante eu vehicula porttitor, libero purus consequat metus, quis aliquet lectus orci sit amet mi. Aenean libero sapien, tempus sit amet lorem in, cursus sodales erat. Vivamus suscipit felis et ex pulvinar, vitae rutrum diam tempus. ";
        Offer trip;/* = new Offer("Casablanca Me gusto",15,description,"casablanca_me_gusta",1000,"Casablan, Mexico",2);
        tripDao.persist(trip);
        */JobSession tripSession;

        //priklady tripov a user progressu medzi nimi 0
        description = "Tento zajezd bude mit cenu za dopravu a kurz, po absolvování se odemkne achievement ´kuchař ryb fugu´, pro absolvování je potřeba mít achievement ´Kuchtík´." ;
        trip = new Offer("Kurz vaření ryb Fugu",10,description,"fugukurz",150,"Tokyo, Japan",1);
        //trip.addGainAchievement();
        //trip.addRequiredAchievement();
        tripDao.persist(trip);
        tripSession = new JobSession(trip, LocalDate.parse("2020-06-06"), LocalDate.parse("2020-06-12"), 3);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripSession = new JobSession(trip, LocalDate.parse("2020-06-12"), LocalDate.parse("2020-06-18"), 3);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripSession = new JobSession(trip, LocalDate.parse("2020-06-18"), LocalDate.parse("2020-06-24"), 3);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripDao.update(trip);

        //1
        description = "Tento zajezd bude mit zalohu, pro absolvování je potřeba mít achievement ´Kuchař ryb fugu´." ;
        trip = new Offer("Vaření ryb Fugu, Praha",10,description,"fuguvar",120,"Praha, Česká republika",1);
        tripDao.persist(trip);

        tripSession = new JobSession(trip, LocalDate.parse("2020-07-06"), LocalDate.parse("2020-07-12"), 4);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);

        tripSession = new JobSession(trip, LocalDate.parse("2020-07-12"), LocalDate.parse("2020-07-18"), 3);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);

        tripSession = new JobSession(trip, LocalDate.parse("2020-07-18"), LocalDate.parse("2020-07-24"), 5);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);

        //tripSession ma datum  ukonceni vcera
        tripSession = new JobSession(trip, LocalDate.now().minusDays(16), LocalDate.now().minusDays(1), 4);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);

        //tripSession ma datum  ukonceni predevcirem
        tripSession = new JobSession(trip, LocalDate.now().minusDays(15), LocalDate.now().minusDays(2), 3);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);

        //tripSession ma datum  ukonceni pred tydnem
        tripSession = new JobSession(trip, LocalDate.now().minusDays(21), LocalDate.now().minusDays(7), 2);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);

        tripDao.update(trip);

        //2.trip "Kuchař na Pražském hradě"
        description = "Tento zajezd bude mit zalohu, pro absolvování je potřeba mít achievement ´Kuchař´." ;
        trip = new Offer("Kuchař na Pražském hradě",8,description,"prahradvar",110,"Praha, Česká republika",3);
        tripDao.persist(trip);
        tripSession = new JobSession(trip, LocalDate.parse("2020-07-06"), LocalDate.parse("2020-07-12"), 5);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripSession = new JobSession(trip, LocalDate.parse("2020-07-12"), LocalDate.parse("2020-07-18"), 4);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripSession = new JobSession(trip, LocalDate.parse("2020-07-18"), LocalDate.parse("2020-07-24"), 8);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripDao.update(trip);

        //3.trip "Kuchař menza Studentský dům, Praha"
        description = "Tento zajezd nevyzaduje zadne achievementy a po nem se nedaji ziskat specialni achievementy ale daji se ziskat achievementy jako jsou např. ´Kuchtík´, ´Kuchař´ apod. Odměna Xp je dost nízká aby se nedalo jednoduše dostat za tuhle práci na prestižnější místa jako pražský hrad, ale zároveň je možno si dopomoct s touto lehčí a dostupnější práci nahnat achievement kuchař, jestliže xp grind mám za sebou z jiných zájezdů." ;
        trip = new Offer("Kuchař menza Studentský dům, Praha",3,description,"studumkuch",140,"Praha, Česká republika",0);
        tripDao.persist(trip);
        tripSession = new JobSession(trip, LocalDate.parse("2020-06-06"), LocalDate.parse("2020-06-12"), 2);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripSession = new JobSession(trip, LocalDate.parse("2020-06-12"), LocalDate.parse("2020-06-18"), 3);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripSession = new JobSession(trip, LocalDate.parse("2020-06-18"), LocalDate.parse("2020-06-24"), 2);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripDao.update(trip);

        //4.trip "projekt „Úsměv pro všechny“"
        description = "Humanitární akce v imigračním táboře Ušivak v Bosně a Hercegovině. Potřeba znát základy javy, office a nebát se ušpinit si ruce při stavbě skleníku." ;
        trip = new Offer("projekt „Úsměv pro všechny“",3,description,"usibos",200,"tábor Ušivak, Bosna a Hercegovina",0);
        tripDao.persist(trip);
        tripSession = new JobSession(trip, LocalDate.parse("2020-06-06"), LocalDate.parse("2020-06-12"), 3);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);

        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripSession = new JobSession(trip, LocalDate.parse("2020-06-18"), LocalDate.parse("2020-06-24"), 5);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripSession = new JobSession(trip, LocalDate.parse("2020-06-24"), LocalDate.parse("2020-06-30"), 3);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripDao.update(trip);

        //5. trip - bez sessions
        description = "Neaktivni trip. Nema aktivni sessions, je viditelny pouze Administratorem.";
        trip = new Offer("Retired trip", 12, description, "retrip", 135, "London, Great Britain", 5);
        tripDao.persist(trip);
        //tripSession ma datum  ukonceni vcera
        tripSession = new JobSession(trip, LocalDate.now().minusDays(16), LocalDate.now().minusDays(1), 2);
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripDao.update(trip);

        //6.trip "Animátor v českém krumlově"
        description = "Pojď animovat zábavní program pro účastníky zájezdů v Českém Krumlově! Zkušenosti nepotřebuješ jenom úsměv na rtech a odhodlání rozdávat radost." ;
        trip = new Offer("Animátor v Českém Krumlově",6,description,"czekrum",180,"Český Krumlov, Česká republika",1);
        tripDao.persist(trip);
        tripSession = new JobSession(trip, LocalDate.parse("2018-08-06"), LocalDate.parse("2018-08-12"), 5); //0
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripSession = new JobSession(trip, LocalDate.parse("2018-08-12"), LocalDate.parse("2018-08-18"), 4);//1
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripSession = new JobSession(trip, LocalDate.parse("2018-08-18"), LocalDate.parse("2018-08-24"), 8);//2
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripSession = new JobSession(trip, LocalDate.parse("2019-08-06"), LocalDate.parse("2019-08-12"), 2);//3
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripSession = new JobSession(trip, LocalDate.parse("2019-08-12"), LocalDate.parse("2019-08-18"), 3);//4
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripSession = new JobSession(trip, LocalDate.parse("2019-08-18"), LocalDate.parse("2019-08-24"), 3);//5
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripSession = new JobSession(trip, LocalDate.parse("2020-08-06"), LocalDate.parse("2020-08-12"), 3);//6
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripSession = new JobSession(trip, LocalDate.parse("2020-08-12"), LocalDate.parse("2020-08-18"), 8);//7
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripSession = new JobSession(trip, LocalDate.parse("2020-08-18"), LocalDate.parse("2020-08-24"), 2);//8
        tripSessionDao.persist(tripSession);
        trip.addSession(tripSession);
        tripDao.update(trip);
    }

    @Transactional
    void createAchievement(){
        AchievementCertificate achievementCertificate;
        AchievementSpecial achievementSpecial;
        AchievementCategorized achievementCategorized;
        //ACHIEVEMENTY ZAJEZDOVE

        //Certifikáty
        achievementCertificate = new AchievementCertificate("Certifikát Angličtina B2", "Uživatel má certifikát B2 v anglickém jazyku.", "graduation-cap"); //0
        achievementCertificateDao.persist(achievementCertificate);
        achievementCertificate = new AchievementCertificate("Certifikát Španěličina C1", "Uživatel má certifikát C1 v španělském jazyku.", "graduation-cap"); //1
        achievementCertificateDao.persist(achievementCertificate);

        //Specifické achievementy
        achievementSpecial = new AchievementSpecial("Kuchař sushi", "Uživatel má zkušenosti s přípravou sushi.", "fish"); //0
        achievementSpecialDao.persist(achievementSpecial);

        achievementSpecial = new AchievementSpecial("Kuchař ryby fugu", "Uživatel má zkušenosti s přípravou jedovatých ryb fugu.", "fish"); //1
        achievementSpecialDao.persist(achievementSpecial);

        achievementSpecial = new AchievementSpecial("Master Kung-Fugu", "Uživatel je sama zkušenost ohledne přípravy jedovatých ryb fugu.", "fish"); //2
        achievementSpecialDao.persist(achievementSpecial);

        achievementSpecial = new AchievementSpecial("Horolezec", "Uživatel má zkušenosti s lezením po skalách.", "mountain"); //3
        achievementSpecialDao.persist(achievementSpecial);

        achievementSpecial = new AchievementSpecial("Restaurátor hradů", "Uživatel má zkušenosti s restaurací starých památek.", "chess-rook"); //3
        achievementSpecialDao.persist(achievementSpecial);

        //Achievementy za počet zájezdů v konkrétních kategoriích
        achievementCategorized = new AchievementCategorized("Kuchtík", "Uživatel byl jednou vařit.", "hamburger"); //0
        achievementCategorized.setCategory(categoryDao.findAll().get(0));
        achievementCategorized.setLimit(1);
        achievementCategorizedDao.persist(achievementCategorized);

        achievementCategorized = new AchievementCategorized("Kuchař", "Uživatel vařil už na 5-ti zájezdech.", "pizza-slice"); //1
        achievementCategorized.setCategory(categoryDao.findAll().get(0));
        achievementCategorized.setLimit(5);
        achievementCategorizedDao.persist(achievementCategorized);

        achievementCategorized = new AchievementCategorized("Pohl v Reichu", "Uživatel vařil už na 15-ti zájezdech.", "glass-cheers"); //2
        achievementCategorized.setCategory(categoryDao.findAll().get(0));
        achievementCategorized.setLimit(15);
        achievementCategorizedDao.persist(achievementCategorized);

        achievementCategorized = new AchievementCategorized("Šprýmař", "Uživatel byl jednou dělat animátora.", "hamburger"); //3
        achievementCategorized.setCategory(categoryDao.findAll().get(5));
        achievementCategorized.setLimit(1);
        achievementCategorizedDao.persist(achievementCategorized);

        achievementCategorized = new AchievementCategorized("Animátor", "Uživatel animoval už na 5-ti zájezdech.", "pizza-slice"); //4
        achievementCategorized.setCategory(categoryDao.findAll().get(5));
        achievementCategorized.setLimit(5);
        achievementCategorizedDao.persist(achievementCategorized);

        achievementCategorized = new AchievementCategorized("Herbert West - ReAnimátor", "Uživatel byl animátorem už na 15-ti zájezdech. Ten oživý každoý zájezd.", "glass-cheers"); //5
        achievementCategorized.setCategory(categoryDao.findAll().get(5));
        achievementCategorized.setLimit(15);
        achievementCategorizedDao.persist(achievementCategorized);
    }

    void setAchievementsAndCategories(){
        List<AchievementCertificate> certificates = achievementCertificateDao.findAll();
        List<AchievementCategorized> categorized =achievementCategorizedDao.findAll();
        List<AchievementSpecial> special = achievementSpecialDao.findAll();
        List<Category> categories = categoryDao.findAll();
        List<Offer> trips = tripDao.findAll();

        //sakra prace :D mozno to pojde
        //IT IS DONE THE HARD WAY BECAUSE AUTHOR (ME) IS LITERALLY BRAINDED RETARD
        //ak budete mat problem sa orientovat v tejto casti nemam vam to za zle je to humus...
        //O.S.

        //Kurz vareni ryb fugu
        Offer trip = trips.get(0);
        trip.addRequired_achievements_categorized(categorized.get(0));
        categorized.get(0).addTrips(trip);
        trip.addGain_achievements_special(special.get(1));
        special.get(1).addTrips(trip);
        trip.setCategory(categories.get(4));
        categories.get(4).add(trip);

        //Vareni ryb Fugu, Praha
        trip =  trips.get(1);
        trip.addRequired_achievements_special(special.get(1));
        special.get(1).addTrips(trip);
        trip.addGain_achievements_special(special.get(2));
        special.get(2).addTrips(trip);
        trip.setCategory(categories.get(0));
        categories.get(0).add(trip);

        trip =  trips.get(2);
        trip.addRequired_achievements_categorized(categorized.get(1));
        categorized.get(1).addTrips(trip);
        trip.setCategory(categories.get(0));
        categories.get(0).add(trip);

        trip =  trips.get(3);
        trip.setCategory(categories.get(0));
        categories.get(0).add(trip);

        trip = trips.get(4);
        trip.setCategory(categories.get(6));
        categories.get(6).add(trip);

        trip = trips.get(5);
        trip.setCategory(categories.get(2));
        categories.get(2).add(trip);

        trip = trips.get(6);
        trip.setCategory(categories.get(5));
        categories.get(5).add(trip);

        //jediny zlepsovak co tu je aj ked by to but ani nemusel...
        for(Offer t : trips) {
            tripDao.update(t);
        }
        for(AchievementCategorized ac : categorized) {
            achievementCategorizedDao.update(ac);
        }
        for(AchievementCertificate cert : certificates) {
            achievementCertificateDao.update(cert);
        }
        for(AchievementSpecial spec : special) {
            achievementSpecialDao.update(spec);
        }
        for(Category category : categories) {
            categoryDao.update(category);
        }

    }

    void createCategories(){
        Category category = new Category("Vaření");//0
        categoryDao.persist(category);

        category = new Category("Archeologie");//1
        categoryDao.persist(category);

        category = new Category("Restaurování");//2
        categoryDao.persist(category);

        category = new Category("Práce instruktora");//3
        categoryDao.persist(category);

        category = new Category("Kurz");//4
        categoryDao.persist(category);

        category = new Category("Práce animátora");//5
        categoryDao.persist(category);

        category = new Category("Humanitarní akce");//6
        categoryDao.persist(category);

        category = new Category("Učitel");//7
        categoryDao.persist(category);

        category = new Category("Práce na stavbě");//8
        categoryDao.persist(category);
    }

    void createUsers(){

        //user Jan
        User user = new User(BCrypt.hashpw("hesloo",BCrypt.gensalt()),"Jan","Testovany","jan@gmail.com");
        user.setRole(Role.USER);

        userDao.persist(user);
        Address address = new Address();
        address.setUser(user);
        address.setCountry("Slovakia");
        address.setCity("Kapusany");
        address.setStreet("Presovska");
        address.setHouseNumber(20);
        address.setZipCode("08001");
        addressDao.persist(address);
        user.setAddress(address);
        userDao.update(user);
        System.out.println("Test user persist.");
        //test pre pruhlasenie na trip
        user.getTravel_journal().setXp_count(11);
        userDao.update(user);

        //user Milan
        user = new User(BCrypt.hashpw("hesloo",BCrypt.gensalt()),"Milan","Netestovany","milan@gmail.com");
        user.setRole(Role.USER);

        userDao.persist(user);
        address = new Address();
        address.setUser(user);
        address.setCountry("Slovakia");
        address.setCity("Piešťany");
        address.setStreet("Teplická");
        address.setHouseNumber(24);
        address.setZipCode("92101");
        addressDao.persist(address);
        user.setAddress(address);
        userDao.update(user);
        System.out.println("Test user persist.");


        //user Julia
        user = new User(BCrypt.hashpw("hesloo",BCrypt.gensalt()),"Julia","Lopez","july1331@gmail.com");
        user.setRole(Role.USER);

        userDao.persist(user);
        address = new Address();
        address.setUser(user);
        address.setCountry("Bohemia");
        address.setCity("Prague");
        address.setStreet("Opletalova");
        address.setHouseNumber(1626);
        address.setZipCode("01001");
        addressDao.persist(address);
        user.setAddress(address);
        userDao.update(user);
        System.out.println("Test user persist.");

        //admin Peter
        user = new User(BCrypt.hashpw("hesloo",BCrypt.gensalt()),"Peter","Testovany","admin@gmail.com");
        user.setRole(Role.ADMIN);
        userDao.persist(user);
        address = new Address();
        address.setUser(user);
        address.setCountry("Slovakia");
        address.setCity("Licartovce");
        address.setStreet("Vranovska");
        address.setHouseNumber(20);
        address.setZipCode("05175");
        addressDao.persist(address);
        user.setAddress(address);
        userDao.update(user);
        System.out.println("Test admin persist.");
    }

    void signUsersToTrips() throws NotAllowedException {
        //JAN
        User user = userDao.findAll().get(0);
        Offer trip = tripDao.findAll().get(0);
        JobSession tripSession = trip.getSessions().get(0);
        TravelJournal travelJournal;

        //test
        /*
        System.out.println("SESSION: " + tripSession.toString());
        System.out.println("TRIP IN SESSION: " + tripSession.getTrip().toString());
        System.out.println("CATEGORIZED IN TRIP: " + trip.getRequired_achievements_categorized().toString());
        System.out.println("CAT IN TRIP DTO: " + translateService.translateAchievementCategorized(trip.getRequired_achievements_categorized().get(0)));
        System.out.println("TRIP DTO: " + translateService.translateTrip(trip).toString());
        System.out.println("TRIP DTO: " + translateService.translateTrip(tripSession.getTrip()).toString());
        System.out.println("SESSION DTO: " + translateService.translateSession(tripSession));
        */

        trip = tripDao.findAll().get(1);
        tripSession = trip.getSessions().get(0);
        signUserToTrip(user, tripSession);

        travelJournal = user.getTravel_journal();
        Enrollment e = travelJournal.getEnrollments().get(0);
        e.setDeposit_was_paid(true);
        enrollmentDao.update(e);

        //JULIA
        user = userDao.findAll().get(2);
        trip = tripDao.findAll().get(0);
        tripSession = trip.getSessions().get(1);

        signUserToTrip(user, tripSession);

        travelJournal = user.getTravel_journal();
        e = travelJournal.getEnrollments().get(0);
        e.setState(EnrollmentState.CANCELED);
        enrollmentDao.update(e);

        //Milan
        user = userDao.findAll().get(1);
        //trip "Fuguvar"
        trip = tripDao.findAll().get(1);
        tripSession = trip.getSessions().get(1);
        signUserToTrip(user, tripSession);

        signUpUserToExpiredEnrollmentsForTesting(user);

        travelJournal = user.getTravel_journal();
        e = travelJournal.getEnrollments().get(2);
        e.setDeposit_was_paid(true);
        e.setState(EnrollmentState.ACTIVE);
        enrollmentDao.update(e);
    }

    void signUserToTrip(User user, JobSession tripSession) throws NotAllowedException {
        JobSessionDto tripSessionDto;

        tripSessionDto = translateService.translateSession(tripSession);
        tripService.signUpToTrip(tripSessionDto, user);
    }

    void addAchievementsToUsers() {
        List<User> users = userDao.findAll();
        List<AchievementCategorized> categorized = achievementCategorizedDao.findAll();
        List<AchievementSpecial> special = achievementSpecialDao.findAll();
        List<AchievementCertificate> certificates = achievementCertificateDao.findAll();
        TravelJournal travelJournal;

        //JAN Jansky
        travelJournal = users.get(0).getTravel_journal();
        travelJournalService.addOwnedCategorizedAchievement(travelJournal, categorized.get(0)); //kuchtik
        travelJournalService.addOwnedSpecialAchievement(travelJournal, special.get(1)); //kuchar ryb fugu

        //MILAN Milanovic
        travelJournal = users.get(1).getTravel_journal();
        travelJournalService.addOwnedSpecialAchievement(travelJournal, special.get(1)); //kuchar ryb fugu
        travelJournalService.addOwnedSpecialAchievement(travelJournal, special.get(3)); //horolezec
        travelJournalService.addOwnedCertificates(travelJournal, certificates.get(0)); //anglictina b2
        travelJournalService.addOwnedCertificates(travelJournal, certificates.get(1)); //spanielcina c1

        //JULIA Julievna
        travelJournal = users.get(2).getTravel_journal();
        travelJournalService.addOwnedCategorizedAchievement(travelJournal, categorized.get(0)); // kuchtik

        //ADMIN Adminovskyj
        //travelJournal = users.get(3).getTravel_journal();
    }

    private void signUpUserToExpiredEnrollmentsForTesting(User user) {
        TravelJournal travelJournal = user.getTravel_journal();

        List<Enrollment> enrollments = travelJournal.getEnrollments();
        JobSession tripSession;
        Offer trip = tripDao.findAll().get(1);
        Enrollment e;

        //enrolment ke tripu, ktery ma datum  ukonceni vcera 0
        tripSession = trip.getSessions().get(3);
        e = createEnrol(tripSession, user);
        enrollments.add(e);
        enrollmentDao.persist(e);

        //enrolment ke tripu, ktery ma datum  ukonceni predevcirem 1
        tripSession = trip.getSessions().get(4);
        e = createEnrol(tripSession, user);
        enrollments.add(e);
        enrollmentDao.persist(e);

        //enrolment ke tripu, ktery ma datum  ukonceni pred tydem 2
        tripSession = trip.getSessions().get(5);
        e = createEnrol(tripSession, user);
        enrollments.add(e);
        enrollmentDao.persist(e);
        travelJournal.setEnrollments(enrollments);
        travelJournalDao.update(travelJournal);

        //get animator trip
        trip = tripDao.findAll().get(6);
        for(int i = 1; i < 7; i++) {
            tripSession = trip.getSessions().get(i);
            e = createEnrol(tripSession, user);
            enrollments.add(e);
            enrollmentDao.persist(e);
        }
        //set paid and closed
        e = user.getTravel_journal().getEnrollments().get(0);
        e.setDeposit_was_paid(true);
        e.setState(EnrollmentState.ACTIVE);
        enrollmentDao.update(e);

        enrollmentService.closeOk(e.getId());

        e = user.getTravel_journal().getEnrollments().get(1);
        e.setDeposit_was_paid(false);
        e.setState(EnrollmentState.ACTIVE);
        enrollmentDao.update(e);
        enrollmentService.closeOk(e.getId());

        for(int i = 5; i < 9; i++) {
            e = user.getTravel_journal().getEnrollments().get(i);
            e.setDeposit_was_paid(true);
            e.setState(EnrollmentState.ACTIVE);
            enrollmentDao.update(e);
            enrollmentService.closeOk(e.getId());
        }
    }

    private Enrollment createEnrol(JobSession tripSession, User user) {
        Enrollment enrollment = new Enrollment();

        enrollment.setDeposit_was_paid(false);
        enrollment.setEnrollDate(LocalDateTime.now());
        enrollment.setActual_xp_reward(0);
        enrollment.setTrip(tripSession.getTrip());
        enrollment.setState(EnrollmentState.ACTIVE);
        enrollment.setTripSession(tripSession);
        enrollment.setTravelJournal(user.getTravel_journal());

        return enrollment;
    }

    private void updateTripRating(Offer trip, double rating) {
        long noReviews;
        if(trip.getTripReviews() == null) {
            noReviews = 1;
        }
        else {
            noReviews = trip.getTripReviews().size();
        }
        double currentRating = trip.getRating();
        trip.setRating((currentRating*(noReviews) + rating)/(noReviews+1));
        tripDao.update(trip);
    }
}
