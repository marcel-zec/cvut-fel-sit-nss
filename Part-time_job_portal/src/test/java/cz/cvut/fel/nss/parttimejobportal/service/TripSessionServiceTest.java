package cz.cvut.fel.nss.parttimejobportal.service;


import cz.cvut.fel.nss.parttimejobportal.exception.BadDateException;
import cz.cvut.fel.nss.parttimejobportal.exception.MissingVariableException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.model.Offer;
import cz.cvut.fel.nss.parttimejobportal.model.TripSession;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class TripSessionServiceTest {

    private TripSession tripSession;
    private Offer trip;

    @Autowired
    private TripSessionService tripSessionService;
    private TripService tripService;
//    @Autowired
//    private AchievementService achievementService;


    @Before
    public void prepare() throws BadDateException, MissingVariableException {
        trip = new Offer("test2",11,"Description","shortName1",1000,"Hawaii",2);
        tripSession = new TripSession(trip, LocalDate.now(), LocalDate.now().plusDays(7),2000);

        ArrayList<TripSession> s = new ArrayList<TripSession>() {{add(tripSession);}};
        trip.setSessions(s);

        tripService.create(trip);
    }

    @Test
    @Transactional
    @Rollback
    public void create_CreatesTripSession() throws Exception {
        tripSessionService.create(trip.getShort_name(),tripSession);
        assertTrue(tripService.find(trip.getId()).getSessions().stream().anyMatch(tripSession1 -> tripSession1.equals(tripSession)));
    }

    @Test
    @Transactional
    @Rollback
    public void update_UpdatesTripSession() throws Exception {
        TripSession tripSession1 = new TripSession(trip, LocalDate.now(), LocalDate.now().plusDays(6),1500);

        tripSessionService.update(tripSession,tripSession1);
        assertEquals(tripSession1.getFrom_date(), tripService.find(trip.getId()).getSessions().get(0).getFrom_date());
        assertEquals(1500,tripService.find(trip.getId()).getSessions().get(0).getPrice(),0.001);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllInTrip_FindsAllTripSessions() throws NotFoundException, MissingVariableException, BadDateException {
        TripSession tripSession1 = new TripSession(trip, LocalDate.now(), LocalDate.now().plusDays(6),1500);
        TripSession tripSession2 = new TripSession(trip, LocalDate.now(), LocalDate.now().plusDays(6),1500);
        TripSession tripSession3 = new TripSession(trip, LocalDate.now(), LocalDate.now().plusDays(6),1500);

        ArrayList<TripSession> exp = new ArrayList<TripSession>(){{add(tripSession1);add(tripSession2);add(tripSession3);}};
        trip.setSessions(exp);

        tripService.update(trip.getShort_name(),trip);
        assertEquals(exp,tripSessionService.findAllInTrip(trip.getShort_name()));
    }
}
