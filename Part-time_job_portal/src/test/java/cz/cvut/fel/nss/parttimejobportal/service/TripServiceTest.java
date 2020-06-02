package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.exception.*;
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

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class TripServiceTest {

    private Offer trip;

    @Autowired
    private EntityManager em;

    @Autowired
    private OfferService offerService;
    @Autowired
    private UserService userService;
    @Autowired
    private JobReviewService jobReviewService;

    @Before
    public void init() throws BadDateException, MissingVariableException {
        trip = new Offer("test1",10,"Description","shortName",2000,"Hawaii",3,null);

        ArrayList<JobSession> s = new ArrayList<>();
        s.add(new JobSession(trip,LocalDate.now(), LocalDate.now().plusDays(7),2000));

        trip.setSessions(s);
        offerService.create(trip);
    }

    @Test
    @Transactional
    @Rollback
    public void create_CreatesNewTrip() throws BadDateException, MissingVariableException {
        Offer tr = new Offer("test2",11,"Description","shortName1",1000,"Hawaii",2,null);
        ArrayList<JobSession> s = new ArrayList<>();
        s.add(new JobSession(tr,LocalDate.now(), LocalDate.now().plusDays(7),2000));
        tr.setSessions(s);

        offerService.create(tr);
        Assert.assertEquals(tr,offerService.find(tr.getId()));
    }

    @Test
    @Transactional
    @Rollback
    public void find_FindsExistingUserById(){
        Assert.assertEquals(trip,offerService.find(trip.getId()));
    }

    @Test
    @Transactional
    @Rollback
    public void find_FindsExistingUserByString(){
        Assert.assertEquals(trip,offerService.findByString(trip.getShort_name()));
    }

    @Test
    @Transactional
    @Rollback
    public void update_TripUpdated() throws NotFoundException, MissingVariableException, BadDateException, NotAllowedException {
        trip.setSalary(3000);
        offerService.update(trip.getShort_name(),trip);
        Assert.assertEquals(3000f,offerService.find(trip.getId()).getSalary(),0.001);
    }

    @Test
    @Transactional
    @Rollback
    public void remove_TripRemoved() throws NotFoundException, BadPassword, UnauthorizedException, AlreadyExistsException, NotAllowedException {

        User user = Generator.generateUser();
        JobReview jobReview = new JobReview();
        jobReview.setNote("note");
        jobReview.setRating(1);
        jobReview.setDate(LocalDateTime.now());
        user.addJobReview(jobReview);

        userService.createUser(user,user.getPassword());
        jobReviewService.create(jobReview,trip.getSessions().get(0).getId());

        offerService.delete(trip.getShort_name());
        assertThrows(NotFoundException.class, ()-> offerService.find(trip.getId()));
    }


}
