package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.dto.*;
import cz.cvut.fel.nss.parttimejobportal.model.Enrollment;
import cz.cvut.fel.nss.parttimejobportal.model.JobSession;
import cz.cvut.fel.nss.parttimejobportal.dao.OfferDao;
import cz.cvut.fel.nss.parttimejobportal.dao.JobSessionDao;
import cz.cvut.fel.nss.parttimejobportal.dto.*;
import cz.cvut.fel.nss.parttimejobportal.exception.MissingVariableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class JobSessionService {

    private JobSessionDao jobSessionDao;
    private OfferDao offerDao;
    private final TranslateService translateService;

    @Autowired
    public JobSessionService(JobSessionDao jobSessionDao, OfferDao offerDao, TranslateService translateService) {
        this.jobSessionDao = jobSessionDao;
        this.offerDao = offerDao;
        this.translateService = translateService;
    }


    /**
     * Get all JobSessionsDto in trip.
     * @param trip_short_name where I looking for job sessions
     * @return  List<JobSessionDto>
     */
    @Transactional
    public List<JobSessionDto> findAllInTrip(String trip_short_name) {
        List<JobSession> tripSessions = jobSessionDao.find(trip_short_name);
        List<JobSessionDto> tripSessionDtos = new ArrayList<>();

        for (JobSession ts: tripSessions) {
            tripSessionDtos.add(translateService.translateSession(ts));
        }
        return tripSessionDtos;
    }


    /**
     * Create new JobSession to offer.
     * @param jobId
     * @param jobSession
     * @throws Exception if from_date is after after_date of session
     */
    @Transactional
    public void create(String jobId, JobSession jobSession) throws Exception {
        if (jobSession.getTo_date().isBefore(jobSession.getFrom_date())) throw new Exception();
        jobSession.setTrip(offerDao.find(jobId));
        jobSessionDao.persist(jobSession);
    }


    /**
     * Update JobSession.
     * @param oldSession
     * @param newSession
     * @return JobSession
     * @throws Exception
     */
    @Transactional
    public JobSession update(JobSession oldSession, JobSession newSession) throws Exception {
        if (oldSession == null || newSession==null) throw new MissingVariableException();

        oldSession.setFrom_date(newSession.getFrom_date());
        oldSession.setCapacity(newSession.getCapacity());
        oldSession.setTo_date(newSession.getTo_date());
        oldSession.setTrip(newSession.getTrip());
        jobSessionDao.update(oldSession);
        return oldSession;
    }


    /**
     * Get all participants of job session
     * @param trip_short_name
     * @return List<RequestWrapperTripSessionsParticipants>
     */
    @Transactional
    public List<RequestWrapperTripSessionsParticipants> findAllParticipants(String trip_short_name){
        List<JobSession> tripSessions = jobSessionDao.find(trip_short_name);
        List<RequestWrapperTripSessionsParticipants> response = new ArrayList<RequestWrapperTripSessionsParticipants>();
        for (JobSession session:tripSessions) {
            List<RequestWrapperEnrollmentGet> enrollments = new ArrayList<RequestWrapperEnrollmentGet>();
            for (Enrollment enrollment :session.getEnrollments()) {
                UserDto userDto = translateService.translateUser(enrollment.getJobJournal().getUser());
                EnrollmentDto enrollmentDto = translateService.translateEnrollment(enrollment);
                enrollments.add(new RequestWrapperEnrollmentGet(enrollmentDto,userDto));
            }

            response.add(new RequestWrapperTripSessionsParticipants(translateService.translateSession(session),enrollments));
        }
        return response;
    }

}
