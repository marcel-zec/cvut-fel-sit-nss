package cz.cvut.fel.nss.parttimejobportal.service;


import cz.cvut.fel.nss.parttimejobportal.dao.AddressDao;
import cz.cvut.fel.nss.parttimejobportal.dao.ManagerDao;
import cz.cvut.fel.nss.parttimejobportal.dto.ManagerDto;
import cz.cvut.fel.nss.parttimejobportal.exception.BadDateException;
import cz.cvut.fel.nss.parttimejobportal.exception.MissingVariableException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotAllowedException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.model.Address;
import cz.cvut.fel.nss.parttimejobportal.model.Manager;
import cz.cvut.fel.nss.parttimejobportal.model.Offer;
import cz.cvut.fel.nss.parttimejobportal.model.JobSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
public class JobSessionServiceTest {

    private JobSession tripSession;
    private Offer trip;
    private Manager manager;

    @Autowired
    private JobSessionService jobSessionService;
    private OfferService offerService;
    private ManagerDao managerDao;
    private AddressDao addressDao;
//    @Autowired
//    private AchievementService achievementService;


    @Before
    public void prepare() throws BadDateException, MissingVariableException {
        manager = new Manager(BCrypt.hashpw("hesloo",BCrypt.gensalt()),"Jan","Kolar","manager@gmail.com","777 850 167","TestCompany, s.r.o");

        Address address = new Address();
        address.setUser(manager);
        address.setCountry("Slovakia");
        address.setCity("Licartovce");
        address.setStreet("Vranovska");
        address.setHouseNumber(20);
        address.setZipCode("05175");
        manager.setAddress(address);

        trip = new Offer("test2",11,"Description","shortName1",1000,"Hawaii",2,manager);
        tripSession = new JobSession(trip, LocalDate.now(), LocalDate.now().plusDays(7),2000);

        ArrayList<JobSession> s = new ArrayList<JobSession>() {{add(tripSession);}};
        trip.setSessions(s);

        offerService.create(trip);
    }

    @Test
    @Transactional
    @Rollback
    public void create_CreatesTripSession() throws Exception {
        jobSessionService.create(trip.getShort_name(),tripSession);
        assertTrue(offerService.find(trip.getId()).getSessions().stream().anyMatch(tripSession1 -> tripSession1.equals(tripSession)));
    }

    @Test
    @Transactional
    @Rollback
    public void update_UpdatesTripSession() throws Exception {
        JobSession tripSession1 = new JobSession(trip, LocalDate.now(), LocalDate.now().plusDays(6),1500);

        jobSessionService.update(tripSession,tripSession1);
        assertEquals(tripSession1.getFrom_date(), offerService.find(trip.getId()).getSessions().get(0).getFrom_date());
        assertEquals(1500,offerService.find(trip.getId()).getSessions().get(0).getCapacity(),0.001);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllInTrip_FindsAllTripSessions() throws NotFoundException, MissingVariableException, BadDateException, NotAllowedException {
        JobSession tripSession1 = new JobSession(trip, LocalDate.now(), LocalDate.now().plusDays(6),1500);
        JobSession tripSession2 = new JobSession(trip, LocalDate.now(), LocalDate.now().plusDays(6),1500);
        JobSession tripSession3 = new JobSession(trip, LocalDate.now(), LocalDate.now().plusDays(6),1500);

        ArrayList<JobSession> exp = new ArrayList<JobSession>(){{add(tripSession1);add(tripSession2);add(tripSession3);}};
        trip.setSessions(exp);

        offerService.update(trip.getShort_name(),trip);
        assertEquals(exp,jobSessionService.findAllInTrip(trip.getShort_name()));
    }
}
