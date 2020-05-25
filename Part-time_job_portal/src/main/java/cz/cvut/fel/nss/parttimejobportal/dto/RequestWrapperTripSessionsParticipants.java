package cz.cvut.fel.nss.parttimejobportal.dto;

import java.util.List;

public class RequestWrapperTripSessionsParticipants {

    private JobSessionDto session;
    private List<RequestWrapperEnrollmentGet> enrollments;


    public RequestWrapperTripSessionsParticipants(JobSessionDto session, List<RequestWrapperEnrollmentGet> enrollments) {
        this.session = session;
        this.enrollments = enrollments;
    }

    public JobSessionDto getSession() {
        return session;
    }

    public void setSession(JobSessionDto session) {
        this.session = session;
    }

    public List<RequestWrapperEnrollmentGet> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<RequestWrapperEnrollmentGet> enrollments) {
        this.enrollments = enrollments;
    }

}
