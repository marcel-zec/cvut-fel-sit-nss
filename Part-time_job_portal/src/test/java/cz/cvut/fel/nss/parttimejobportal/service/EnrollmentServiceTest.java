package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.dto.EnrollmentDto;
import cz.cvut.fel.nss.parttimejobportal.dto.TripSessionDto;
import cz.cvut.fel.nss.parttimejobportal.exception.NotAllowedException;
import cz.cvut.fel.nss.parttimejobportal.model.*;
import cz.cvut.fel.nss.parttimejobportal.environment.util.Generator;
import cz.cvut.fel.rsp.travelandwork.model.*;
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
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class EnrollmentServiceTest {

    private Enrollment enrollment;
    private User user;
    private Trip trip;
    private TripSession tripSession,tripSession2,tripSession3;
    private TripSessionDto tripSessionDto,tripSessionDto2,tripSessionDto3;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private TripService tripService;

    @Autowired
    private TranslateService translateService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TripSessionService tripSessionService;

    @Before
    public void prepare() throws Exception {
        user = Generator.generateUser();
        trip = new Trip("test2",11,"Description","shortName1",1000,"Hawaii",2);

        tripSession = new TripSession(trip, LocalDate.now(), LocalDate.now().plusDays(7),2000);
        tripSession2 = new TripSession(trip, LocalDate.now(), LocalDate.now().plusDays(10),2500);
        tripSession3 = new TripSession(trip, LocalDate.now(), LocalDate.now().plusDays(4),1500);

        ArrayList<TripSession> sessions = new ArrayList<TripSession>(Arrays.asList(tripSession, tripSession2, tripSession3));
        trip.setSessions(sessions);
        tripService.create(trip);

        tripSessionService.create(trip.getShort_name(),tripSession);

        Category category = new Category("test");
        trip.setCategory(category);
        categoryService.create(category);

        userService.createUser(user,user.getPassword());
        tripSessionDto = translateService.translateSession(tripSession);
        tripSessionDto2 = translateService.translateSession(tripSession2);
        tripSessionDto3 = translateService.translateSession(tripSession3);

        tripService.signUpToTrip(tripSessionDto,user);
        tripService.signUpToTrip(tripSessionDto2,user);
        tripService.signUpToTrip(tripSessionDto3,user);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllOfUser() throws NotAllowedException {
        assertEquals(translateService.translateEnrollment(user.getTravel_journal().getEnrollments().get(0)).getEnrollDate(), enrollmentService.findAllOfUser(user).get(0).getEnrollDate());
        assertEquals(translateService.translateEnrollment(user.getTravel_journal().getEnrollments().get(0)).getRecieved_achievements_special(), enrollmentService.findAllOfUser(user).get(0).getRecieved_achievements_special());
    }

    @Test
    @Transactional
    @Rollback
    public void findAllOfUserActive() throws NotAllowedException {
        for(int i = 0; i < 3; ++i){
            Assert.assertEquals(EnrollmentState.ACTIVE, enrollmentService.findAllOfUserActive(user).get(i).getState());
            assertEquals(translateService.translateEnrollment(user.getTravel_journal().getEnrollments().get(i)).getEnrollDate(), enrollmentService.findAllOfUserActive(user).get(i).getEnrollDate());
        }
    }

    @Test
    @Transactional
    @Rollback
    public void findAllOfUserFinished() throws NotAllowedException {
        enrollmentService.closeOk(user.getTravel_journal().getEnrollments().get(0).getId());
        enrollmentService.closeOk(user.getTravel_journal().getEnrollments().get(1).getId());
        for(EnrollmentDto e: enrollmentService.findAllOfUserFinished(user)){
            Assert.assertEquals(EnrollmentState.FINISHED, e.getState());
        }
    }

    @Test
    @Transactional
    @Rollback
    public void findDto(){
        assertEquals(user.getTravel_journal().getEnrollments().get(0).getEnrollDate(),enrollmentService.findDto(user.getTravel_journal().getEnrollments().get(0).getId()).getEnrollDate());
    }

    @Test
    @Transactional
    @Rollback
    public void close() throws NotAllowedException {
        enrollmentService.close(translateService.translateEnrollment(user.getTravel_journal().getEnrollments().get(0)));
        for(EnrollmentDto e: enrollmentService.findAllOfUserFinished(user)){
            Assert.assertEquals(EnrollmentState.FINISHED, e.getState());
        }
    }

}
