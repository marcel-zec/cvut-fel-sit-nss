package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.dto.*;
import cz.cvut.fel.nss.parttimejobportal.model.Enrollment;
import cz.cvut.fel.nss.parttimejobportal.model.TripSession;
import cz.cvut.fel.nss.parttimejobportal.dao.TripDao;
import cz.cvut.fel.nss.parttimejobportal.dao.TripSessionDao;
import cz.cvut.fel.rsp.travelandwork.dto.*;
import cz.cvut.fel.nss.parttimejobportal.exception.MissingVariableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class TripSessionService {

    private TripSessionDao tripSessionDao;
    private TripDao tripDao;
    private final TranslateService translateService;

    @Autowired
    public TripSessionService(TripSessionDao tripSessionDao, TripDao tripDao, TranslateService translateService) {
        this.tripSessionDao = tripSessionDao;
        this.tripDao = tripDao;
        this.translateService = translateService;
    }

    @Transactional
    public List<TripSessionDto> findAllInTrip(String trip_short_name) {
        List<TripSession> tripSessions = tripSessionDao.find(trip_short_name);
        List<TripSessionDto> tripSessionDtos = new ArrayList<>();

        for (TripSession ts: tripSessions) {
            tripSessionDtos.add(translateService.translateSession(ts));
        }
        return tripSessionDtos;
    }

    @Transactional
    public void create(String tripId, TripSession tripSession) throws Exception {
        if (tripSession.getTo_date().isBefore(tripSession.getFrom_date())) throw new Exception();
        tripSession.setTrip(tripDao.find(tripId));
        tripSessionDao.persist(tripSession);
    }

    @Transactional
    public TripSession update(TripSession oldSession, TripSession newSession) throws Exception {
        if (oldSession == null || newSession==null) throw new MissingVariableException();

        oldSession.setFrom_date(newSession.getFrom_date());
        oldSession.setPrice(newSession.getPrice());
        oldSession.setTo_date(newSession.getTo_date());
        oldSession.setTrip(newSession.getTrip());
        tripSessionDao.update(oldSession);
        return oldSession;
    }

    @Transactional
    public List<RequestWrapperTripSessionsParticipants> findAllParticipants(String trip_short_name){
        List<TripSession> tripSessions = tripSessionDao.find(trip_short_name);
        List<RequestWrapperTripSessionsParticipants> response = new ArrayList<RequestWrapperTripSessionsParticipants>();
        for (TripSession session:tripSessions) {
            List<RequestWrapperEnrollmentGet> enrollments = new ArrayList<RequestWrapperEnrollmentGet>();
            for (Enrollment enrollment :session.getEnrollments()) {
                UserDto userDto = translateService.translateUser(enrollment.getTravelJournal().getUser());
                EnrollmentDto enrollmentDto = translateService.translateEnrollment(enrollment);
                enrollments.add(new RequestWrapperEnrollmentGet(enrollmentDto,userDto));
            }

            response.add(new RequestWrapperTripSessionsParticipants(translateService.translateSession(session),enrollments));
        }
        return response;
    }

}
