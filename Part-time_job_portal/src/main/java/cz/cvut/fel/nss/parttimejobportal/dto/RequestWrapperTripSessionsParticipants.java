package cz.cvut.fel.nss.parttimejobportal.dto;

import java.util.List;

public class RequestWrapperTripSessionsParticipants {

    private TripSessionDto session;
    private List<RequestWrapperEnrollmentGet> enrollments;


    public RequestWrapperTripSessionsParticipants(TripSessionDto session, List<RequestWrapperEnrollmentGet> enrollments) {
        this.session = session;
        this.enrollments = enrollments;
    }

    public TripSessionDto getSession() {
        return session;
    }

    public void setSession(TripSessionDto session) {
        this.session = session;
    }

    public List<RequestWrapperEnrollmentGet> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<RequestWrapperEnrollmentGet> enrollments) {
        this.enrollments = enrollments;
    }

}
