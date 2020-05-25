package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.exception.*;
import cz.cvut.fel.nss.parttimejobportal.model.Offer;
import cz.cvut.fel.nss.parttimejobportal.model.TripReview;
import cz.cvut.fel.nss.parttimejobportal.model.TripSession;
import cz.cvut.fel.nss.parttimejobportal.model.User;
import cz.cvut.fel.nss.parttimejobportal.environment.util.Generator;
import cz.cvut.fel.nss.parttimejobportal.exception.*;
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
    private TripService tripService;
    @Autowired
    private UserService userService;
    @Autowired
    private TripReviewService tripReviewService;

    @Before
    public void init() throws BadDateException, MissingVariableException {
        trip = new Offer("test1",10,"Description","shortName",2000,"Hawaii",3);

        ArrayList<TripSession> s = new ArrayList<>();
        s.add(new TripSession(trip,LocalDate.now(), LocalDate.now().plusDays(7),2000));

        trip.setSessions(s);
        tripService.create(trip);
    }

    @Test
    @Transactional
    @Rollback
    public void create_CreatesNewTrip() throws BadDateException, MissingVariableException {
        Offer tr = new Offer("test2",11,"Description","shortName1",1000,"Hawaii",2);
        ArrayList<TripSession> s = new ArrayList<>();
        s.add(new TripSession(tr,LocalDate.now(), LocalDate.now().plusDays(7),2000));
        tr.setSessions(s);

        tripService.create(tr);
        Assert.assertEquals(tr,tripService.find(tr.getId()));
    }

    @Test
    @Transactional
    @Rollback
    public void find_FindsExistingUserById(){
        Assert.assertEquals(trip,tripService.find(trip.getId()));
    }

    @Test
    @Transactional
    @Rollback
    public void find_FindsExistingUserByString(){
        Assert.assertEquals(trip,tripService.findByString(trip.getShort_name()));
    }

    @Test
    @Transactional
    @Rollback
    public void update_TripUpdated() throws NotFoundException, MissingVariableException, BadDateException {
        trip.setSalary(3000);
        tripService.update(trip.getShort_name(),trip);
        Assert.assertEquals(3000f,tripService.find(trip.getId()).getSalary(),0.001);
    }

    @Test
    @Transactional
    @Rollback
    public void remove_TripRemoved() throws NotFoundException, BadPassword, UnauthorizedException, AlreadyExistsException {

        User user = Generator.generateUser();
        TripReview tripReview = new TripReview();
        tripReview.setNote("note");
        tripReview.setRating(1);
        tripReview.setDate(LocalDateTime.now());
        user.addTripReview(tripReview);

        userService.createUser(user,user.getPassword());
        tripReviewService.create(tripReview,trip.getSessions().get(0).getId());

        tripService.delete(trip.getShort_name());
        assertThrows(NotFoundException.class, ()-> tripService.find(trip.getId()));
    }


}
