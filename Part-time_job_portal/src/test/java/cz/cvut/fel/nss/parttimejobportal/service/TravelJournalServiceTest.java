package cz.cvut.fel.nss.parttimejobportal.service;


import cz.cvut.fel.nss.parttimejobportal.exception.BadDateException;
import cz.cvut.fel.nss.parttimejobportal.exception.BadPassword;
import cz.cvut.fel.nss.parttimejobportal.exception.MissingVariableException;
import cz.cvut.fel.nss.parttimejobportal.model.*;
import cz.cvut.fel.nss.parttimejobportal.environment.util.Generator;
import cz.cvut.fel.nss.parttimejobportal.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class TravelJournalServiceTest {

    private TravelJournal travelJournal;
    private User user;
    private Offer trip;
    private Category category;

    @Autowired
    private TravelJournalService travelJournalService;
    @Autowired
    private UserService userService;
    @Autowired
    private OfferService offerService;
    @Autowired
    private CategoryService categoryService;

    @Before
    public void prepare() throws BadPassword, BadDateException, MissingVariableException {
        user = Generator.generateUser();
        userService.createUser(user,user.getPassword());

        category = new Category("TestCat");
        trip = new Offer("test1",10,"Description","shortName",2000,"Hawaii",3);
        trip.setCategory(category);
        ArrayList<JobSession> s = new ArrayList<>();
        s.add(new JobSession(trip, LocalDate.now(), LocalDate.now().plusDays(7),2000));

        trip.setSessions(s);
        categoryService.create(category);
        offerService.create(trip);

        travelJournal = new TravelJournal(user);
        travelJournal.addTrip(trip.getCategory().getId());
        travelJournal.setXp_count(5);

        travelJournal.setTrip_counter(new HashMap<Long,Integer>(){{put(category.getId(),1);}});
    }

    @Test
    @Transactional
    @Rollback
    public void addTrip(){
        travelJournalService.addTrip(travelJournal.getId(),trip.getId());
        Assert.assertEquals(travelJournal.getId(),userService.find(user.getId()).getTravel_journal().getId());
    }
}











