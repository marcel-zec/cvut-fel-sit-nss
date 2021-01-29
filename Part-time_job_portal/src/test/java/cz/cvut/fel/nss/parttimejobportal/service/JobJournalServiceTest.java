package cz.cvut.fel.nss.parttimejobportal.service;


import cz.cvut.fel.nss.parttimejobportal.exception.BadDateException;
import cz.cvut.fel.nss.parttimejobportal.exception.BadPassword;
import cz.cvut.fel.nss.parttimejobportal.exception.MissingVariableException;
import cz.cvut.fel.nss.parttimejobportal.model.*;
import cz.cvut.fel.nss.parttimejobportal.environment.util.Generator;
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
public class JobJournalServiceTest {

    private JobJournal jobJournal;
    private AbstractUser user;
    private Offer trip;
    private Category category;

    @Autowired
    private JobJournalService jobJournalService;
    @Autowired
    private UserService userService;
    @Autowired
    private OfferService offerService;
    @Autowired
    private CategoryService categoryService;

    @Before
    public void prepare() throws BadPassword, BadDateException, MissingVariableException {
        user = Generator.generateUser();
        userService.createUser((User) user,user.getPassword());

        category = new Category("TestCat");
        trip = new Offer("test1",10,"Description","shortName",2000,"Hawaii",3,null);
        trip.setCategory(category);
        ArrayList<JobSession> s = new ArrayList<>();
        s.add(new JobSession(trip, LocalDate.now(), LocalDate.now().plusDays(7),2000));

        trip.setSessions(s);
        categoryService.create(category);
        offerService.create(trip);

        jobJournal = new JobJournal((User) user);
        jobJournal.addTrip(trip.getCategory().getId());
        jobJournal.setXp_count(5);

        jobJournal.setTrip_counter(new HashMap<Long,Integer>(){{put(category.getId(),1);}});
    }

    @Test
    @Transactional
    @Rollback
    public void addTrip(){
        jobJournalService.addTrip(jobJournal.getId(),trip.getId());
        Assert.assertEquals(jobJournal.getId(),userService.find(user.getId()).getTravel_journal().getId());
    }
}











